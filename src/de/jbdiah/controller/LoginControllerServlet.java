package de.jbdiah.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.jbdiah.model.BackendUser;
import de.jbdiah.util.AuthUtil;
import de.jbdiah.util.Constants;


public class LoginControllerServlet extends HttpServlet {

	protected static Logger logger = Logger
			.getLogger(LoginControllerServlet.class);

	private static final long serialVersionUID = 1L;

	public LoginControllerServlet() {
		super();
	}
	
	 public void init(ServletConfig config) throws ServletException {
		    super.init(config);		 
		  }

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.warn("doGet() called for login, ignoring");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String appPath = getServletConfig().getInitParameter(Constants.DATA_PATH);
		ArrayList<BackendUser> users = BackendUser.getAvailable(appPath);
		
		String login = request.getParameter("email");
		String password = request.getParameter("password");
		String appRoot = request.getContextPath();

		boolean userFound = false, passwordCorrect = false;

		for (BackendUser user : users) {
			if (user.getUsername().equals(login)) {
				userFound = true;
				if (user.passwordCorrect(password)) {
					passwordCorrect = true;
				}
				// we found the user, no need to check any further
				break;
			}
		}

		if (userFound == true && passwordCorrect == true) {
			logger.debug("Login successful for \"" + login + "\"");

// 			necessary? 2013-03-01
//			logger.info("Adding users to servlet context");
//			BackendUtil.setUsersToServletContext(servletContext, users);
			
			// get SERVER_SECRET from web.xml
			String serverSecret = getServletConfig().getInitParameter(
					"SERVER_SECRET");

			String authToken = AuthUtil.genAuthToken(login, serverSecret);

			// if generating the auth token failed, don't set any cookies
			if (authToken.length() > 0) {
				// username cookie, so we don't have to send the username via url 
				Cookie usernameCookie = new Cookie(Constants.COOKIE_USERNAME, login);
				Cookie authCookie = new Cookie(Constants.COOKIE_AUTH_TOKEN,
						authToken);

				response.addCookie(usernameCookie);
				response.addCookie(authCookie);
			}

			String nextView = response.encodeRedirectURL(appRoot
					+ Constants.SHOW_VIEW_PATH);
			response.sendRedirect(nextView);

		} else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			if (userFound == false || passwordCorrect == false) {
				out.println("Access denied. <a href=\"" + appRoot
						+ "\">Retry</a>.");
			} else {
				logger.warn("Something unexpected happened!");
			}
		}
	}
}
