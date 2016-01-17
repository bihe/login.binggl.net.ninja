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

public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private UserRepository userRepo;
	
	@Inject
	public UserServiceImpl(UserRepository userRepo, CacheService cache) {
		this.userRepo = userRepo;
	}
	
	@Override
	public User findUserByAlternativId(String alternativeId) {
		logger.debug("Find user by altId {}", alternativeId);
		net.binggl.login.core.entity.User entityUser = userRepo.getUserByAlternativeId(alternativeId);
		if(entityUser != null) {
			User user = fromEntityUser(entityUser);
			return user;
		}
		return null;
	}
	
	@Override
	public User findUserByEmail(String email) {
		logger.debug("Find user by email {}", email);
		net.binggl.login.core.entity.User entityUser = userRepo.getUserByEmail(email);
		if(entityUser != null) {
			User user = fromEntityUser(entityUser);
			return user;
		}
		return null;
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
