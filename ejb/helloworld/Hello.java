package org.acme;
import javax.ejb.Remote;

@Remote
public interface Hello{
	public String sayHello();
}

