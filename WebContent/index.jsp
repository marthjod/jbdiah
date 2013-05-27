<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="de.jbdiah.*,org.apache.log4j.Logger,javax.servlet.http.Cookie" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="header.jsp" %>
<title>Login</title>
</head>
<body>

	<% 
	
	boolean logout = false;
	
	// this does not work with method="post" if action="<contextpath>" only
	// that is b/c <contextpath> gets directed to <contextpath>/index.jsp,
	// whilst losing the POST data!
	// this does not happen with GET (sticky url params), but GET is impractical for page refresh
	// solution: make form action access "<contextpath>/<login_view>" directly
	
	if(request.getParameter("logout") != null) { 
		
		logout = true;
	
		// clear cookies
		// TODO: only clear our own cookies, though!
		// but request.getCookies() should return only our own cookies, anyway...?
		
		Cookie[] requestCookies = request.getCookies();
		
		for (Cookie requestCookie : requestCookies) {
			// make cookie expire immediately
			requestCookie.setMaxAge(0);
			// add to response
			response.addCookie(requestCookie);
		}
	
	%>
	
		<div>
			You have been logged out.
		</div>
		
		<% }  %>
	
		<form action="<%out.print(request.getContextPath() + Constants.LOGIN_CONTROLLER);%>" method="post">
			<div>
				<input type="text" name="email" required="yes" placeholder="Username" />
			</div>
			<div>
				<input type="password" name="password" required="yes" placeholder="Password" />
			</div>
			<div>
				<input type="submit" value="Log in" class="button" />
			</div>
		</form>

</body>
</html>