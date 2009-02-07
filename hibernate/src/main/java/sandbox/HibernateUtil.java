package sandbox;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil
{
	private static SessionFactory sessionFactory;
	private static Session session;
	
	static
	{
		try {
			Configuration configuration = new Configuration();
			configuration.configure(); // Loads hibernate.cfg.xml from classpath
			sessionFactory = configuration.buildSessionFactory();
			session = sessionFactory.openSession();
		} catch(Throwable e) {
			System.err.println("Initial SessionFactory creation failed." + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static Session getCurrentSession()
	{
		return session;
	}
}
