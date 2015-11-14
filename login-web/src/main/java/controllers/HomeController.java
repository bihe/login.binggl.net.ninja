package controllers;

import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.utils.NinjaProperties;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class HomeController {
	
	@Inject
	NinjaProperties ninjaProperties;
	
	//@FilterWith(SecurityFilter.class)
    public Result index(Context context) {
    	return Results.html();
    } 
}
 