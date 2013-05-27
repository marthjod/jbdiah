<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="de.jbdiah.util.Constants" %>

<div>
<form action="<%= (request.getContextPath() + Constants.INITIAL_VIEW) %>" 
	method="post">
	<input type="submit" name="logout" value="Logout" class="button" />
</form>
</div>