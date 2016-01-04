package filters;

import static net.binggl.login.core.Constants.CONFIG_BASE_PATH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.Results;
import ninja.session.FlashScope;
import ninja.utils.NinjaProperties;
import util.InternationalizationHelper;

/**
 * check the loged-in user
 * @author henrik
 */
public class SecurityFilter implements Filter {

	// members
	private static final Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);
	
	private NinjaProperties properties;
	private LoginService loginService;
	private InternationalizationHelper i18n;
	
	@Inject
	public SecurityFilter(LoginService loginService, NinjaProperties properties, InternationalizationHelper i18n) {
		this.loginService = loginService;
		this.properties = properties;
		this.i18n = i18n;
	}
	

	@Override
	public Result filter(FilterChain chain, Context context) {

		logger.debug("start: security check.");

		try {
			
			User user = this.loginService.materializeUser(context);
			if(user == null) {
				logger.warn("No user available, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "auth.user.invalid"));
			}
		    
		} catch (Throwable e) {
			logger.error("Error during security filter check: " + e.getMessage(), e);
			return getErrorResult(e);
		}

		logger.debug("done: security check!");
		
		return chain.next(context);
	}
	
	
	private Result getNoAccessResult(Context context, String message) {
		Result r = Results.forbidden();
		context.getSession().clear();
		FlashScope flashScope = context.getFlashScope();
		flashScope.error(message);
		r = Results.redirect(properties.get(CONFIG_BASE_PATH) + "login");
		return r;
	}
	
	private Result getErrorResult(Throwable thr) {
		Result result = Results.internalServerError();
		result.render("message", thr.getMessage());
		return result.html().template("/views/system/500error.ftl.html");
	}
	
	
}