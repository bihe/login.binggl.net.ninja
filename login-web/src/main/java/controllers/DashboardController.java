package controllers;

import com.google.inject.Singleton;

import filters.SecurityFilter;
import ninja.FilterWith;

/**
 * manages the application dashboard
 * @author henrik
 */
@Singleton
@FilterWith(SecurityFilter.class)
public class DashboardController extends AbstractController {
}
