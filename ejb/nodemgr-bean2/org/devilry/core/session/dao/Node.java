package org.devilry.core.session.dao;

import javax.ejb.Stateful;
import javax.persistence.*;

@Stateful
public class Node implements NodeRemote {
	@PersistenceContext(unitName="TreeService")
	protected EntityManager em;

	protected org.devilry.core.entity.Node node;

	public boolean init(long nodeId) {
		Query q = em.createQuery("SELECT n FROM Node n WHERE n.id = :nodeId");
		q.setParameter("nodeId", nodeId);

		try {
			node = (org.devilry.core.entity.Node) q.getSingleResult();
		} catch(NoResultException e) {
			node = null;
		}

		return node==null?false:true;
	}

	public void setId(long nodeId) {
		node.setId(nodeId);
		em.merge(node);
	}

	public long getId() {
		return node.getId();
	}

	public void setName(String name) {
		node.setName(name);
		em.merge(node);
	}

	public String getName() {
		return node.getName();
	}

	public void setDisplayName(String name) {
		node.setDisplayName(name);
		em.merge(node);
	}

	public String getDisplayName() {
		return node.getDisplayName();
	}
}

