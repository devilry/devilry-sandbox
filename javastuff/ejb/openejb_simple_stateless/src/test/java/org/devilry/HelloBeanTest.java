package org.devilry;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class HelloBeanTest {
	Context ctx;

	@BeforeMethod
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.LocalInitialContextFactory");
		ctx = new InitialContext(props);
	}

	@Test
	public void sayHello() throws NamingException {
		Object ref = ctx.lookup("HelloBeanRemote");
		Hello h = (Hello) PortableRemoteObject.narrow(ref, Hello.class);
		String result = h.sayHello();
		assertEquals(result, "Hello world!");
	}
}
