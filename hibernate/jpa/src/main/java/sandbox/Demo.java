package sandbox;

import javax.persistence.*;


public class Demo {

	EntityManagerFactory emf;

	public static void main(String[] args) {
		String username = args[0];
		Demo d = new Demo();
		User u = d.add(username);
		d.get(u.getId());
		d.queryUser(username);
		d.printAllUsers();
		d.close();
	}


	public Demo() {
		emf = Persistence.createEntityManagerFactory("helloworld");
	}

	public void close() {
		emf.close();
	}

	public User add(String name) {
		User u = new User(name);

		// Persist the user
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(u);
		tx.commit();
		em.close();

		// Print - note that u.getId() has a value!
		System.err.printf("** Added user ID: %d, NAME: %s%n", u.getId(),
				u.getName());

		return u;
	}


	public User get(Long id) {
		EntityManager em = emf.createEntityManager();
		User u = em.find(User.class, id);

		System.err.printf("** Got user ID: %d, NAME: %s%n", u.getId(),
				u.getName());
		return u;
	}


	public User queryUser(String name) {
		String jpql = "FROM User WHERE name = :name";
		//String jpql = "from sandbox.User where name = :name"; // the same

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("name", name);
		User u = (User) q.getSingleResult();

		System.err.printf("** FOUND user ID: %d, NAME: %s%n", u.getId(),
				u.getName());
		return u;
	}


	public void printAllUsers() {
		// All 3 are the same, but the upper two are collision safer?
		//String jpql = "SELECT u FROM sandbox.User u ORDER BY u.name ASC";
		//String jpql = "FROM sandbox.User u ORDER BY u.name ASC";
		String jpql = "FROM User u ORDER BY u.name ASC";

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		System.out.println("** All users:");
		for(Object uo: q.getResultList()) {
			User u = (User) uo;
			System.out.printf("    - %s (%d)%n", u.getName(), u.getId());
		}
	}
}
