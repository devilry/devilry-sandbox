package org.devilry;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

public class HelloClient2 {
	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.RemoteInitialContextFactory");
		props.put(Context.PROVIDER_URL, "ejbd://127.0.0.1:4201");
		Context ctx = new InitialContext(props);

		Object ref = ctx.lookup("HelloBean2Remote");
		Hello2 h = (Hello2) PortableRemoteObject.narrow(ref, Hello2.class);

		h.addHelloworld("en", "Hello world!");
		System.out.println(h.getHelloworld("en"));
		
		h.addHelloworld("no", "Hei verden!");
		System.out.println(h.getHelloworld("no"));
	}
}
