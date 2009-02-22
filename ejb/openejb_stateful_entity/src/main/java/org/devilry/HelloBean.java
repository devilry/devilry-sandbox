package org.devilry;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class HelloBean implements Hello {
    @PersistenceContext(unitName = "hello-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

	@Override
	public void addHelloworld(String lang, String helloworld) {
		entityManager.persist(new HelloTranslations(lang, helloworld));
	}

	@Override
	public String getHelloworld(String lang) {
		return entityManager.find(HelloTranslations.class, lang).getHelloworld();
	}
}
