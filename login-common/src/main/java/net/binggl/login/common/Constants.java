package net.binggl.login.common;

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
	 * token cookie
	 */
	public final static String TOKEN_COOKIE_NAME = "application.cookie.token.name";

	/**
	 * the domain of the cookie
	 */
	public final static String COOKIE_DOMAIN = "application.cookie.domain";
	
	/**
	 * max time to life for cookie
	 */
	public final static String COOKIE_MAXAGE = "application.cookie.maxAge";
	
	/**
	 * the cookie path
	 */
	public final static String COOKIE_PATH = "application.cookie.path";
	
	/**
	 * secure cookie
	 */
	public final static String COOKIE_SECURE = "application.cookie.secure";
	
	/**
	 * http only
	 */
	public final static String COOKIE_HTTP_ONLY = "application.cookie.httponly";
	
	
}
