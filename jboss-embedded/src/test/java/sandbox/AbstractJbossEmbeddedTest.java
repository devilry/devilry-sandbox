package sandbox;

import org.jboss.embedded.Bootstrap;
import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.jboss.virtual.plugins.context.vfs.AssembledContextFactory;
import org.jboss.deployers.spi.DeploymentException;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.io.File;

import sun.misc.VM;


public abstract class AbstractJbossEmbeddedTest {
	private static AssembledDirectory jar;


	private static void setVMFieldValue(String name, Boolean value) {
		try {
			Field field = VM.class.getDeclaredField(name);
			boolean oldState = field.isAccessible();
			field.setAccessible(true);
			field.set(null, value);
			field.setAccessible(oldState);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	protected static void addToJar(AssembledDirectory j, File target, File classes,
							File testclasses)
			throws ClassNotFoundException {
		new JbossJarCreator(j, classes).addToJar();
		new JbossTestJarCreator(j, testclasses).addToJar();
	}

	protected static void deploy() throws DeploymentException {
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
		try {
			Bootstrap.getInstance().deploy(jar);
		}
		catch (DeploymentException e) {
			throw new RuntimeException("Unable to deploy", e);
		}
	}

	protected static void undeploy(){
		try {
			Bootstrap.getInstance().undeploy(jar);
		} catch (DeploymentException e) {
			throw new RuntimeException("Unable to undeploy", e);
		}
//		AssembledContextFactory.getInstance().remove(jar);
//		Bootstrap.getInstance().shutdown();
	}
}