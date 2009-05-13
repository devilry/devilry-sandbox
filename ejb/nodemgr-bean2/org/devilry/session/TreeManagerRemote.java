
package org.devilry.session;

import javax.ejb.*;

@Remote
public interface TreeManagerRemote {
	public long addNode(String name, String displayName);
	public long addNode(String name, String displayName, long parentId);
	public long getNodeIdFromPath(String path);
}

