package net.binggl.login.core.service.impl;

import static net.binggl.login.common.Constants.*;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.google.inject.Inject;

import net.binggl.login.common.models.User;
import net.binggl.login.core.service.TokenService;
import ninja.Context;
import ninja.Cookie;
import ninja.utils.NinjaProperties;

public class JwtTokenService implements TokenService {

	private static final String USER_ID = "UserId";
	private static final String DISPLAYNAME = "DisplayName";
	private static final String EMAIL = "Email";
	
	
	private NinjaProperties properties;
	
	@Inject
	public JwtTokenService(NinjaProperties properties) {
		this.properties = properties;
	}
	
	@Override
	public String getToken(User user, String secret) {
		if(user == null)
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
	public String getTokenFromCookie(Context context) {
		String token = null;
		Cookie cookie = context.getCookie(properties.get(TOKEN_COOKIE_NAME));
		if(cookie != null) {
			token = cookie.getValue();
		}
		return token;
	}
	
	@Override
	public User verifyToken(String token, String secret) {
		User user = null;
		JWTVerifier jwtVerifier = new JWTVerifier(secret);
		try {
			Map<String, Object> payload = jwtVerifier.verify(token);
			if(payload != null) {
				user = this.getUserObject(payload);
			}
		} catch(Exception EX) {
			throw new RuntimeException(EX);
		}
		
		return user;
	}
	
	
	
	private Cookie createCookie(Context context, String token) {
		Cookie cookie = new Cookie(properties.get(TOKEN_COOKIE_NAME), token, 
				"", 
				properties.get(COOKIE_DOMAIN), 
				properties.getInteger(COOKIE_MAXAGE),
				properties.get(COOKIE_PATH),
				properties.getBoolean(COOKIE_SECURE),
				properties.getBoolean(COOKIE_HTTP_ONLY)
				); 
		return cookie;
	}
	
	private Map<String, Object> getUserMap(User user) {
		Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(USER_ID, user.getId());
        payload.put(DISPLAYNAME, user.getDisplayName());
        payload.put(EMAIL, user.getEmail());
        return payload;
	}
	
	private User getUserObject(Map<String, Object> payload) {
		User user = new User((String)payload.get(EMAIL), 
				(String)payload.get(DISPLAYNAME),
				(String)payload.get(USER_ID));
		return user;
	}

}
