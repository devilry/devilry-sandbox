package sandbox;

import javax.ejb.Stateless;

@Stateless
public class Stuff implements StuffRemote, StuffLocal {
	public String helloWorld() {
		return "Hello remote";
	}

	public String helloLocal() {
		return "Hello local";
	}
}