package sandbox;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class PlainTextServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException
	{
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("Hello world");
	}
}
