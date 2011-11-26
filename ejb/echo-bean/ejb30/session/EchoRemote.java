package ejb30.session;

import javax.ejb.Remote;

@Remote
public interface EchoRemote {
    public String echo(String msg);
    public String echo2(String msg);
}

