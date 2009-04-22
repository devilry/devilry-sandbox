
package ejb30.entity;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.Collection;

@Entity
public class Node implements java.io.Serializable {
	@Id
	@Column(nullable=false)
	private String name;
	
	@Id
	private Node parent;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Collection<Node> children; 

	public Node() { }

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Collection<Node> getChildren() {
		if(this.children == null)
			this.children = new java.util.ArrayList<Node>();

		return children;
	}

	public void setChildren(Collection<Node> children) {
		this.children = children;
	}

	public void addChild(Node child) {
		getChildren().add(child);
	}

	public void removeChild(Node child) {
		getChildren().remove(child);
	}

	public String getPath() {
		return getParent()!=null?getParent().getPath() + "." + getName():getName();
	}
}

