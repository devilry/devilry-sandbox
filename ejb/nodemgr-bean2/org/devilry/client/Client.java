package org.devilry.client;

import java.util.Properties;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import javax.naming.InitialContext;

import org.devilry.session.*;
//import org.devilry.entity.*;

public class Client {
    public static void main(String args[]) throws Exception {
		try {
			Properties p = new Properties();
			p.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			p.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext ctx = new InitialContext(p);
			TreeManagerRemote mgr = (TreeManagerRemote) ctx.lookup("org.devilry.session.TreeManagerRemote");

			mgr.addNode("uio", "Universitetet i Oslo");
			mgr.addNode("matnat", "Det matematisk-naturvitenskapelige fakultet", mgr.getNodeIdFromPath("uio"));
			mgr.addNode("ifi", "Institutt for informatikk", mgr.getNodeIdFromPath("uio.matnat"));

			System.out.println(mgr.getNodeIdFromPath("uio.matnat.ifi"));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }
}

