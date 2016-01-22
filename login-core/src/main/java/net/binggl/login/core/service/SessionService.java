package net.binggl.login.core.service;

import ninja.Context;

public interface SessionService {

	/**
	 * clear the whole sessiion
	 * @param context
	 */
	void clear(Context context);
	
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
	 * remove the entry from the session
	 * @param context a NinjaFramework context
	 */
	void removeUserId(Context context);

	
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
	
	/**
	 * remove the entry from the session
	 * @param context a NinjaFramework context
	 */
	void removeLoginType(Context context);
	

	/**
	 * store the name of the originating site
	 * @param context a NinjaFramework context
	 * @param siteName the name of the originating site
	 */
	void setAuthFlowSiteName(Context context, String siteName);
	
	/**
	 * retrieve the name of the originating site
	 * @param context a NinjaFramework context
	 * @return
	 */
	String getAuthFlowSiteName(Context context);
	
	/**
	 * remove the entry from the session
	 * @param context a NinjaFramework context
	 */
	void removeAuthFlowSiteName(Context context);
	
	
	/**
	 * store the url of the originating site
	 * @param context a NinjaFramework context
	 * @param siteName the name of the originating site
	 */
	void setAuthFlowUrl(Context context, String url);
	
	/**
	 * retrieve the url of the originating site
	 * @param context a NinjaFramework context
	 * @return
	 */
	String getAuthFlowUrl(Context context);
	
	/**
	 * remove the entry from the session
	 * @param context a NinjaFramework context
	 */
	void removeAuthFlowUrl(Context context);
}
