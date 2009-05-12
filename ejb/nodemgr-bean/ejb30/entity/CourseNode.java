
package ejb30.entity;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import java.util.Collection;

@Entity
@DiscriminatorValue("CN")
public class CourseNode extends Node {
	@Column(nullable=false)
	private String courseCode;

	public CourseNode() { }

	public void setCourseCode(String code) {
		this.courseCode = code;
	}

	public String getCourseCode() {
		return this.courseCode;
	}
}

