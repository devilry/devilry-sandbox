package sandbox;


import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.deployers.spi.DeploymentException;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.devilry.test.AbstractJbossEmbeddedTest;


public class HelloJpaTest extends AbstractJbossEmbeddedTest {
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
		HelloJpaRemote h = (HelloJpaRemote) ctx.lookup("HelloJpa/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}

	@Test
	public void testLocal() throws NamingException {
		InitialContext ctx = new InitialContext();
		HelloJpaLocal h = (HelloJpaLocal) ctx.lookup("HelloJpa/local");
		assertEquals(h.helloLocal(), "Hello local");
	}

	@Test
	public void testTull() throws NamingException {
		InitialContext ctx = new InitialContext();
		HelloJpaRemote h = (HelloJpaRemote) ctx.lookup("HelloJpa/remote");
		assertEquals(h.helloWorld(), "Hello remote");
	}
}