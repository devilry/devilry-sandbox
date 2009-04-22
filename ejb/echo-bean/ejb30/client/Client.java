package ejb30.client;

import javax.naming.*;
import ejb30.session.*;
import java.util.Properties;
import com.sun.appserv.security.ProgrammaticLogin;

public class Client {
    public static void main(String args[]) throws Exception {
                ProgrammaticLogin pl = new ProgrammaticLogin();
                pl.login("morteoh", "morteoh");

                try {
                    Properties p = new Properties();
                    p.setProperty("org.omg.CORBA.ORBInitialHost", "macfoo");
                    p.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

                    InitialContext ctx = new InitialContext(p);
                    EchoRemote echo = (EchoRemote) ctx.lookup("EchoImplRemote");

                    System.out.println( echo.echo("Hello, World!") );
                    System.out.println( echo.echo2("Hello, World2!") );
                } catch(Exception e) {
                    System.err.println("Got exception... not allowed to access object.");
                    System.exit(-1);
                }

                pl.logout();

    }
}

