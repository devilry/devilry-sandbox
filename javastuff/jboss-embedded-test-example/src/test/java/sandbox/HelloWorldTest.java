package sandbox;


import org.jboss.deployers.spi.DeploymentException;
import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.devilry.test.AbstractJbossEmbeddedTest;
import org.devilry.test.JbossJarCreator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;


public class HelloWorldTest extends AbstractJbossEmbeddedTest {

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
	protected void addToJar(AssembledDirectory jar, File target, File classes,
							File testclasses)
			throws ClassNotFoundException {
		super.addToJar(jar, target, classes, testclasses);
		JbossJarCreator.printJar(jar);
	}


	/** Exactly the same as the superclass.. */
//	protected void addToJar(AssembledDirectory jar, File target, File classes,
//							File testclasses)
//			throws ClassNotFoundException {
//		new JbossJarCreator(jar, classes).addToJar();
//		new JbossJarCreatorIgnoreBoostrapfiles(jar, testclasses).addToJar();
//	}

	/** Only deploy the main sources and resources, and ignore a class, directory
	 * and file. Note that none of the files are actually ignored. */
//	protected void addToJar(AssembledDirectory jar, File target, File classes,
//							File testclasses)
//			throws ClassNotFoundException {
//		new JbossJarCreator(jar, classes).addIgnore("some.MyClass",
//				"WEB-INF/", "META-INF/stuff.xml").addToJar();
//	}

}