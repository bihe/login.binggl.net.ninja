package controllers;

import static net.binggl.login.core.util.ExceptionHelper.logEx;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import extractors.AuthenticatedUser;
import net.binggl.login.core.models.LoginType;
import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.SessionService;
import ninja.Context;
import ninja.Result;
import ninja.Results;

/**
 * this is the "home/index" of the login logic. if no valid session is available
 * a login is shown, otherwise the user is forwarded to the application
 * dashboard
 */
@Singleton
public class HomeController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private LoginService loginService;
	private SessionService sessionService;

	@Inject
	public HomeController(LoginService loginService, SessionService sessionService) {
		this.loginService = loginService;
		this.sessionService = sessionService;
	}

	public Result index(Context context, @AuthenticatedUser User user) {
		try {
		
			if (user == null) {
				return getNoAccessResult(context);
			}
			
			// if this is an authentication flow, redirect to the originating url
			String siteName = this.sessionService.getAuthFlowSiteName(context);
			String returnUrl = this.sessionService.getAuthFlowUrl(context);
			if(StringUtils.isNotEmpty(siteName) && StringUtils.isNotEmpty(returnUrl)) {
			
				logger.debug("Application authentication flow. Site {} / Url {}", siteName, returnUrl);
				
				if(this.loginService.isValidRedirectUrl(user, siteName, returnUrl)) {
					logger.debug("Will redirect to url {}", returnUrl);
					
					this.sessionService.removeAuthFlowSiteName(context);
					this.sessionService.removeAuthFlowUrl(context);
					this.sessionService.removeLoginType(context);
					
					return Results.redirect(returnUrl);
				}
				throw new IllegalArgumentException("Permissions missing or redirect url is not valid!");
			}
		
		
		} catch(Exception EX) {
			logger.error("Error occured during authentication flow {}, stack: {}", EX.getMessage(), EX);
			return this.getErrorResult(EX);
		}
		
		return Results.redirect(this.getBaseUrl(context) + this.getBasePath() + "assets/index.html");
	}

	public Result login(Context context) {
		this.sessionService.setLoginType(context, LoginType.DIRECT.toString());
		return this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
	}
	
	public Result show403(Context context) {
		return this.processTemplateResult(Results.forbidden().html().template("views/HomeController/index.ftl.html"));
	}
	
	public Result show404(Context context) {
		return this.processTemplateResult(Results.notFound().html().template("views/system/404notFound.ftl.html"));
	}
	

	public Result logout(Context context, @AuthenticatedUser User user) {
		Result result = logEx(() -> {
			if (user == null) {
				return getNoAccessResult(context);
			}
			boolean logout = loginService.logout(user, context);
			logger.debug("Result from logout operation {}", logout);
			return this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
		});
		return result != null ? result
				: this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
	}
}
