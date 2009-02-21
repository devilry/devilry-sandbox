package sandbox;


public class DefaultUserSystem implements UserSystem {

	private UserDb userdb;

	public void setUserDb(UserDb db) {
		userdb = db;
	}


	public boolean hasUser(String name) {
		return userdb.hasUserForReal(name);
	}

	public void addUser(String name) {
		userdb.addUserForReal(name);	
	}
}
