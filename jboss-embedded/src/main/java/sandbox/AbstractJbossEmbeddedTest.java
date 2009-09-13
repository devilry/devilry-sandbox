package sandbox;

import org.jboss.embedded.Bootstrap;
import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.jboss.virtual.plugins.context.vfs.AssembledContextFactory;
import org.jboss.deployers.spi.DeploymentException;

import java.lang.reflect.Field;
import java.io.File;

import sun.misc.VM;


public abstract class AbstractJbossEmbeddedTest {
	private AssembledDirectory jar;


	private void setVMFieldValue(String name, Boolean value) {
		try {
			Field field = VM.class.getDeclaredField(name);
			boolean oldState = field.isAccessible();
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(oldState);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to set VM field value.", ex);
		}
	}

	/** Add files to the deployed jar.
	 *
	 * @param theJar The jar file.
	 * @param target The target/ directory.
	 * @param classes The target/classes directory.
	 * @param testclasses The target/test-classes directory.
	 *  */
	protected void addToJar(AssembledDirectory theJar, File target,
								   File classes,
								   File testclasses)
			throws ClassNotFoundException {
		new JbossJarCreator(theJar, classes).addToJar();
		new JbossJarCreatorIgnoreBoostrapfiles(theJar, testclasses).addToJar();
	}

	protected void deploy() throws DeploymentException {
		// This is required to make java 1.6 work.
		setVMFieldValue("defaultAllowArraySyntax", Boolean.TRUE);
		setVMFieldValue("allowArraySyntax", Boolean.TRUE);

		// Only start the server once..
		if (!Bootstrap.getInstance().isStarted()) {
			Bootstrap.getInstance().bootstrap();
		}

		// Create jar file
		jar = AssembledContextFactory.getInstance().create("ejbTestCase.jar");
		File currentdir = new File(System.getProperty("user.dir"));
		File target = new File(currentdir, "target");
		File classes = new File(target, "classes");
		File testclasses = new File(target, "test-classes");
		try {
			addToJar(jar, target, classes, testclasses);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load classes", e);
		}

		// Deploy jar file to the embedded JBoss server
		// The while loop is a workaround for a bug, which I believe might
		// be a result of delay when undeploying, but Thread.wait(...) does
		// not seem to help, so it might be something else. failedCount of
		// 3 seems to be the magic number:) The workaround to avoid massive
		// amounts of noise in the logs, is in src/test/bootstrap/log4j.xml.
		int failedCount = 0;
		while (true) {
			try {
				Bootstrap.getInstance().deploy(jar);
			} catch (DeploymentException e) {
				if (failedCount == 3) {
					throw new RuntimeException("Unable to deploy", e);
				} else {
					failedCount ++;
					continue;
				}
			}
			break;
		}
	}

	protected void undeploy() {
		try {
			Bootstrap.getInstance().undeploy(jar);
		} catch (DeploymentException e) {
			throw new RuntimeException("Unable to undeploy", e);
		}
	}
}