package controllers;

import static net.binggl.login.core.util.ExceptionHelper.logEx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import extractors.AuthenticatedUser;
import net.binggl.login.core.models.User;
import net.binggl.login.core.service.LoginService;
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

	@Inject
	public HomeController(LoginService loginService) {
		this.loginService = loginService;
	}

	public Result index(Context context, @AuthenticatedUser User user) {
		if (user == null) {
			return getNoAccessResult(context);
		}
		return Results.redirect(this.getBaseUrl(context) + this.getBasePath() + "assets/index.html");
	}

	public Result login() {
		return this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
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
