package sandbox;

import javax.ejb.Remote;

@Remote
public interface HelloWorldRemote {
	public String helloWorld();
}
