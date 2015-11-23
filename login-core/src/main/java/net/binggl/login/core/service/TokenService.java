package net.binggl.login.core.service;

import net.binggl.login.common.models.User;
import ninja.Context;

public interface TokenService {

	/**
	 * retrieve a token for the given user object
	 * @param user a User object
	 * @param secret the signing secret
	 * @return a string representation of the token
	 */
	String getToken(User user, String secret);
	
	/**
	 * set the token as a cookie
	 * @param context a Ninja Context
	 * @param token String form of the token
	 */
	void setCookie(Context context, String token);
	
	/**
	 * retrieve the token from the cookie
	 * @param context a Ninja Context
	 * @return the string form of the token
	 */
	String getTokenFromCookie(Context context);
	
	/**
	 * verify the given token
	 * @param token the token to verify
	 * @param secret the signing secret
	 * @return a User object when verification is correct
	 */
	User verifyToken(String token, String secret);
}
