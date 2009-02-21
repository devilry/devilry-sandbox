package sandbox;

import java.util.Collection;

import javax.persistence.*;


@Entity
//@Table(name = "USERS")  // column and table names are not required
class User {
	@Id // primary key
	@GeneratedValue // autogenerate value
	//@Column(name = "id")   // column and table names are not required
	private Long id;

	//@Column(name = "name")    // column and table names are not required
	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	private User bestFriend;

	@ManyToMany
	private Collection<User> allFriends;

	// Required by jpa
	private User(){}


	//
	// Plain java (POJO)
	//

	public User(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAllFriends(Collection<User> allFriends) {
		this.allFriends = allFriends;
	}

	public Collection<User> getAllFriends() {
		return allFriends;
	}
	
	public void setBestFriend(User bestFriend) {
		this.bestFriend = bestFriend;
	}

	public User getBestFriend() {
		return bestFriend;
	}

	public String toString() {
		return String.format("[%d, %s, %s]", id, name, bestFriend.getName());
	}
}