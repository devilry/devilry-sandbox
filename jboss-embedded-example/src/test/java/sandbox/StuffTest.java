package sandbox;


import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;


public class StuffTest extends AbstractJbossEmbeddedTest {
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
		StuffRemote h = (StuffRemote) ctx.lookup("Stuff/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}

	@Test
	public void testLocal() throws NamingException {
		InitialContext ctx = new InitialContext();
		StuffLocal h = (StuffLocal) ctx.lookup("Stuff/local");
		assertEquals(h.helloLocal(), "Hello local");
	}

	@Test
	public void testTull() throws NamingException {
		InitialContext ctx = new InitialContext();
		StuffRemote h = (StuffRemote) ctx.lookup("Stuff/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}
}