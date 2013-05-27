package de.jbdiah.model;

import java.util.ArrayList;


public class AllUsersInContext {

	public ArrayList<BackendUser> users = new ArrayList<BackendUser>();
	
	public AllUsersInContext(ArrayList<BackendUser> users) {
		this.users = users;
	}
	
	public AllUsersInContext() {
	}
}
