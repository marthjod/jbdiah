package de.jbdiah.util;

public class Constants {

	// Web constants
	public static final String ROOT_PATH = "/";
	public static final String INITIAL_VIEW = "/index.jsp";
	public static final String LOGIN_CONTROLLER = "/login";
	public static final String JS_PATH = "/js";
	public static final String STATIC_PATH = "/static";
	
	public static final String POST_REQUEST = "/post";
	public static final String POST_BIB = POST_REQUEST + "/bib";
	public static final String POST_MARKDOWN = POST_REQUEST + "/md";
	
	public static final String CONVERT_REQUEST = "/convert";
	public static final String RETRIEVAL_REQUEST = "/retrieve";
	public static final String SHOW_VIEW_PATH = "/incoming";

	public static final String COOKIE_USERNAME = "username";
	public static final String COOKIE_AUTH_TOKEN = "authToken";

	// web.xml init-params
	public static final String SERVER_SECRET = "SERVER_SECRET";
	public static final String DATA_PATH = "DATA_PATH";
	
	public static final String USERDATA_FILE = "users.xml";
	
	public static final String MARKDOWN_FILE = "/var/jbdiah/jbda.md";
	public static final String BIBTEX_FILE = "/var/jbdiah/jbda.bib";
	public static final String PDF_FILE = "jbda.pdf";
	public static final String PDF_PATH = "/var/jbdiah/" + PDF_FILE;

	public static final String MAKE_CMD = "make -C /var/jbdiah -f /var/jbdiah/Makefile";
}
