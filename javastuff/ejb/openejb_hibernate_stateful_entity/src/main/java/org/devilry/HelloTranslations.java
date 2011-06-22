package org.devilry;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HelloTranslations {

	@Id
	private String lang;

	/** Localized "Hello World". */
	private String helloworld;

	protected HelloTranslations() {
	}

	public HelloTranslations(String lang, String helloworld) {
		this.lang = lang;
		this.helloworld = helloworld;
	}

	public String getHelloworld() {
		return helloworld;
	}
}
