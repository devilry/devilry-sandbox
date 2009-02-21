package sandbox;

import javax.ejb.Remote;


@Remote
public interface ConfigRemote {
	public String getSiteName();

	public int getMaxFileSize();
}
