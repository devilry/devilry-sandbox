package sandbox;

import javax.ejb.Remote;

@Remote
public interface HelloJpaRemote {
	public String helloWorld();
}
