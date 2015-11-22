package net.binggl.login.core.service;

import net.binggl.login.common.models.User;
import ninja.Context;

public interface TokenService {

	/**
	 * retrieve a token for the given user object
	 * @param user
	 * @return
	 */
	String getToken(User user);
	
	void setCookie(Context context, String token);
	
	String getTokenFromCookie(Context context);
}
