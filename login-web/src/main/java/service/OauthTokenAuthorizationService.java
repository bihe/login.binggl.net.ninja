package service;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.UserService;
import net.binggl.ninja.oauth.OauthAuthorizationService;
import ninja.Context;

public class OauthTokenAuthorizationService implements OauthAuthorizationService {

	private static final Logger logger = LoggerFactory.getLogger(OauthTokenAuthorizationService.class);

	private UserService userService;
	private LoginService loginService;
	
	@Inject
	public OauthTokenAuthorizationService(UserService userService, LoginService loginService) {
		this.userService = userService;
		this.loginService = loginService;
	}

	@Override
	public boolean lookupAndProcessProfile(Context context, Google2Profile profile) {
		try {
			logger.debug("Try to find user by email: {}", profile.getEmail());

			User user = userService.findUserByEmail(profile.getEmail());
			return this.loginService.login(user, context);

		} catch (Exception EX) {
			logger.error("Could not lookup the user by the given profile {} {}", profile, EX.getMessage());
		}
		return false;
	}
}
