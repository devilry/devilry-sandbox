package org.devilry.core.client;

import java.util.*;
import javax.naming.InitialContext;

import org.devilry.core.session.*;
import org.devilry.core.session.dao.*;

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

				mgr.addCourseNode("inf1000", "INF1000", "Grunnkurs i objektorientert programmering",
						mgr.getNodeIdFromPath("uio.matnat.ifi"));
				mgr.addCourseNode("inf1010", "INF1010", "Objektorientert programmering",
						mgr.getNodeIdFromPath("uio.matnat.ifi"));

				Calendar start = new GregorianCalendar(2009, 00, 01);
				Calendar end = new GregorianCalendar(2009, 05, 15);

				mgr.addPeriodNode("fall09", "Fall 2009", start.getTime(), end.getTime(), 
						mgr.getNodeIdFromPath("uio.matnat.ifi.inf1000"));

				start = new GregorianCalendar(2009, 07, 01);
				end = new GregorianCalendar(2009, 11, 20);

				mgr.addPeriodNode("autumn09", "Autumn 2009", start.getTime(), end.getTime(), 
						mgr.getNodeIdFromPath("uio.matnat.ifi.inf1010"));

			}

			NodeRemote ifi = (NodeRemote) ctx.lookup("org.devilry.core.session.dao.NodeRemote");
			if(ifi.init( mgr.getNodeIdFromPath("uio.matnat.ifi") )) {
				System.out.println( "ifi: " + ifi.getName() );
				System.out.println( "ifi: " + ifi.getDisplayName() );
				System.out.println( "ifi: " + ifi.getPath() );
			}

			CourseNodeRemote inf1000 = (CourseNodeRemote) ctx.lookup("org.devilry.core.session.dao.CourseNodeRemote");
			if(inf1000.init( mgr.getNodeIdFromPath("uio.matnat.ifi.inf1000") )) {
				System.out.println( "inf1000: " + inf1000.getName() );
				System.out.println( "inf1000: " + inf1000.getDisplayName() );
				System.out.println( "inf1000: " + inf1000.getCourseCode() );
				System.out.println( "inf1000: " + inf1000.getPath() );
			}

			PeriodNodeRemote fall09 = (PeriodNodeRemote) ctx.lookup("org.devilry.core.session.dao.PeriodNodeRemote");
			if(fall09.init( mgr.getNodeIdFromPath("uio.matnat.ifi.inf1000.fall09") )) {
				System.out.println( "fall09: " + fall09.getName() );
				System.out.println( "fall09: " + fall09.getDisplayName() );
				System.out.println( "fall09: " + fall09.getStartDate() );
				System.out.println( "fall09: " + fall09.getEndDate() );
				System.out.println( "fall09: " + fall09.getPath() );

			}

		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }
}

