package net.binggl.login.core.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import net.binggl.login.core.models.LoginType;
import net.binggl.login.core.service.SessionService;
import ninja.Context;

public class NinjaSessionService implements SessionService {

	private static final String SESSION_USER_ID = "session.user.id";
	private static final String SESSION_LOGIN_TYPE = "session.login.type";
	private static final String SESSION_AUTH_FLOW_SITE_NAME = "session.auth.flow.site.name";
	private static final String SESSION_AUTH_FLOW_SITE_URL = "session.auth.flow.site.url";
	
	private static final List<String> SESSION_PARAMETERS = Arrays.asList(
			SESSION_USER_ID, 
			SESSION_AUTH_FLOW_SITE_NAME, 
			SESSION_AUTH_FLOW_SITE_URL, 
			SESSION_LOGIN_TYPE);
	
	@Override
	public void setUserId(Context context, String id) {
		Preconditions.checkArgument(StringUtils.isNoneEmpty(id), "Supplied userId is empty!");
		context.getSession().put(SESSION_USER_ID, id);
	}

	@Override
	public String getUserId(Context context) {
		return context.getSession().get(SESSION_USER_ID);
	}
	
	@Override
	public void removeUserId(Context context) {
		context.getSession().remove(SESSION_USER_ID);
	}
	

	@Override
	public void setLoginType(Context context, String loginType) {
		Preconditions.checkArgument(StringUtils.isNoneEmpty(loginType), "Supplied loginType is empty!");
		context.getSession().put(SESSION_LOGIN_TYPE, loginType);
	}
	
	@Override
	public String getLoginType(Context context) {
		String loginType = context.getSession().get(SESSION_LOGIN_TYPE);
		return loginType != null ? loginType : LoginType.DIRECT.toString();
	}
	
	@Override
	public void removeLoginType(Context context) {
		context.getSession().remove(SESSION_LOGIN_TYPE);
	}
	

	@Override
	public void setAuthFlowSiteName(Context context, String siteName) {
		Preconditions.checkArgument(StringUtils.isNoneEmpty(siteName), "Supplied site-name is empty!");
		context.getSession().put(SESSION_AUTH_FLOW_SITE_NAME, siteName);
	}
	
	@Override
	public String getAuthFlowSiteName(Context context) {
		return context.getSession().get(SESSION_AUTH_FLOW_SITE_NAME);
	}

	@Override
	public void removeAuthFlowSiteName(Context context) {
		context.getSession().remove(SESSION_AUTH_FLOW_SITE_NAME);
	}
	

	@Override
	public void setAuthFlowUrl(Context context, String url) {
		Preconditions.checkArgument(StringUtils.isNoneEmpty(url), "Supplied site-url is empty!");
		context.getSession().put(SESSION_AUTH_FLOW_SITE_URL, url);
	}
	
	@Override
	public String getAuthFlowUrl(Context context) {
		return context.getSession().get(SESSION_AUTH_FLOW_SITE_URL);
	}

	@Override
	public void removeAuthFlowUrl(Context context) {
		context.getSession().remove(SESSION_AUTH_FLOW_SITE_URL);
	}

	@Override
	public void clear(Context context) {
		SESSION_PARAMETERS.stream().forEach((param) -> {
			context.getSession().remove(param);
		});
	}	
}
