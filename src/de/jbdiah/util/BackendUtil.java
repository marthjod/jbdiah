package de.jbdiah.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import de.jbdiah.model.BackendUser;

public class BackendUtil {

	protected static Logger logger = Logger.getLogger(BackendUtil.class);

	public static ArrayList<BackendUser> readUserdataFromXML(
			String xmlfilePath) {

		ArrayList<BackendUser> users = new ArrayList<BackendUser>();

		XStream xstream = new XStream();
		ObjectInputStream usersXMLFile = null;
		try {
			usersXMLFile = xstream.createObjectInputStream(new BufferedReader(
					new FileReader(xmlfilePath)));

			boolean moreUsers = true;
			while (moreUsers == true) {
				try {
					BackendUser u = (BackendUser) usersXMLFile.readObject();
					logger.info("[" + xmlfilePath + "] Adding "
							+ u.getUsername());

					// add to list of available users
					users.add(u);

				} catch (ClassNotFoundException cnfe) {
					logger.warn(cnfe.getMessage());
				} catch (java.io.EOFException eofe) {
					logger.debug("No more data to be read (EOF)");
					moreUsers = false;
				}
			}

		} catch (FileNotFoundException e) {
			logger.error("File not found: " + xmlfilePath);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (usersXMLFile != null) {
				try {
					logger.info("[" + xmlfilePath + "] Closing file");
					usersXMLFile.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

		return users;
	}
}
