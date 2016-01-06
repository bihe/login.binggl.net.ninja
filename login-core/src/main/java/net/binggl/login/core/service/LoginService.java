package net.binggl.login.core.service;

import net.binggl.login.core.models.User;
import ninja.Context;

public interface LoginService {

	/**
	 * materialize user from stored data
	 * @param context the web context
	 * @return User object
	 */
	User materializeUser(Context context);
	
	/**
	 * login the given user
	 * @param user a user object
	 * @param context the web context
	 * @return result of login
	 */
	boolean login(User user, Context context);
	
	/**
	 * logout the user
	 * @param user a user object
	 * @param context the web context
	 * @return result of login
	 */
	boolean logout(User user, Context context);
	
	/**
	 * check if the supplied url is valid for the given user
	 * @param user a user object
	 * @param siteName the name of the originating site
	 * @param redirectUrl the redirect url of the orginiating site
	 * @return
	 */
	boolean isValidRedirectUrl(User user, String siteName, String redirectUrl);
}
