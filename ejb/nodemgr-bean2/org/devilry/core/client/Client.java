package org.devilry.core.client;

import java.util.*;
import javax.naming.InitialContext;

import org.devilry.core.session.*;

public class Client {
    public static void main(String args[]) throws Exception {
		try {
			Properties p = new Properties();
			p.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			p.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext ctx = new InitialContext(p);
			TreeManagerRemote mgr = (TreeManagerRemote) ctx.lookup("org.devilry.core.session.TreeManagerRemote");

			if(mgr.getNodeIdFromPath("uio") == -1) {
				mgr.addNode("uio", "Universitetet i Oslo");
				mgr.addNode("matnat", "Det matematisk-naturvitenskapelige fakultet", 
						mgr.getNodeIdFromPath("uio"));
				mgr.addNode("ifi", "Institutt for informatikk", 
						mgr.getNodeIdFromPath("uio.matnat"));

				mgr.addCourseNode("INF1000", "Grunnkurs i objektorientert programmering",
						mgr.getNodeIdFromPath("uio.matnat.ifi"));
				mgr.addCourseNode("INF1010", "Objektorientert programmering",
						mgr.getNodeIdFromPath("uio.matnat.ifi"));

				Calendar start = new GregorianCalendar(2009, 00, 01);
				Calendar end = new GregorianCalendar(2009, 05, 15);

				mgr.addPeriodNode("fall09", "Fall 2009", start.getTime(), end.getTime(), 
						mgr.getNodeIdFromPath("uio.matnat.ifi.inf1010"));

				start = new GregorianCalendar(2009, 07, 01);
				end = new GregorianCalendar(2009, 11, 20);

				mgr.addPeriodNode("autumn09", "Autumn 2009", start.getTime(), end.getTime(), 
						mgr.getNodeIdFromPath("uio.matnat.ifi.inf1010"));

			}

			System.out.println(mgr.getNodeIdFromPath("uio.matnat.ifi"));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }
}

