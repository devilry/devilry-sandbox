
package ejb30.session;

import java.util.*; 
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;
import javax.annotation.security.DenyAll;
import javax.annotation.Resource;
import java.security.Principal;

import ejb30.entity.Node;
import ejb30.entity.CourseNode;

@Stateless(mappedName = "NodeMgrImplRemote")
@RolesAllowed("admin")
public class NodeMgrImpl implements NodeMgrRemote {
	@PersistenceContext(unitName="NodeService")
	private EntityManager em;

	@Resource
	private SessionContext ctx;

	private Node root;

	public Node addNode(String name) {
		return addNode(name, null);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Node addNode(String name, Node parent) {
		Node node = new Node();
		node.setNodeName(name);
		node.setParent(parent);

		if(node.getParent() == null) {
			setRoot(node);
			em.persist(node);
		} else {
			parent.addChild(node);
			em.merge(parent);
		}

		return node;
	}
	
	public Node addCourseNode(String code, String name) {
		return addCourseNode(code, name, null);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Node addCourseNode(String code, String name, Node parent) {
		CourseNode node = new CourseNode();
		node.setNodeName(code.toLowerCase());
		node.setCourseCode(code);
		node.setCourseName(name);
		node.setParent(parent);

		if(node.getParent() == null) {
			// not sure if we should allow courses with no parent..
			setRoot(node);
			em.persist(node);
		} else {
			parent.addChild(node);
			em.merge(parent);
		}

		return node;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Node update(Node node) {
		return em.merge(node);
	}

	@PermitAll
	public Node findNode(String name) {
		return findNode(name, null);
	}

	@PermitAll
	public Node findNode(String name, String parent) {
		Query q;

		if(parent == null) {
			q = em.createQuery("SELECT n from Node n WHERE n.nodeName=:name AND n.parent IS NULL");
			q.setParameter("name", name);
		} else {
			q = em.createQuery("SELECT n from Node n WHERE n.nodeName=:name AND n.parent IS NOT NULL AND n.parent.nodeName=:parent");
			q.setParameter("name", name);
			q.setParameter("parent", parent);
		}

		Node n;

		try {
			n = (Node) q.getSingleResult();
		} catch(javax.persistence.NoResultException e) {
			n = null;
		}

		return n;
	}

	@PermitAll
	public Node findByPath(String path) {
		String[] s = path.split("\\.");
		int n1 = s.length-1;
		int n2 = s.length-2;

		return findNode(s[n1], s[n2]);
	}

	@PermitAll
	public Node getRoot() {
		return this.root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
}

