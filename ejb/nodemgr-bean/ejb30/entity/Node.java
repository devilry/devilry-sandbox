
package ejb30.entity;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.Collection;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="nodeType")
@DiscriminatorValue("N")
public class Node implements java.io.Serializable {
	@Id
	@Column(nullable=false)
	private String nodeName;
	
	@Id
	@Column(nullable=true)
	private Node parent;

	private String displayName;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Collection<Node> children; 

	public Node() { }

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String name) {
		this.nodeName = name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String name) {
		this.displayName = name;
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
		return getParent()!=null?getParent().getPath() + "." + getNodeName():getNodeName();
	}
}

