package sandbox;

import org.hibernate.Session;
import org.hibernate.Query;


public class Demo {
	public static void main(String[] args) {
		Demo d = new Demo();
		Long pp = d.add("Peter Pan");
		Long sm = d.add("Spiderman");
		d.get(pp);
		d.get(sm);
		d.queryUser("Spiderman");
		d.queryUser("Peter Pan");
	}


	protected Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	public Long add(String name) {
		User u = new User();
		u.setName(name);

		Session session = getSession();
		session.beginTransaction();

		Long id = (Long) session.save(u);
		System.err.printf("** Added user ID: %d, NAME: %s%n", id, name);

		session.getTransaction().commit();
		//HibernateUtil.getSessionFactory().close();

		return id;
	}

	public User get(Long id) {
		User u = (User) getSession().get(User.class, id);
		System.err.printf("** Got user ID: %d, NAME: %s%n", u.getId(),
				u.getName());
		return u;
	}

	public User queryUser(String name) {
		String hql = "from User where name = :name";
		//String hql = "from sandbox.User where name = :name"; // the same
		Query q = getSession().createQuery(hql);
		q.setString("name", name);
		User u = (User) q.uniqueResult();

		System.err.printf("** FOUND user ID: %d, NAME: %s%n", u.getId(),
				u.getName());
		return u;
	}
}
