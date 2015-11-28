package net.binggl.login.core.repository;

import java.util.List;

import net.binggl.login.core.entity.Site;

public interface SiteRepository {

	/**
	 * get a list of all available sites
	 * @return list of Site objects
	 */
	List<Site> getAll();
	
	/**
	 * get a list of permissions for a given site
	 * @param name the name of the site
	 * @return a list of permissions
	 */
	List<String> getPermissionsForSite(String name);
	
	/**
	 * save a site object
	 * @param site the site object
	 * @return a site object
	 */
	Site save(Site site);
	
}
