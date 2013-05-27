package de.jbdiah.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.jbdiah.util.AuthUtil;
import de.jbdiah.util.Constants;

public class AuthFilter implements Filter {

	protected static Logger logger = Logger.getLogger(AuthFilter.class);

	String serverSecret;

	public AuthFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*
		 * JEE Filter#doFilter() comes only with ServletRequest,
		 * ServletResponse, not their HttpServletX counterparts; cast to
		 * HttpServletRequest necessary in order to be able to use
		 * getContextPath(), getCookies() etc. cast to HttpServletResponse
		 * necessary in order to be able to use sendRedirect()
		 * 
		 * some methods are missing, namely HttpServletRequest.getPathInfo();
		 * fortunately, we can simulate this via ServletRequest.getServletPath
		 * with the context part removed
		 */

		if (request instanceof HttpServletRequest) {

			HttpServletRequest httprequest = (HttpServletRequest) request;
			Cookie[] cookies = ((HttpServletRequest) request).getCookies();

			boolean userAuthenticated = false;

			// check for cookies
			String authCookieValue = null;
			String login = "";

			if (cookies != null) {
				for (Cookie cookie : cookies) {

					if (Constants.COOKIE_USERNAME.equals(cookie.getName())) {
						login = cookie.getValue();
					}

					if (Constants.COOKIE_AUTH_TOKEN.equals(cookie.getName())) {
						authCookieValue = cookie.getValue();
					}
				}

				if (AuthUtil.checkAuthTokens(login, serverSecret,
						authCookieValue) == true) {
					userAuthenticated = true;
				}
			}

			if (userAuthenticated == true) {
				chain.doFilter(request, response);
			} else { // userAuthenticated == false

				String remoteHost = request.getRemoteHost();
				// this is a hack in order for cookies to work in test env
				// (which switches between 127.0.0.1 and localhost
				// inexplicably)
				if ("127.0.0.1".equals(remoteHost)) {
					remoteHost = "localhost";
				}

				String appLocation = request.getScheme() + "://" + remoteHost
						+ ":" + request.getLocalPort()
						+ ((HttpServletRequest) request).getContextPath();
				logger.debug("Not authenticated, redirecting to initial view "
						+ appLocation);

				// HTTP response = 302 Found, redirecting back to initial
				// view
				// via HTTP Location header
				((HttpServletResponse) response)
						.setStatus(HttpServletResponse.SC_FOUND);
				((HttpServletResponse) response).setHeader("Location",
						appLocation);
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// get SERVER_SECRET from web.xml
		serverSecret = fConfig.getInitParameter(Constants.SERVER_SECRET);
	}

}
