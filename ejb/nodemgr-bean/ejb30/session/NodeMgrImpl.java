
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
import ejb30.entity.PeriodNode;

@Stateless
@RolesAllowed("admin")
public class NodeMgrImpl implements NodeMgrRemote {
	@PersistenceContext(unitName="NodeService")
	private EntityManager em;

	@Resource
	private SessionContext ctx;

	private String rootPath;

	public Node addNode(String name) {
		return addNode(name, null, null);
	}

	public Node addNode(String name, String display) {
		return addNode(name, display, null);
	}

	public Node addNode(String name, Node parent) {
		return addNode(name, null, parent);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Node addNode(String name, String display, Node parent) {
		Node node = new Node();
		node.setNodeName(name);
		node.setParent(parent);

		if(display == null)
			node.setDisplayName(name);
		else
			node.setDisplayName(display);

		if(node.getParent() == null) {
			setRoot(node.getNodeName());
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
		node.setDisplayName(name);
		node.setParent(parent);

		if(node.getParent() == null) {
			// not sure if we should allow courses with no parent..
			setRoot(node.getNodeName());
			em.persist(node);
		} else {
			parent.addChild(node);
			em.merge(parent);
		}

		return node;
	}

	public Node addPeriodNode(String name, Date start, Date end, CourseNode parent) {
		return addPeriodNode(name, null, start, end, parent);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Node addPeriodNode(String name, String display, Date start, Date end, CourseNode parent) {
		PeriodNode node = new PeriodNode();
		node.setNodeName(name);
		node.setStartDate(start);
		node.setEndDate(end);
		
		if(display == null)
			node.setDisplayName(name);
		else
			node.setDisplayName(display);

		parent.addChild(node);
		em.merge(parent);

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
			q = em.createQuery("SELECT n FROM Node n WHERE n.nodeName=:name AND n.parent IS NULL");
			q.setParameter("name", name);
		} else {
			q = em.createQuery("SELECT n FROM Node n WHERE n.nodeName=:name AND n.parent IS NOT NULL AND n.parent.nodeName=:parent");
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
		Query q = em.createQuery("SELECT n FROM Node n WHERE n.nodeName=:name AND n.parent IS NULL");
		q.setParameter("name", this.rootPath);

		return (Node) q.getSingleResult();
	}

	public void setRoot(String path) {
		this.rootPath = path;
	}
}

