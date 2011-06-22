package sandbox;

import javax.ejb.Stateless;

@Stateless
public class HelloWorld implements HelloWorldRemote, HelloWorldLocal {
	public String helloWorld() {
		return "Hello remote";
	}

	public String helloLocal() {
		return "Hello local";
	}
}