package net.binggl.login.core.service;

import ninja.Context;

public interface SessionService {

	/**
	 * set the userid in the session
	 * @param context a NinjaFramework context
	 * @param id
	 */
	void setUserId(Context context, String id);
	
	/**
	 * retrieve the userid
	 * @param context a NinjaFramework context
	 * @return
	 */
	String getUserId(Context context);
	
	/**
	 * set the logintype in the session
	 * @param context a NinjaFramework context
	 * @param loginType
	 */
	void setLoginType(Context context, String loginType);
	
	/**
	 * retrieve the loginType
	 * @param context a NinjaFramework context
	 * @return
	 */
	String getLoginType(Context context);
	
}
