package service;

import static net.binggl.login.core.Constants.AUTH_TOKEN_SECRET;
import static net.binggl.login.core.Constants.SESSION_USER_ID;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import net.binggl.ninja.oauth.OauthAuthorizationService;

import ninja.Context;
import ninja.utils.NinjaProperties;

public class OauthTokenAuthorizationService implements OauthAuthorizationService {

	private static final Logger logger = LoggerFactory.getLogger(OauthTokenAuthorizationService.class);

	@Inject
	private UserService userService;
	@Inject
	private TokenService tokenService;
	@Inject
	private NinjaProperties properties;

	@Override
	public boolean lookupAndProcessProfile(Context context, Google2Profile profile) {
		try {
			logger.debug("Try to find user by email: {}", profile.getEmail());

			User user = userService.findUserByEmail(profile.getEmail());
			if (user != null) {
				String token = tokenService.getToken(user, this.getTokenSecret());
				logger.debug("Got a user {}  create a token and save the token {}", user, token);

				if (StringUtils.isNotEmpty(token)) {
					tokenService.setCookie(context, token);
					context.getSession().put(SESSION_USER_ID, user.getId());
					return true;
				}
				logger.warn("Could not create a token for user {}", user);
			}

		} catch (Exception EX) {
			logger.error("Could not lookup the user by the given profile {} {}", profile, EX.getMessage());
		}
		return false;
	}

	private String getTokenSecret() {
		return properties.getOrDie(AUTH_TOKEN_SECRET);
	}
}
