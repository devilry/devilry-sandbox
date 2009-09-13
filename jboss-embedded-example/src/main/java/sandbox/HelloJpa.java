package sandbox;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class HelloJpa implements HelloJpaRemote, HelloJpaLocal {
	@PersistenceContext(unitName = "DevilryCore")
	protected EntityManager em;

	public String helloWorld() {
		return "Hello remote";
	}

	public String helloLocal() {
		return "Hello local";
	}
}