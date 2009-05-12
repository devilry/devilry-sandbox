package ejb30.session;

import javax.ejb.Remote;
import ejb30.entity.Node;
import ejb30.entity.CourseNode;
import java.util.Date;

@Remote
public interface NodeMgrRemote {
    public Node addNode(String name);
    public Node addNode(String name, String display);
    public Node addNode(String name, Node parent);
    public Node addNode(String name, String display, Node parent);
	public Node addCourseNode(String code, String name);
	public Node addCourseNode(String code, String name, Node parent);
	public Node addPeriodNode(String name, Date start, Date end, CourseNode parent);
	public Node addPeriodNode(String name, String display, Date start, Date end, CourseNode parent);
	public Node update(Node node);
	public Node findNode(String name);
	public Node findNode(String name, String parent);
	public Node findByPath(String path);
	public void setRoot(String path);
	public Node getRoot();
}

