package org.devilry;

import javax.ejb.Remote;

@Remote
public interface Hello {
	public String sayHello();
}