package sandbox;

import javax.persistence.*;


public class Demo {

	EntityManagerFactory emf;

	public static void main(String[] args) {
		Demo d = new Demo();

		User peterpan = new User("Peter Pan");
		d.add(peterpan);
		System.out.println("#### Added: " + peterpan);

//		User u2 = d.add("Peter Pan");
		d.queryUser("Peter Pan");
		d.printAllUsers();
		d.close();
	}


	public Demo() {
		emf = Persistence.createEntityManagerFactory("helloworld");
	}

	public void close() {
		emf.close();
	}

	public void add(User u) {
		// Persist the user
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(u);
		tx.commit();
		em.close();
	}


	public User get(Long id) {
		EntityManager em = emf.createEntityManager();
		User u = em.find(User.class, id);
		return u;
	}


	public User queryUser(String name) {
		String jpql = "FROM User WHERE name = :name";

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("name", name);
		User u = (User) q.getSingleResult();
		return u;
	}


	public void printAllUsers() {
		String jpql = "FROM User u ORDER BY u.name ASC";
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		System.out.println("#### All users:");
		for(Object uo: q.getResultList()) {
			User u = (User) uo;
			System.out.println("#### - " + u);
		}
	}
}