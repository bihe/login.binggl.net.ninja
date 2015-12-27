package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import extractors.AuthenticatedUser;
import net.binggl.login.core.models.User;
import ninja.Context;
import ninja.Result;
import ninja.Results;


/**
 * this is the "home/index" of the login logic.
 * if no valid session is available a login is shown,
 * otherwise the user is forwarded to the application dashboard
 */
@Singleton
public class HomeController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
   
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
}
 