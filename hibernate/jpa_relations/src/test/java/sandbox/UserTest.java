package sandbox;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


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
		Collection<User> allFriends = new LinkedList<User>();
		allFriends.add(userB);
		allFriends.add(userC);
		userA.setAllFriends(allFriends);

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
		EntityManager em = emf.createEntityManager();
		User fromdb = em.find(User.class, userA.getId());
		assertEquals(fromdb.getId(), userA.getId());
		assertEquals(fromdb.getName(), userA.getName());
	}

	@Test
	public void queryManyToOne() {
		// This query will only work with EAGER fetch (in User).
		String jpql = "FROM User s WHERE s.bestFriend.name = :bestFriend";

		EntityManager em = emf.createEntityManager();
		Query q = em.createQuery(jpql);
		q.setParameter("bestFriend", "B");
		User fromdb = (User) q.getSingleResult();
		em.close();
		assertEquals(fromdb.getId(), userA.getId());
	}

	@Test
	public void queryManyToOne2() {
		EntityManager em = emf.createEntityManager();
		User fromdb = em.find(User.class, userA.getId());
		em.close();

		assertEquals(fromdb.getBestFriend().getId(), userB.getId());
		assertEquals(fromdb.getBestFriend().getBestFriend().getId(), userC
				.getId());
		assertNotSame(fromdb, userA);
	}
	
	@Test
	public void setAllFriends() {
		EntityManager em = emf.createEntityManager();
		User a = em.find(User.class, userA.getId());
		assertFalse(a.getAllFriends().contains(userB));

		// Note that we have to fetch the object (user B) from the db.
		User b = em.find(User.class, userB.getId());
		assertTrue(a.getAllFriends().contains(b));
	}
}