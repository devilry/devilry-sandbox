package sandbox;

import java.util.TreeSet;


class DefaultUserDb implements UserDb {

	private TreeSet<String> all = new TreeSet<String>();

	public boolean hasUserForReal(String name) {
		return all.contains(name);
	}

	public void addUserForReal(String name) {
		System.out.println("Add user: " + name);
		all.add(name);
	}
}
