package net.binggl.login.core.service.impl;

import static net.binggl.login.core.Constants.COOKIE_DOMAIN;
import static net.binggl.login.core.Constants.COOKIE_HTTP_ONLY;
import static net.binggl.login.core.Constants.COOKIE_MAXAGE;
import static net.binggl.login.core.Constants.COOKIE_PATH;
import static net.binggl.login.core.Constants.COOKIE_SECURE;
import static net.binggl.login.core.Constants.TOKEN_COOKIE_NAME;
import static net.binggl.login.core.util.ExceptionHelper.wrapEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.google.inject.Inject;

import net.binggl.login.core.models.Site;
import net.binggl.login.core.models.User;
import net.binggl.login.core.service.TokenService;
import ninja.Context;
import ninja.Cookie;
import ninja.utils.NinjaProperties;

public class NinjaJwtTokenService implements TokenService {

	private static final String USER_ID = "UserId";
	private static final String USERNAME = "UserName";
	private static final String DISPLAYNAME = "DisplayName";
	private static final String EMAIL = "Email";
	private static final String CLAIMS = "Claims";
	private static final String TYPE = "Type";
	private static final String TYPE_VALUE = "login.User";

	private NinjaProperties properties;

	@Inject
	public NinjaJwtTokenService(NinjaProperties properties) {
		this.properties = properties;
	}

	@Override
	public String getToken(User user, String secret) {
		if (user == null)
			return null;

		JWTSigner jwtSigner = new JWTSigner(secret);
		Map<String, Object> payload = this.getUserMap(user);

		String token = jwtSigner.sign(payload);
		return token;
	}

	@Override
	public void setCookie(Context context, String token) {
		Cookie cookie = this.createCookie(context, token);
		context.addCookie(cookie);
	}
	
	@Override
	public void unsetCookie(Context context) {
		Cookie cookie = this.removeCookie(context);
		context.unsetCookie(cookie);
	}
	

	@Override
	public String getTokenFromCookie(Context context) {
		String token = null;
		Cookie cookie = context.getCookie(properties.get(TOKEN_COOKIE_NAME));
		if (cookie != null) {
			token = cookie.getValue();
		}
		return token;
	}

	@Override
	public boolean verifyToken(String token, String secret) {
		boolean result = false;
		JWTVerifier jwtVerifier = new JWTVerifier(secret);
		
		result = wrapEx(() -> {
			Map<String, Object> payload = jwtVerifier.verify(token);
			if (payload != null) {
				if(payload.size() == 6 
						&& payload.get(TYPE) != null
						&& TYPE_VALUE.equals(payload.get(TYPE))) {
					return true;
				}
			}
			return false;
		});
		
		return result;
	}

	private Cookie createCookie(Context context, String token) {
		Cookie cookie = new Cookie(properties.get(TOKEN_COOKIE_NAME), token, "", 
				properties.get(COOKIE_DOMAIN),
				properties.getInteger(COOKIE_MAXAGE), 
				properties.get(COOKIE_PATH), 
				properties.getBoolean(COOKIE_SECURE),
				properties.getBoolean(COOKIE_HTTP_ONLY));
		return cookie;
	}
	
	private Cookie removeCookie(Context context) {
		Cookie cookie = new Cookie(properties.get(TOKEN_COOKIE_NAME), "", "", 
				properties.get(COOKIE_DOMAIN),
				0, // remove cookie
				properties.get(COOKIE_PATH), 
				properties.getBoolean(COOKIE_SECURE),
				properties.getBoolean(COOKIE_HTTP_ONLY));
		return cookie;
	}

	private Map<String, Object> getUserMap(User user) {
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put(USER_ID, user.getId());
		payload.put(USERNAME, user.getUserName());
		payload.put(DISPLAYNAME, user.getDisplayName());
		payload.put(EMAIL, user.getEmail());
		
		List<String> sitePermissions = new ArrayList<>();
		if(user.getSitePermissions() != null) {
			for(Site s : user.getSitePermissions()) {
				sitePermissions.add(this.formatSitePermissions(s));
			}
		}
		payload.put(CLAIMS, sitePermissions);
		
		payload.put(TYPE, TYPE_VALUE);
		return payload;
	}
	
	
	/**
	 * format the sites/permissions
	 * site-name|site-url|permission1;permission2
	 * @param site
	 * @return
	 */
	protected String formatSitePermissions(Site site) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(site.getName());
		buffer.append("|");
		buffer.append(site.getUrl());
		buffer.append("|");
		int i=0;
		for(String permission : site.getPermissions()) {
			if(i>0)
				buffer.append(";");
			buffer.append(permission);
			i++;
		}
		return buffer.toString();
	}

}
