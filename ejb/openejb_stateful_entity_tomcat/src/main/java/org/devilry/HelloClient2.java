package org.devilry;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class HelloClient2 {
	public static void main(String[] args) throws Exception {
		String serverUrl = "http://localhost:8080/openejb/ejb";

		if(args.length < 2) {
			System.out.println(
				"Usage: java org.devilry.HelloClient2 <lang> <translation> [server-url]");
			System.out.printf("%nserver-url defaults to: %s%n", serverUrl);
			System.exit(1);
		}
		String lang = args[0];
		String trans = args[1];

		if(args.length == 3)
			serverUrl = args[2];


		// Setup connection properties
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.RemoteInitialContextFactory");
		props.put(Context.PROVIDER_URL, serverUrl);
		Context ctx = new InitialContext(props);

		// Connect to server
		Object ref = null;
		try {
			ref = ctx.lookup("HelloBean2Remote");
		} catch(NamingException e) {
			System.out.printf("FAILED to connect to the server '%s'!%n", serverUrl);
			System.out.println(e.getMessage());
			System.exit(2);
		}
		Hello2 h = (Hello2) PortableRemoteObject.narrow(ref, Hello2.class);



		System.out.println("Currently stored translations:");
		for(String s: h.getAllTranslations())
			System.out.println(" - " + s);

		if(h.getHelloworld(lang) != null) {
			System.out.printf(
				"ERROR! Translation for lang \"%s\" already exists%n", lang);
			System.exit(3);
		}

		System.out.printf("Adding %s:%s%n", lang, trans);
		h.addHelloworld(lang, trans);
		
		String stored = h.getHelloworld(lang);
		if(!stored.equals(trans))
			System.out.println("ERROR! the bean does not return the stored translation.");
		else
			System.out.println("Translation storage SUCCESSFUL:)");
	}
}
