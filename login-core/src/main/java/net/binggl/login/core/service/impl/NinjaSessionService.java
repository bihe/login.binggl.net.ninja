package net.binggl.login.core.service.impl;

import net.binggl.login.core.models.LoginType;
import net.binggl.login.core.service.SessionService;
import ninja.Context;

public class NinjaSessionService implements SessionService {

	private static final String SESSION_USER_ID = "session.user.id";
	private static final String SESSION_LOGIN_TYPE = "session.login.type";
	
	@Override
	public void setUserId(Context context, String id) {
		context.getSession().put(SESSION_USER_ID, id);

	}

	@Override
	public String getUserId(Context context) {
		return context.getSession().get(SESSION_USER_ID);
	}

	@Override
	public void setLoginType(Context context, String loginType) {
		context.getSession().put(SESSION_LOGIN_TYPE, loginType);
		
	}

	@Override
	public String getLoginType(Context context) {
		String loginType = context.getSession().get(SESSION_LOGIN_TYPE);
		return loginType != null ? loginType : LoginType.DIRECT.toString();
	}
}
