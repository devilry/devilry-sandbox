package org.devilry;

import javax.ejb.Remote;

@Remote
public interface Hello2 {
	public void addHelloworld(String lang, String helloworld);
	public String getHelloworld(String lang);
}