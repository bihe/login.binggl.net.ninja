package net.binggl.login.core;

/**
 * global constants, values, ...
 * 
 * @author henrik
 */
public class Constants {

	/**
	 * the base path of the application
	 */
	public static final String CONFIG_BASE_PATH = "application.basepath";
	
	/**
	 * the key used to store the user in the session
	 */
	public static final String SESSION_USER_ID = "session.user.id";
	
	/**
	 * additional mode switch in the application.conf file
	 * PROD and DEV
	 */
	public static final String APPLICATION_MODE = "application.mode";
	
	/**
	 * the value indicating development mode
	 */
	public static final String MODE_DEVELOPMENT = "DEV";

	/**
	 * the secret used for token signing
	 */
	public final static String AUTH_TOKEN_SECRET = "auth.token.secret";

	/**
	 * token cookie
	 */
	public final static String TOKEN_COOKIE_NAME = "auth.cookie.token.name";

	/**
	 * the domain of the cookie
	 */
	public final static String COOKIE_DOMAIN = "auth.cookie.domain";

	/**
	 * max time to life for cookie
	 */
	public final static String COOKIE_MAXAGE = "auth.cookie.maxAge";

	/**
	 * the cookie path
	 */
	public final static String COOKIE_PATH = "auth.cookie.path";

	/**
	 * secure cookie
	 */
	public final static String COOKIE_SECURE = "auth.cookie.secure";

	/**
	 * http only
	 */
	public final static String COOKIE_HTTP_ONLY = "auth.cookie.httponly";

}
