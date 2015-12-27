package net.binggl.login.core;

/**
 * global constants, values, ...
 * @author henrik
 */
public class Constants {

	/**
	 * the secret used for token signing
	 */
	public final static String AUTH_TOKEN_SECRET = "auth.token.secret";
	
	/**
	 * the base path of the application
	 */
	public static final String CONFIG_BASE_PATH = "application.basepath";
	
	/**
	 * token cookie
	 */
	public final static String TOKEN_COOKIE_NAME = "application.auth.cookie.token.name";

	/**
	 * the domain of the cookie
	 */
	public final static String COOKIE_DOMAIN = "application.auth.cookie.domain";
	
	/**
	 * max time to life for cookie
	 */
	public final static String COOKIE_MAXAGE = "application.auth.cookie.maxAge";
	
	/**
	 * the cookie path
	 */
	public final static String COOKIE_PATH = "application.auth.cookie.path";
	
	/**
	 * secure cookie
	 */
	public final static String COOKIE_SECURE = "application.auth.cookie.secure";
	
	/**
	 * http only
	 */
	public final static String COOKIE_HTTP_ONLY = "application.auth.cookie.httponly";
	
	
}
