package sandbox;

import javax.ejb.Local;

@Local
public interface HelloWorldLocal {
	public String helloLocal();
}
