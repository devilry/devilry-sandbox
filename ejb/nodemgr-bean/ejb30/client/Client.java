package ejb30.client;

import ejb30.session.*;
import ejb30.entity.Node;
import ejb30.entity.CourseNode;

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
						mgr.addNode("uio");
						mgr.addNode("matnat", mgr.findNode("uio"));
						mgr.addNode("ifi", mgr.findNode("matnat", "uio"));
						mgr.addCourseNode("INF1000", "Grunnkurs i objektorientert programmering",
								mgr.findNode("ifi", "matnat"));
						mgr.addCourseNode("INF1010", "Objektorientert programmering",
								mgr.findNode("ifi", "matnat"));
					}

					for(Object o : mgr.findByPath("uio.matnat.ifi").getChildren()) {
						if(o instanceof CourseNode) {
							CourseNode cn = (CourseNode) o;
							System.out.println("Code: " + cn.getCourseCode() + ", Name: " + cn.getCourseName());
						}
					}
                } catch(Exception e) {
					e.printStackTrace();
                    System.exit(-1);
                }

                pl.logout();

    }
}

