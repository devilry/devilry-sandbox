
package ejb30.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import java.util.Collection;

@Entity
@DiscriminatorValue("CN")
public class CourseNode extends Node {
	private String courseName;
	private String courseCode;

	public CourseNode() { }

	public void setCourseName(String name) {
		this.courseName = name;
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseCode(String code) {
		this.courseCode = code;
	}

	public String getCourseCode() {
		return this.courseCode;
	}
}

