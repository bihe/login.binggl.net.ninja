package controllers;

import extractors.AuthenticatedUser;
import net.binggl.login.common.models.User;
import ninja.Context;
import ninja.Result;
import ninja.Results;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * this is the "home/index" of the login logic.
 * if no valid session is available a login is shown,
 * otherwise the user is forwarded to the application dashboard
 */
@Singleton
public class HomeController {

    // members
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public Result index(Context context, @AuthenticatedUser User user) {

        if(user == null) {
            logger.debug("No authenticated user available show login screen!");
            return Results.html();
        }

        return Results.redirect("dashboard/");
    } 
}
 