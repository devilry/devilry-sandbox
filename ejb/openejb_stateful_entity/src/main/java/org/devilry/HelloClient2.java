package org.devilry;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

public class HelloClient2 {
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Requires two arguments: <lang> <translation>");
			System.exit(1);
		}

		String lang = args[0];
		String trans = args[1];

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.RemoteInitialContextFactory");
		props.put(Context.PROVIDER_URL, "ejbd://127.0.0.1:4201");
		Context ctx = new InitialContext(props);

		Object ref = ctx.lookup("HelloBean2Remote");
		Hello2 h = (Hello2) PortableRemoteObject.narrow(ref, Hello2.class);

		if(h.getHelloworld(lang) != null) {
			System.out.printf(
				"ERROR! Translation for lang %s already exists%n", lang);
			System.exit(2);
		}

		System.out.printf("Adding %s:%s%n", lang, trans);
		h.addHelloworld(lang, trans);
		System.out.printf("Translation stored in db for lang %s: %s%n", lang,
				h.getHelloworld(lang));
	}
}
