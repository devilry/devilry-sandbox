package ejb30.client;

import ejb30.session.*;
import ejb30.entity.Node;
import ejb30.entity.CourseNode;
import ejb30.entity.PeriodNode;

import java.util.Properties;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
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
			NodeMgrRemote mgr = (NodeMgrRemote) ctx.lookup("ejb30.session.NodeMgrRemote");

			if(mgr.findNode("uio") == null) {
				mgr.addNode("uio", "Universitetet i Oslo");
				mgr.addNode("matnat", "Det matematisk-naturvitenskapelige fakultet", 
						mgr.findNode("uio"));
				mgr.addNode("math", "Matematisk institutt",
						mgr.findNode("matnat", "uio"));
				mgr.addCourseNode("MAT1001", "Matematikk 1", mgr.findNode("math", "matnat"));
				mgr.addCourseNode("MAT1010", "Matematikk i praksis II", mgr.findNode("math", "matnat"));

				mgr.addNode("ifi", "Institutt for informatikk",
						mgr.findNode("matnat", "uio"));
				mgr.addCourseNode("INF1000", "Grunnkurs i objektorientert programmering",
						mgr.findNode("ifi", "matnat"));
				mgr.addCourseNode("INF1010", "Objektorientert programmering",
						mgr.findNode("ifi", "matnat"));


				Calendar start = new GregorianCalendar(2009, 00, 01);
				Calendar end = new GregorianCalendar(2009, 05, 15);

				mgr.addPeriodNode("fall09", "Fall 2009", start.getTime(), end.getTime(), 
						(CourseNode) mgr.findByPath("uio.matnat.ifi.inf1000"));

				start = new GregorianCalendar(2009, 07, 01);
				end = new GregorianCalendar(2009, 11, 20);

				mgr.addPeriodNode("autumn09", "Autumn 2009", start.getTime(), end.getTime(), 
						(CourseNode) mgr.findByPath("uio.matnat.ifi.inf1000"));
			}

			/*
			for(Object o : mgr.findByPath("uio.matnat.ifi").getChildren()) {
				if(o instanceof CourseNode) {
					CourseNode cn = (CourseNode) o;
					System.out.println("Code: " + cn.getCourseCode() + ", Name: " + cn.getDisplayName() +
							" [Path: " + cn.getPath() + "]");
				}
			} */

			printCourses(mgr.getRoot());
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		pl.logout();
    }

	public static void printCourses(Node node) {
		for(Node n : node.getChildren()) {
			if(n instanceof CourseNode) {
				CourseNode cn = (CourseNode) n;
				System.out.println("Code: " + cn.getCourseCode() + ", Name: " + cn.getDisplayName() +
						" [Path: " + cn.getPath() + "]");
				printPeriods(cn);
			}

			printCourses(n);
		}
	}

	public static void printPeriods(CourseNode node) {
		for(Node n : node.getChildren()) {
			if(n instanceof PeriodNode) {
				PeriodNode pn = (PeriodNode) n;
				System.out.println("==> Period: " + pn.getDisplayName() +
						" [" + pn.getStartDate() + ", " + pn.getEndDate() +
						"]");
			}
		}
	}
}

