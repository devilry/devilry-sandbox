package sandbox;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;


@Test
public class UserTest {
	EntityManagerFactory emf;
	User userA, userB, userC;

	@BeforeMethod
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("helloworld");
		userA = new User("A");
		userB = new User("B");
		userC = new User("C");
	}

	@BeforeMethod(dependsOnMethods = { "setUp" })
	public void createSomeUsers() {
		userA.setBestFriend(userB);
		userB.setBestFriend(userC);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(userA);
		em.persist(userB);
		em.persist(userC);
		tx.commit();
		em.close();
	}

	@AfterMethod
	public void tearDown() {
		emf.close();
	}

	@Test
	public void persistUser() {
		String jpql = "FROM User s WHERE s.id = :id";

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("id", userA.getId());
		User fromdb = (User) q.getSingleResult();
		assertEquals(fromdb.getId(), userA.getId());
	}

	@Test
	public void queryManyToOne() {
		// This query will only work with EAGER fetch (in User).
		String jpql = "FROM User s WHERE s.bestFriend.name = :bestFriend";

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("bestFriend", "B");
		User fromdb = (User) q.getSingleResult();
		assertEquals(fromdb.getId(), userA.getId());
	}

	@Test
	public void queryManyToOne2() {
		String jpql = "FROM User s WHERE id = :id";
		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("id", userA.getId());
		User fromdb = (User) q.getSingleResult();
		assertEquals(fromdb.getBestFriend().getId(), userB.getId());
		assertEquals(fromdb.getBestFriend().getBestFriend().getId(), userC
				.getId());
		assertNotSame(fromdb, userA);
	}
}