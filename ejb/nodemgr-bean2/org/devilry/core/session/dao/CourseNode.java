package org.devilry.core.session.dao;

import javax.ejb.Stateful;
import javax.persistence.*;

@Stateful
public class CourseNode extends Node implements CourseNodeRemote {
	public void setCourseCode(String courseCode) {
		((org.devilry.core.entity.CourseNode) node).setCourseCode(courseCode);
		em.merge(node);
	}

	public String getCourseCode() {
		return ((org.devilry.core.entity.CourseNode) node).getCourseCode();
	}
}

