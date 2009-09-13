package sandbox;

import javax.ejb.Local;

@Local
public interface StuffLocal {
	public String helloLocal();
}
