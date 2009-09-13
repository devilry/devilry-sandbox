package sandbox;

import javax.ejb.Remote;

@Remote
public interface StuffRemote {
	public String helloWorld();
}
