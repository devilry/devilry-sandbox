package sandbox;


import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.deployers.spi.DeploymentException;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;


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

	/*
	protected void addToJar(AssembledDirectory jar, File target, File classes,
							File testclasses)
			throws ClassNotFoundException {
		super.addToJar(jar, target, classes, testclasses);
		JbossJarCreator.printJar(jar);
	}
	*/

}