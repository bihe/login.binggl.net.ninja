package controllers;

import static net.binggl.login.core.Constants.SESSION_USER_ID;
import static net.binggl.login.core.util.ExceptionHelper.logEx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import extractors.AuthenticatedUser;
import net.binggl.login.core.models.User;
import net.binggl.login.core.service.TokenService;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.session.FlashScope;


/**
 * this is the "home/index" of the login logic.
 * if no valid session is available a login is shown,
 * otherwise the user is forwarded to the application dashboard
 */
@Singleton
public class HomeController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    private TokenService tokenService;
    
    @Inject
    public HomeController(TokenService tokenService) {
    	this.tokenService = tokenService;
    }
    
    public Result index(Context context, @AuthenticatedUser User user) {
        if(user == null) {
            logger.debug("No authenticated user available show login screen!");
            return this.processTemplateResult(Results.html());
        }
        return Results.redirect(this.getBaseUrl(context) + this.getBasePath() + "assets/index.html");
    } 
    
    public Result login() {
        return this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
    } 
    
    public Result logout(Context context, @AuthenticatedUser User user) {
    	logEx(() -> {
    		FlashScope flashScope = context.getFlashScope();
    		if(user == null) {
        		flashScope.error("No user available to logout!");
            } else {   
	    		context.getSession().remove(SESSION_USER_ID);
	        	this.tokenService.unsetCookie(context);
	        	flashScope.success("User " + user.getDisplayName() + " loged out!");
            }
    	});
    	return this.processTemplateResult(Results.html().template("views/HomeController/index.ftl.html"));
    } 
}
 