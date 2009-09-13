package sandbox;


import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.jboss.virtual.plugins.context.vfs.AssembledContextFactory;
import org.jboss.embedded.Bootstrap;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Field;

import sun.misc.VM;


public class HelloWorldTest extends AbstractJbossEmbeddedTest {
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

	@Before
	public void setUp() throws DeploymentException {
		deploy();
	}

	@After
	public void tearDown() {
		undeploy();
	}

	@Test
	public void testRemote() throws NamingException {
		InitialContext ctx = new InitialContext();
		HelloWorldRemote h = (HelloWorldRemote) ctx.lookup("HelloWorld/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}

	@Test
	public void testLocal() throws NamingException {
		InitialContext ctx = new InitialContext();
		HelloWorldLocal h = (HelloWorldLocal) ctx.lookup("HelloWorld/local");
		assertEquals(h.helloLocal(), "Hello local");
	}

	@Test
	public void testTull() throws NamingException {
		InitialContext ctx = new InitialContext();
		HelloWorldRemote h = (HelloWorldRemote) ctx.lookup("HelloWorld/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}




	//////////////////////////////////////////////////////////////
	// Customizing the deployed jar-file.
	//
	//////////////////////////////////////////////////////////////

	/** Print the contents of the deployed jar-file. */
//	protected void addToJar(AssembledDirectory jar, File target, File classes,
//							File testclasses)
//			throws ClassNotFoundException {
//		super.addToJar(jar, target, classes, testclasses);
//		JbossJarCreator.printJar(jar);
//	}


	/** Exactly the same as the superclass.. */
//	protected void addToJar(AssembledDirectory jar, File target, File classes,
//							File testclasses)
//			throws ClassNotFoundException {
//		new JbossJarCreator(jar, classes).addToJar();
//		new JbossTestJarCreator(jar, testclasses).addToJar();
//	}

	/** Only deploy the main sources and resources, and ignore a class, directory
	 * and file. Note that none of the files are actually ignored. */
//	protected void addToJar(AssembledDirectory jar, File target, File classes,
//							File testclasses)
//			throws ClassNotFoundException {
//		new JbossJarCreator(jar, classes).ignorePaths("some.MyClass",
//				"WEB-INF/", "META-INF/stuff.xml").addToJar();
//	}

}