package extractors;

import static net.binggl.login.core.Constants.AUTH_TOKEN_SECRET;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.TokenService;
import ninja.Context;
import ninja.params.ArgumentExtractor;
import ninja.utils.NinjaProperties;

/**
 * extract the authenticated user from the session
 * @author henrik
 */
public class AuthenticatedUserExtractor implements ArgumentExtractor<User> {
    
	private static final Logger logger = LoggerFactory.getLogger(AuthenticatedUserExtractor.class);
	
	private TokenService tokenService;
	private NinjaProperties properties;
	
	@Inject
	public AuthenticatedUserExtractor(NinjaProperties properties, TokenService tokenService){
		this.tokenService = tokenService;
		this.properties = properties;
	}
	
	
	@Override
    public User extract(Context context) {
		try {
			String token = tokenService.getTokenFromCookie(context);
			if(StringUtils.isEmpty(token))
				return  null;
			logger.debug("Got a token from the context!");
	        User user = tokenService.verifyToken(token, this.getTokenSecret());
	        logger.debug("Got a user from the token: {}", user);
		    return user;
		} catch(Exception EX) {
			logger.error("Could not extract user from context: " + EX.getMessage(), EX);
		}
		return null;
    }

    @Override
    public Class<User> getExtractedType() {
        return User.class;
    }

    @Override
    public String getFieldName() {
        return null;
    }
    
    
    
    private String getTokenSecret() {
		return properties.getOrDie(AUTH_TOKEN_SECRET);
	}
}
