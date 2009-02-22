package org.devilry;

import javax.ejb.Stateless;

@Stateless
public class HelloBean implements Hello {

	@Override
	public String sayHello() {
		return "Hello world!";
	}

}
