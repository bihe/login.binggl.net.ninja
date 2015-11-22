package service;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import controllers.HomeController;
import net.binggl.login.common.models.User;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import net.binggl.ninja.oauth.OauthAuthorizationService;
import ninja.Context;

public class OauthMongoDbAuthorizationService implements OauthAuthorizationService {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Inject private UserService userService;
	@Inject private TokenService tokenService; 
	
	
	@Override
	public boolean lookupAndProcessProfile(Context context, Google2Profile profile) {
		try {
			logger.debug("Try to find user by email: {}", profile.getEmail());
			
			User user = userService.findUserByEmail(profile.getEmail());
			if(user != null) {
				String token = tokenService.getToken(user);
				logger.debug("Got a user {}  create a token and save the token {}", user, token);
				
				if(StringUtils.isNotEmpty(token)) {
					tokenService.setCookie(context, token);
					return true;
				} 
				logger.warn("Could not create a token for user {}", user);
			}
			
			
		} catch(Exception EX) {
			logger.error("Could not lookup the user by the given profile {} {}", profile, EX.getMessage());
		}
		return false;
	}

}
