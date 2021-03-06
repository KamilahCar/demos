package com.revature.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// We must extend some Servlet implementation in order to interact with HTTP Traffic
public class AuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// This only works when you have a GET request with URL Query Parameters
		// Or if your request has a body, which is of Content-Type application/x-www-form-urlencoded
		String username = req.getParameter("someReallyCrazyLongVariableName");
		String password = req.getParameter("password");
		
		if ("william".equals(username) && "password".equals(password)) {
			// Session, cookies, hidden form field => worst practice
			// On a cookie, you specify a name, a value, and path (At least). Cookies are stored on the browser,
			// and every time the browser send an HTTP request to that path, the cookie will be included
			req.getSession().setAttribute("currentUser", "william");
			req.getSession().setAttribute("age", 26);
			
			// At this point in time, what are you trying to do? 
			// Go to the home page!
			// This is not used in REST API's. Only when using Server Side Rendering (In our case, JSP).
			req.getRequestDispatcher("/home.jsp").include(req, resp);
			return;
		} else {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
	}
}
