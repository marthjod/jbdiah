package de.jbdiah.model;

import org.apache.log4j.Logger;

public class User {

	protected static Logger logger = Logger
			.getLogger(User.class);

	private String username;
	private String realname;
	
	public User(String username){
		this.setUsername(username);
	}
	
	public User() {
		this("unknown username");
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return this.realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	

	



	



}
