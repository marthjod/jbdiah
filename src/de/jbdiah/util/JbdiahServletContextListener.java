package de.jbdiah.util;

import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;

public class JbdiahServletContextListener implements
		javax.servlet.ServletContextListener {

	protected static Logger logger = Logger
			.getLogger(JbdiahServletContextListener.class);

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void contextInitialized(ServletContextEvent sce) {
	}
}
