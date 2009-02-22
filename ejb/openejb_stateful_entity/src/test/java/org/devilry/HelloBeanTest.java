package org.devilry;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class HelloBeanTest {
	Context ctx;

	@BeforeMethod
	public void setUp() throws Exception {
		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");
		p.put("helloDatabase", "new://Resource?type=DataSource");
		p.put("helloDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
		p.put("helloDatabase.JdbcUrl", "jdbc:hsqldb:mem:hellodb");

		p.put("helloDatabaseUnmanaged", "new://Resource?type=DataSource");
		p.put("helloDatabaseUnmanaged.JdbcDriver", "org.hsqldb.jdbcDriver");
		p.put("helloDatabaseUnmanaged.JdbcUrl", "jdbc:hsqldb:mem:hellodb");
		p.put("helloDatabaseUnmanaged.JtaManaged", "false");

		ctx = new InitialContext(p);
	}

	@Test
	public void addHelloworld() throws NamingException {
		Object ref = ctx.lookup("HelloBeanRemote");
		Hello h = (Hello) ref;
		h.addHelloworld("en", "Hello world!");
		String result = h.getHelloworld("en");
		assertEquals(result, "Hello world!");
	}
}
