package net.binggl.login.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.entity.UserSite;
import net.binggl.login.core.models.User;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.UserService;

public class CacheAwareUserService implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(CacheAwareUserService.class);
	
	private static final String PREFIX = "Cache.User.";
	
	private UserRepository userRepo;
	private CacheService cacheService;
	
	@Inject
	public CacheAwareUserService(UserRepository userRepo, CacheService cache) {
		this.userRepo = userRepo;
		this.cacheService = cache;
	}
	
	/**
	 * find the user by the given email
	 * @param email
	 * @return user object
	 */
	public User findUserByEmail(String email) {
		logger.debug("Find a user by the given email {}", email);
		
		User user = this.cacheService.get(PREFIX + email, User.class);
		if(user == null) {
			logger.debug("Cache miss, use repository to query user!");
		
			net.binggl.login.core.entity.User entityUser = userRepo.getUserByEmail(email);
			if(entityUser != null) {
				
				// format the sites and site-permissions in a "String-way"
				List<String> permissions = new ArrayList<>();
				if(entityUser.getSites() != null) {
					for(UserSite site : entityUser.getSites()) {
						permissions.add(this.formatSitePermissions(site));
					}
				}
				
				user = new User(entityUser.getEmail(),
						entityUser.getDisplayName(),
						entityUser.getId(),
						entityUser.getUserName());
				user.setSitePermissions(permissions);
				
				this.cacheService.put(PREFIX + email, user);
				
				return user;
			}
		}
		
		return user;
	}
	
	
	/**
	 * format the sites/permissions
	 * site:permission1;permission2
	 * @param site
	 * @return
	 */
	protected String formatSitePermissions(UserSite site) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(site.getName());
		buffer.append("|");
		int i=0;
		for(String permission : site.getPermissions()) {
			if(i>0)
				buffer.append(";");
			buffer.append(permission);
			i++;
		}
		return buffer.toString();
	}
}
