package util;

import org.apache.commons.lang3.StringUtils;

import models.Token;
import ninja.Context;

public class TokenHelper {

	public static String SESSION_TOKEN = "auth.session.token";
	
	/**
	 * set the token in the context
	 * @param context
	 * @param token
	 * @throws Throwable
	 */
	public static void setToken(Context context, Token token) throws Throwable {
		context.getSession().put(SESSION_TOKEN, JsonSerializer.serialize(token));
	}

	/**
	 * get the token from the context
	 * @param context
	 * @return
	 * @throws Throwable
	 */
	public static Token getToken(Context context) throws Throwable {
		Token token = null;
		String sessionPayload = "";
		if(context.getSession() != null) {
			sessionPayload = context.getSession().get(SESSION_TOKEN);
		}
		if(StringUtils.isNotEmpty(sessionPayload)) {
			token = JsonSerializer.deserialize(sessionPayload, Token.class);
			
			if(!token.getChecksum().equals(token.getCalculatedChecksum()))
				throw new IllegalArgumentException("Token checksum does not match!");
			
		}
		return token;
	}
}
