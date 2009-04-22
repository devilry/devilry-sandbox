package ejb30.session;

import javax.ejb.Remote;
import ejb30.entity.Node;
import java.util.Collection;

@Remote
public interface NodeMgrRemote {
    public Node add(String name);
    public Node add(String name, Node parent);
	public Node update(Node node);
	public Node findNode(String name);
	public Node findNode(String name, String parent);
	public Node findByPath(String path);
}

