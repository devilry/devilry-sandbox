
package ejb30.session;

import java.util.*; 
import javax.ejb.Stateful;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;

@Stateful(mappedName = "EchoImplRemote")
public class EchoImpl implements EchoRemote {

    @RolesAllowed("admin")
    public String echo(String msg) {
        return msg;
    }

    @RolesAllowed("manager")
    public String echo2(String msg) {
        return msg;
    }
} 

