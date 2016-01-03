package extractors;

import static net.binggl.login.core.Constants.SESSION_USER_ID;
import static net.binggl.login.core.util.ExceptionHelper.logEx;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.UserService;
import ninja.Context;
import ninja.params.ArgumentExtractor;

/**
 * extract the authenticated user from the session
 * @author henrik
 */
public class AuthenticatedUserExtractor implements ArgumentExtractor<User> {
    
	private static final Logger logger = LoggerFactory.getLogger(AuthenticatedUserExtractor.class);
	
	private UserService userService;
	
	@Inject
	public AuthenticatedUserExtractor(UserService userService) {
		this.userService = userService;
	}
	
	
	@Override
    public User extract(Context context) {
		
		return logEx(() -> {
			
			String userId = context.getSession().get(SESSION_USER_ID);
			if(StringUtils.isEmpty(userId)) {
				logger.info("Could not get a user from the session!");
				return null;
			}
			logger.debug("Try to find user by id {}", userId);
			User user = this.userService.findeUserByAlternativId(userId);
		    return user;
		});
    }

    @Override
    public Class<User> getExtractedType() {
        return User.class;
    }

    @Override
    public String getFieldName() {
        return null;
    }
}
