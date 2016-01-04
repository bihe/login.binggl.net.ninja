package extractors;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
import ninja.Context;
import ninja.params.ArgumentExtractor;

/**
 * extract the authenticated user from the session
 * @author henrik
 */
public class AuthenticatedUserExtractor implements ArgumentExtractor<User> {
    
	private LoginService loginService;
	
	@Inject
	public AuthenticatedUserExtractor(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@Override
    public User extract(Context context) {
		return this.loginService.materializeUser(context);
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
