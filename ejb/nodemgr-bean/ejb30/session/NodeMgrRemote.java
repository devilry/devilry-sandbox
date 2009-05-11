package ejb30.session;

import javax.ejb.Remote;
import ejb30.entity.Node;
import java.util.Collection;

@Remote
public interface NodeMgrRemote {
    public Node addNode(String name);
    public Node addNode(String name, Node parent);
	public Node addCourseNode(String code, String name);
	public Node addCourseNode(String code, String name, Node parent);
	public Node update(Node node);
	public Node findNode(String name);
	public Node findNode(String name, String parent);
	public Node findByPath(String path);
	public void setRoot(Node node);
	public Node getRoot();
}

