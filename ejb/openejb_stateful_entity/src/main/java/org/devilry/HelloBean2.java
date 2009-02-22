package org.devilry;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


@Stateful
public class HelloBean2 implements Hello2 {
    @PersistenceContext(unitName = "hello-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

	//@TransactionAttribute(TransactionAttributeType.NEVER)
	public void addHelloworld(String lang, String helloworld) {
		entityManager.persist(new HelloTranslations(lang, helloworld));
	}


	//@TransactionAttribute(TransactionAttributeType.NEVER)
	public String getHelloworld(String lang) {
		return entityManager.find(HelloTranslations.class, lang).getHelloworld();
	}
}
