
package org.devilry.session;

import javax.ejb.*;
import javax.persistence.*;
import org.devilry.entity.*;

@Stateless
public class TreeManagerImpl implements TreeManagerRemote {
	@PersistenceContext(unitName="TreeService")
	private EntityManager em;

	public long addNode(String name, String displayName) {
		return addNode(name, displayName, -1);
	}

	public long addNode(String name, String displayName, long parentId) {
		Node node = new Node();
		node.setName(name);
		node.setDisplayName(displayName);

		if(parentId != -1) {
			Node pn = getNode(parentId);
			node.setParent(pn);
		}

		em.persist(node);

		return node.getId();
	}

	public long getNodeIdFromPath(String path) {
		String[] sp = path.split("\\.");

		if(sp.length == 1) {
			return getNodeId(sp[0], -1);
		} else if(sp.length > 1) {
			int length = 0;
			long id = 0;

			while(length < sp.length-1) {
				id = getNodeId(sp[length+1], sp[length]);
				length++;

				if(id == -1)
					return id;
			}

			return id;
		}

		return -1;
	}

	private Node getNode(long nodeId) {
		Query q = em.createQuery("SELECT n FROM Node n WHERE n.id=:nodeId");
		q.setParameter("nodeId", nodeId);

		Node node;

		try {
			node = (Node) q.getSingleResult();
		} catch(NoResultException e) {
			node = null;
		}

		return node;
	}

	private long getNodeId(String name) {
		return getNodeId(name, -1);
	}

	private long getNodeId(String name, long parentId) {
		Query q;
		
		if(parentId != -1) {
			q = em.createQuery("SELECT n FROM Node n WHERE n.name=:name AND n.parent IS NOT NULL AND n.parent.id=:parentId");
			q.setParameter("name", name);
			q.setParameter("parentId", parentId);
		} else {
			q = em.createQuery("SELECT n FROM Node n WHERE n.name=:name AND n.parent IS NULL");
			q.setParameter("name", name);
		}

		Node node;

		try {
			node = (Node) q.getSingleResult();
		} catch(NoResultException e) {
			node = null;
		}

		return node==null?-1:node.getId();
	}

	private long getNodeId(String name, String parent) {
		Query q;

		if(parent != null) {
			q = em.createQuery("SELECT n FROM Node n WHERE n.name=:name AND n.parent IS NOT NULL AND n.parent.name=:parent");
			q.setParameter("name", name);
			q.setParameter("parent", parent);
		} else {
			q = em.createQuery("SELECT n FROM Node n WHERE n.name=:name AND n.parent IS NULL");
			q.setParameter("name", name);
		}

		Node node;

		try {
			node = (Node) q.getSingleResult();
		} catch(NoResultException e) {
			node = null;
		}

		return node==null?-1:node.getId();
	}
}

