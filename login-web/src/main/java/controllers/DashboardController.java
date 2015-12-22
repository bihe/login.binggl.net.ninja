package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import extractors.AuthenticatedUser;
import filters.SecurityFilter;
import net.binggl.login.core.models.User;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;

/**
 * manages the application dashboard
 * @author henrik
 */
@Singleton
@FilterWith(SecurityFilter.class)
public class DashboardController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	public Result list(Context context, @AuthenticatedUser User user) {
		Result result = Results.ok();
		net.binggl.login.core.entity.User lookupUser = this.getUserById(user.getId());
		
		if(lookupUser == null) {
			logger.debug("Could not get the user by the given id {}", user.getId());
			return Results.notFound();
		}
		result = Results.json().render(lookupUser);
		
		return result;
	}
	
}
