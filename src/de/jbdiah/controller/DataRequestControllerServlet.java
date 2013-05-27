package de.jbdiah.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import de.jbdiah.util.AuthUtil;
import de.jbdiah.util.Constants;


public class DataRequestControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger
			.getLogger(DataRequestControllerServlet.class);

	public DataRequestControllerServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream stream = null;
		BufferedInputStream buf = null;

		try {
			File pdf = new File(Constants.PDF_PATH);

			response.setContentLength((int) pdf.length());
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "filename=\""
					+ Constants.PDF_FILE + "\"");

			stream = response.getOutputStream();
			FileInputStream input = new FileInputStream(pdf);

			buf = new BufferedInputStream(input);
			int readBytes = 0;

			while ((readBytes = buf.read()) != -1)
				stream.write(readBytes);
		} catch (IOException ioe) {
			throw new ServletException(ioe.getMessage());
		} finally {
			if (stream != null)
				stream.close();
			if (buf != null)
				buf.close();
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String request_type = "";
		String base64_data = "";
		String text_data = "";
		byte[] bytes;
		File file;
		String filename = "";
		int response_status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

		BufferedReader buf = request.getReader();
		base64_data = buf.readLine();

		// logger.debug(base64_data);

		request_type = request.getRequestURI().substring(
				request.getRequestURI().lastIndexOf("/") + 1);
		logger.debug("Source file type/name = " + request_type);

		if ("bib".equals(request_type)) {
			filename = Constants.BIBTEX_FILE;
		} else if ("md".equals(request_type)) {
			filename = Constants.MARKDOWN_FILE;
		}

		logger.debug("Will write to file " + filename);

		if (!"".equals(filename)) {
			file = new File(filename);

			base64_data = base64_data.substring(base64_data.indexOf(",") + 1);

			bytes = new BASE64Decoder().decodeBuffer(base64_data);
			text_data = new String(bytes);

			try {
				FileWriter writer = new FileWriter(file, false);
				writer.write(text_data);
				writer.flush();
				writer.close();
				response_status = HttpServletResponse.SC_OK;
			} catch (IOException e) {
				e.printStackTrace();
				response_status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			} finally {
				response.setStatus(response_status);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String output = "";
		int response_status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		long startTime, endTime, duration = 0L;

		logger.debug("Conversion request received");
		output += "Conversion request received, starting make.\n\n";

		Runtime r = Runtime.getRuntime();

		try {
			startTime = System.currentTimeMillis();
	
			Process p = r.exec(Constants.MAKE_CMD);
			InputStream in = p.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(in);
			InputStreamReader inread = new InputStreamReader(buf);
			BufferedReader bufferedreader = new BufferedReader(inread);
			String line = "";
			while ((line = bufferedreader.readLine()) != null) {
				output += AuthUtil.escapeHTML(line) + "\n";
			}

			try {
				int retval = p.waitFor();
				if (0 != retval) {
					logger.error("Exit value = " + p.exitValue());
					output += "Make unsuccessful, exit value = " + p.exitValue() + ", ";
				} else {
					output += "\nMake successful, exit value " + retval
							+ ", ";
					response_status = HttpServletResponse.SC_OK;
				}
				
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				output += "took " + duration + " msecs.\n";
			} catch (InterruptedException e) {
				logger.error(e);
				output += e + "\n";
			} finally {
				// close the InputStream
				bufferedreader.close();
				inread.close();
				buf.close();
				in.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			output += e.getMessage() + "\n";
		}

		response.setStatus(response_status);
		PrintWriter out = response.getWriter();
		out.print(output);
		out.flush();
		out.close();
	}
}
