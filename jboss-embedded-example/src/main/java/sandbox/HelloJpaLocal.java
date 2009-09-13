package sandbox;

import javax.ejb.Local;

@Local
public interface HelloJpaLocal {
	public String helloLocal();
}
