package de.jbdiah.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

public class AuthUtil {

	protected static Logger logger = Logger.getLogger(AuthUtil.class);

	public static final String sha1Hash(String password) {
		// 160-bit digest (40 hex numbers)
		byte[] hash = new byte[40];

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(password.getBytes("ISO-8859-1"), 0, password.length());
			hash = md.digest();

		} catch (NoSuchAlgorithmException nsae) {
			// TODO Auto-generated catch block
			nsae.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			// TODO Auto-generated catch block
			uee.printStackTrace();
		}
		return new String(hash);
	}

	public static final boolean compareHashes(byte[] pass1, byte[] pass2) {
		return MessageDigest.isEqual(pass1, pass2);
	}

	public static final String genAuthToken(String id, String salt) {
		// should not become null (NPE)
		String authToken = "";
		if (id != null && id.length() > 0) {
			Integer token = new Integer(
					(".::" + id + ":" + salt + "::.").hashCode());
			authToken = token.toString();
		}
		return authToken;
	}

	public static final boolean checkAuthTokens(String id, String salt,
			String authToken) {
		boolean checkOk = false;

		// avoid nullpointer
		if (id != null && authToken != null && id.length() > 0
				&& authToken.length() > 0) {

			// if the authtoken generated from the cookies
			// yields the same result as the authtoken the user passed,
			// we assume it's ok
			if (genAuthToken(id, salt).equals(authToken)) {
				checkOk = true;
			}

		}
		return checkOk;
	}

	public static String getUsernameFromCookies(Cookie[] cookies) {
		String username = "";

		if (cookies != null) {
			for (Cookie cookie : cookies) {

				if (Constants.COOKIE_USERNAME.equals(cookie.getName())) {
					username = cookie.getValue();
				}
			}
		}

		return username;
	}

	public static final String escapeHTML(String s) {
		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case 'à':
				sb.append("&agrave;");
				break;
			case 'À':
				sb.append("&Agrave;");
				break;
			case 'â':
				sb.append("&acirc;");
				break;
			case 'Â':
				sb.append("&Acirc;");
				break;
			case 'ä':
				sb.append("&auml;");
				break;
			case 'Ä':
				sb.append("&Auml;");
				break;
			case 'å':
				sb.append("&aring;");
				break;
			case 'Å':
				sb.append("&Aring;");
				break;
			case 'æ':
				sb.append("&aelig;");
				break;
			case 'Æ':
				sb.append("&AElig;");
				break;
			case 'ç':
				sb.append("&ccedil;");
				break;
			case 'Ç':
				sb.append("&Ccedil;");
				break;
			case 'é':
				sb.append("&eacute;");
				break;
			case 'É':
				sb.append("&Eacute;");
				break;
			case 'è':
				sb.append("&egrave;");
				break;
			case 'È':
				sb.append("&Egrave;");
				break;
			case 'ê':
				sb.append("&ecirc;");
				break;
			case 'Ê':
				sb.append("&Ecirc;");
				break;
			case 'ë':
				sb.append("&euml;");
				break;
			case 'Ë':
				sb.append("&Euml;");
				break;
			case 'ï':
				sb.append("&iuml;");
				break;
			case 'Ï':
				sb.append("&Iuml;");
				break;
			case 'ô':
				sb.append("&ocirc;");
				break;
			case 'Ô':
				sb.append("&Ocirc;");
				break;
			case 'ö':
				sb.append("&ouml;");
				break;
			case 'Ö':
				sb.append("&Ouml;");
				break;
			case 'ø':
				sb.append("&oslash;");
				break;
			case 'Ø':
				sb.append("&Oslash;");
				break;
			case 'ß':
				sb.append("&szlig;");
				break;
			case 'ù':
				sb.append("&ugrave;");
				break;
			case 'Ù':
				sb.append("&Ugrave;");
				break;
			case 'û':
				sb.append("&ucirc;");
				break;
			case 'Û':
				sb.append("&Ucirc;");
				break;
			case 'ü':
				sb.append("&uuml;");
				break;
			case 'Ü':
				sb.append("&Uuml;");
				break;
			case '®':
				sb.append("&reg;");
				break;
			case '©':
				sb.append("&copy;");
				break;
			case '€':
				sb.append("&euro;");
				break;
			case ' ':
				sb.append("&nbsp;");
				break;

			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

}
