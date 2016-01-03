package net.binggl.login.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.entity.UserSite;
import net.binggl.login.core.models.Site;
import net.binggl.login.core.models.Site.SiteBuilder;
import net.binggl.login.core.models.User;
import net.binggl.login.core.models.User.UserBuilder;
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
	
	@Override
	public User findeUserByAlternativId(String alternativeId) {
		User user = this.cacheService.get(PREFIX + alternativeId, User.class);
		if(user == null) {
			logger.debug("Cache miss, use repository to query user!");
			
			net.binggl.login.core.entity.User entityUser = userRepo.getUserByAlternativeId(alternativeId);
			if(entityUser != null) {
				user = fromEntityUser(entityUser);
				this.cacheService.put(PREFIX + alternativeId, user);
				return user;
			}
			
		}
		return user;
	}
	
	@Override
	public User findUserByEmail(String email) {
		logger.debug("Find a user by the given email {}", email);
		
		User user = this.cacheService.get(PREFIX + email, User.class);
		if(user == null) {
			logger.debug("Cache miss, use repository to query user!");
		
			net.binggl.login.core.entity.User entityUser = userRepo.getUserByEmail(email);
			if(entityUser != null) {
				user = fromEntityUser(entityUser);
				this.cacheService.put(PREFIX + email, user);
				return user;
			}
		}
		
		return user;
	}
	
	
	
	
	
	protected User fromEntityUser(net.binggl.login.core.entity.User entityUser) {
		List<Site> sites = new ArrayList<>();
		if(entityUser.getSites() != null) {
			for(UserSite entitySite : entityUser.getSites()) {
				sites.add(
					new SiteBuilder()
						.name(entitySite.getName())
						.url(entitySite.getUrl())
						.permissions(entitySite.getPermissions())
						.build());
			}
		}
		
		User user = new UserBuilder()
				.id(entityUser.getAlternativeId()) // do not use the internal database id to identify the user!
				.displayName(entityUser.getDisplayName())
				.userName(entityUser.getUserName())
				.email(entityUser.getEmail())
				.sites(sites)
				.build();
		
		return user;
	}
}
