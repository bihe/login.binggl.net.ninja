package net.binggl.login.core.service.impl;

import static net.binggl.login.common.Constants.COOKIE_DOMAIN;
import static net.binggl.login.common.Constants.COOKIE_HTTP_ONLY;
import static net.binggl.login.common.Constants.COOKIE_MAXAGE;
import static net.binggl.login.common.Constants.COOKIE_PATH;
import static net.binggl.login.common.Constants.COOKIE_SECURE;
import static net.binggl.login.common.Constants.TOKEN_COOKIE_NAME;

import com.google.inject.Inject;

import net.binggl.login.common.models.User;
import net.binggl.login.core.service.TokenService;
import ninja.Context;
import ninja.Cookie;
import ninja.utils.NinjaProperties;

public class JwtTokenService implements TokenService {

	@Inject private NinjaProperties properties;
	
	@Override
	public String getToken(User user) {
		return "token";
	}

	@Override
	public void setCookie(Context context, String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTokenFromCookie(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Cookie getCookie(Context context) {
		Cookie cookie = new Cookie(properties.get(TOKEN_COOKIE_NAME), "token", "", 
				properties.get(COOKIE_DOMAIN), 
				properties.getInteger(COOKIE_MAXAGE),
				properties.get(COOKIE_PATH),
				properties.getBoolean(COOKIE_SECURE),
				properties.getBoolean(COOKIE_HTTP_ONLY)
				); 
		return cookie;
		
	}

	
	
}
