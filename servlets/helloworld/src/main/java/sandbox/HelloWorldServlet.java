package sandbox;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class HelloWorldServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Hello world";

		out.println("<html>");
		out.println("	<head>");
		out.println("		<title>" + title + "</title>");
		out.println("	</head>");
		out.println("	<body>");
		out.println("		<h1>" + title + "</h1>");
		out.println("	</body>");
		out.println("</html>");
	}
}
