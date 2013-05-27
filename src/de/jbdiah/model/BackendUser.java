package de.jbdiah.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.jbdiah.util.AuthUtil;
import de.jbdiah.util.BackendUtil;
import de.jbdiah.util.Constants;


public class BackendUser extends User {

	protected static Logger logger = Logger
			.getLogger(BackendUser.class);
	
	private String passwordHash;
	private static ArrayList<BackendUser> availableUsers = new ArrayList<BackendUser>();

	// unsafe; use only for test.CreateUsers
	public BackendUser(String username, String realname, String password) {
		setUsername(username);
		setRealname(realname);
		setPasswordHash(password);
	}

	// unsafe; use only for test.CreateUsers
	public BackendUser(String username, String password) {
		setUsername(username);
		setPasswordHash(password);
	}

	public BackendUser(String username) {
		setUsername(username);
	}

	public BackendUser() {
		this("unknown username");
	}

	private String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String password) {

		this.passwordHash = AuthUtil.sha1Hash(password);
	}

	public boolean passwordCorrect(String password) {

		return AuthUtil.compareHashes(AuthUtil.sha1Hash(password).getBytes(),
				this.getPasswordHash().getBytes());
	}

	public static final ArrayList<BackendUser> getAvailable(String appPath) {
		availableUsers = BackendUtil.readUserdataFromXML(appPath + '/' + Constants.USERDATA_FILE);
		return availableUsers;
	}
	
	public static final void setAvailable(ArrayList<BackendUser> users) {
		availableUsers = users;
	}
}
