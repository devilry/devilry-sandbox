package org.devilry;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;


@Stateful
public class HelloBean2 implements Hello2 {
    @PersistenceContext
    private EntityManager entityManager;

	public void addHelloworld(String lang, String helloworld) {
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		entityManager.persist(new HelloTranslations(lang, helloworld));
		t.commit();
	}

	public String getHelloworld(String lang) {
		try{
			return entityManager.find(HelloTranslations.class, lang).getHelloworld();
		} catch(Exception e) {
			return null;
		}
	}


	public List<String> getAllTranslations() {
		LinkedList<String> l = new LinkedList<String>();
		Query q = entityManager.createQuery("SELECT t FROM HelloTranslations t");
		List<HelloTranslations> res = q.getResultList();
		for(HelloTranslations t: res) {
			l.add(String.format("%s:%s", t.getLang(), t.getHelloworld()));
		}
		return l;
	}
}
