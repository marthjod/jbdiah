package de.jbdiah.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import de.jbdiah.model.BackendUser;
import de.jbdiah.util.Constants;

public class CreateUsers {

	protected static Logger logger = Logger.getLogger(CreateUsers.class);

	public static void main(String[] args) {

		XStream xstream = new XStream();
		// xstream.alias("user", BackendUser.class);

		// users
		try {
			ObjectOutputStream xmlfile = xstream
					.createObjectOutputStream(new FileWriter("/tmp/jbdiah/" + Constants.USERDATA_FILE));	
			BackendUser newUser = new BackendUser("newUser", "newUser", "insert-5up3r53cr3tP855w0rD-here");
			xmlfile.writeObject(newUser);
			xmlfile.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
