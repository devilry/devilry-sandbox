package ejb30.client;

import ejb30.session.*;
import ejb30.entity.Node;

import java.util.Properties;
import javax.naming.InitialContext;
import com.sun.appserv.security.ProgrammaticLogin;

public class Client {
    public static void main(String args[]) throws Exception {
                ProgrammaticLogin pl = new ProgrammaticLogin();
                pl.login("morteoh", "morteoh");

                try {
                    Properties p = new Properties();
                    p.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
                    p.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

                    InitialContext ctx = new InitialContext(p);
                    NodeMgrRemote mgr = (NodeMgrRemote) ctx.lookup("NodeMgrImplRemote");

					if(mgr.findNode("uio") == null) {
						mgr.add("uio");
						mgr.add("matnat", mgr.findNode("uio"));
						mgr.add("ifi", mgr.findNode("matnat", "uio"));
						mgr.add("inf1000", mgr.findNode("ifi", "matnat"));
						mgr.add("inf1010", mgr.findNode("ifi", "matnat"));
					}

					System.out.println(mgr.findByPath("uio.matnat.ifi").getChildren().size());
                } catch(Exception e) {
					e.printStackTrace();
                    System.exit(-1);
                }

                pl.logout();

    }
}

