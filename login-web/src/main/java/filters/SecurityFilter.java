package filters;

import static net.binggl.login.common.Constants.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.common.models.User;
import net.binggl.login.core.service.TokenService;
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
	
	@Inject
	private InternationalizationHelper i18n;
	@Inject
	private NinjaProperties properties;
	@Inject
	private TokenService tokenService;
	

	@Override
	public Result filter(FilterChain chain, Context context) {

		logger.debug("start: security check.");

		try {

			String token = tokenService.getTokenFromCookie(context);
			if(StringUtils.isEmpty(token)) {
				logger.warn("No token available, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "auth.token.missing"));
			}
			logger.debug("Got a token from the context! " + token);
			
	        User user = tokenService.verifyToken(token, properties.getOrDie(AUTH_TOKEN_SECRET));
	        if(user == null) {
	        	logger.warn("No user available, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "auth.user.invalid"));
	        }
	        logger.debug("Got a user from the token: {}", user);
		    
		} catch (Exception EX) {
			logger.error("Error during security filter check: " + EX.getMessage(), EX);
			return getErrorResult(EX);
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