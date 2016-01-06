package controllers.flow;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import controllers.AbstractController;
import extractors.AuthenticatedUser;
import net.binggl.login.core.models.LoginType;
import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.SessionService;
import ninja.Context;
import ninja.Result;
import ninja.Results;

/**
 * handel authentication requests by other applications
 * @author henrik
 */
@Singleton
public class AuthenticationController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	private static final String AUTH_START_URL = "/startauth";
	
	private static final String AUTH_FLOW_PARAM_SITE = "~site";
	private static final String AUTH_FLOW_PARAM_RETURN_URL = "~url";
	
	private LoginService loginService;
	private SessionService sessionService;
	
	@Inject
	public AuthenticationController(LoginService loginService, SessionService sessionService) {
		this.loginService = loginService;
		this.sessionService = sessionService;
	}
	
	public Result startFlow(Context context,  @AuthenticatedUser User user) {
		Result result = Results.ok();
		
		try {
		
			String siteName = context.getParameter(AUTH_FLOW_PARAM_SITE);
			String returnUrl  = context.getParameter(AUTH_FLOW_PARAM_RETURN_URL);
			
			Preconditions.checkArgument(StringUtils.isNotEmpty(siteName)
					&& StringUtils.isNotEmpty(returnUrl), "Suppliead site-name or site-url are empty! A param ~site and ~url is necessary.");
			
			if(user != null) {
				logger.info("A user is available {} - no need to start the whole authentication process!", user);
				if(this.loginService.isValidRedirectUrl(user, siteName, returnUrl)) {
					logger.debug("Will redirect to url {}", returnUrl);
					return Results.redirect(returnUrl);
				}
				throw new IllegalArgumentException("Permissions missing or redirect url is not valid!");
			}
			
			logger.debug("Start the authentication flow for site {} with url {}", siteName, returnUrl);
			
			this.sessionService.setAuthFlowSiteName(context, siteName);
			this.sessionService.setAuthFlowUrl(context, returnUrl);
			this.sessionService.setLoginType(context, LoginType.FORWARD.toString());
			
			// no user available - start the oauth process
			result = Results.redirect(this.getBaseUrl(context) + AUTH_START_URL);
		
		} catch(Exception EX) {
			logger.error("Error occured during authentication flow {}", EX.getMessage());
			result = this.getErrorResult(EX);
		}
		return result;
	}
}
