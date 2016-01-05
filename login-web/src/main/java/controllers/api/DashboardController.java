package controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import controllers.AbstractController;
import extractors.AuthenticatedUser;
import net.binggl.login.core.models.User;
import ninja.Context;
import ninja.Result;
import ninja.Results;

/**
 * manages the application dashboard
 * @author henrik
 */
@Singleton
public class DashboardController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
	
	public Result currentUser(Context context, @AuthenticatedUser User user) {
		if (user == null) {
			return getNoAccessResult(context);
		}
		try {
			Result result = Results.ok();
			result = Results.json().render(user);
			return result;
			
		} catch(Exception EX) {
			logger.error("Could not get/render the current user! {} stack {}", EX.getMessage(), EX);
			return Results.notFound().text().render("User not found: " + EX.getMessage());
		}
	}
}
