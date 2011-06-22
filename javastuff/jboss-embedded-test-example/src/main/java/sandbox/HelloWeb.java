package sandbox;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.ejb.EJB;
import java.io.IOException;
import java.util.GregorianCalendar;


public class HelloWeb extends HttpServlet {

	protected static final String LOGNAME = HelloWeb.class.getName();

	@EJB
	protected HelloWorldLocal helloWorldLocal;


	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("helloLocal", helloWorldLocal.helloLocal());
		request.getRequestDispatcher("ShowResult").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
						  HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}