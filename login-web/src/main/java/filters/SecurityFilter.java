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

public class SecurityFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	
	private LoginService loginService;
	private NinjaProperties properties;
	private InternationalizationHelper i18n;

	@Inject
	public SecurityFilter(LoginService loginService, NinjaProperties properties, InternationalizationHelper i18n) {
		this.loginService = loginService;
		this.properties = properties;
		this.i18n = i18n;
	}
	
	@Override
	public Result filter(FilterChain filterChain, Context context) {
		try {
			User user = this.loginService.materializeUser(context);
			if(user != null) {
				return filterChain.next(context);
			}
		} catch(Exception EX) {
			logger.error("Error during filter operation {}; stack: {}", EX.getMessage(), EX);
		}
		return this.getNoAccessResult(context);
	}
	
	protected Result getNoAccessResult(Context context) {
		logger.warn("No user available, show login view!");
		return getNoAccessResult(context, i18n.getMessage(context, "auth.user.invalid"));
	}
	
	private Result getNoAccessResult(Context context, String message) {
		Result r = Results.ok();
		context.getSession().clear();
		FlashScope flashScope = context.getFlashScope();
		flashScope.error(message);
		r = Results.redirect(properties.get(CONFIG_BASE_PATH) + "403");
		return r;
	}	
}
