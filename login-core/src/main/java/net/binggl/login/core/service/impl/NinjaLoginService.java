package net.binggl.login.core.service.impl;

import static net.binggl.login.core.Constants.AUTH_TOKEN_SECRET;
import static net.binggl.login.core.util.ExceptionHelper.logEx;
import static net.binggl.login.core.util.ExceptionHelper.wrapEx;

import java.net.URL;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import net.binggl.login.core.entity.Login;
import net.binggl.login.core.models.Site;
import net.binggl.login.core.models.Token;
import net.binggl.login.core.models.User;
import net.binggl.login.core.repository.LoginRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.SessionService;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import ninja.Context;
import ninja.session.FlashScope;
import ninja.utils.NinjaProperties;

/**
 * Login service implementation using infrastructure of the NinjaFramework
 * @author henrik
 */
public class NinjaLoginService implements LoginService {

	private static final Logger logger = LoggerFactory.getLogger(NinjaLoginService.class);
	private static final String CACHE_PREFIX = "Cache.Login.User.";
	
	private UserService userService;
	private TokenService tokenService;
	private NinjaProperties properties;
	private CacheService cache;
	private LoginRepository loginRepo;
	private SessionService sessionService;
	
	@Inject
	public NinjaLoginService(UserService userService, TokenService tokenService, 
			NinjaProperties properties, CacheService cache, LoginRepository loginRepo,
			SessionService sessionService) {
		this.userService = userService;
		this.tokenService = tokenService;
		this.properties = properties;
		this.cache = cache;
		this.loginRepo = loginRepo;
		this.sessionService = sessionService;
	}
	
	@Override
	public boolean login(User user, Context context) {
		return wrapEx(() -> {
			if (user != null) {
				String token = tokenService.getToken(user, properties.getOrDie(AUTH_TOKEN_SECRET));
				logger.debug("Got a user {}  create a token and save the token {}", user, token);
	
				if (StringUtils.isNotEmpty(token)) {
					tokenService.setCookie(context, token);
					this.sessionService.setUserId(context, user.getId());
					
					String loginType = this.sessionService.getLoginType(context);
					
					Login login = new Login();
					login.setUserId(user.getId());
					login.setUserName(user.getUserName());
					login.setType(loginType);
					this.loginRepo.save(login);
					
					return true;
				}
				logger.warn("Could not create a token for user {}", user);
			}
			return false;
		});
	}

	@Override
	public boolean logout(User user, Context context) {
		return logEx(() -> {
			
			this.tokenService.unsetCookie(context);
        	this.cache.clear(this.getCacheKey(CACHE_PREFIX, context));
        	context.getSession().clear();
			
    		FlashScope flashScope = context.getFlashScope();
    		if(user == null) {
        		flashScope.error("No user available to logout!");
            } else {   
	        	flashScope.success("User " + user.getDisplayName() + " loged out!");
	        	return true;
            }
    		return false;
    	});
	}

	@Override
	public synchronized User materializeUser(Context context) {
		User foundUser = null;
		foundUser = this.cache.get(this.getCacheKey(CACHE_PREFIX, context), User.class);
		
		if(foundUser == null) {
			logger.debug("User not found in cache - start lookup!");
			
			foundUser = logEx(() -> {
				User user = null;
						
				// 1. try it with a session id
				String userId = this.sessionService.getUserId(context);
				if(!StringUtils.isEmpty(userId)) {
					logger.debug("Try to find user by id {}", userId);
					user = this.userService.findeUserByAlternativId(userId);
				}
				
				// 2. get the data from the token
				Token token = this.getTokenFromContext(context);
				if(token == null) {
					if(user != null) {
						logger.warn("No token available for loged-in user {}/{}!", user.getDisplayName(), user.getId());
					}
					return null;
				}
				
				if(user != null) {
					return user;
				} else {
					
					// 3. load the user with the given id
					userId = token.getUserId();
					logger.debug("Try to find user by id {}", userId);
					user = this.userService.findeUserByAlternativId(userId);
				}
				
			    return user;
			});
			
			if(foundUser != null) {
				this.cache.put(this.getCacheKey(CACHE_PREFIX, context), foundUser);
				logger.debug("Found user-object, store it in cache {}", foundUser);
			}
		}
		
		return foundUser;
	}
	
	@Override
	public boolean isValidRedirectUrl(User user, String siteName, String redirectUrl) {
		Boolean result = false;
		result = logEx(() -> {

			logger.debug("Find the site {} and check the redirect-url {}", siteName, redirectUrl);
			
			// find the permissions of the given user
			Optional<Site> entry = user.getSitePermissions().stream().filter(s -> siteName.equals(s.getName())).findFirst();
			if(entry.isPresent()) {
				// match the given URL
				// the redirect-URL must start with the url defined for the given site
				URL site = new URL(entry.get().getUrl());
				URL redirect = new URL(redirectUrl);
				
				if(site.getProtocol().equals(redirect.getProtocol())
						&& site.getHost().equals(redirect.getHost())
						&& site.getPort() == redirect.getPort()) {
					
					logger.debug("Matching of url succeeded for protocol/host/port site: {}, redirect: {}", site, redirect);
					
					// specifically check the path
					String sitePath = site.getPath();
					if(StringUtils.isEmpty(sitePath)) {
						return true;
					}
					String redirectPath = redirect.getPath();
					
					logger.debug("The redirect url starts with the same path as the site-url. site: {}, redirect: {}", sitePath, redirectPath);
					
					if(redirectPath.startsWith(sitePath)) {
						return true;
					}
				}
						
			}
			
			logger.debug("Could not find a site with the given name or the redirect url did not match!");
			
			return false;
		});
		return result;
	}
	
	
	

	protected Token getTokenFromContext(Context context) {
		Token tokenObject = null;
		
		String token = tokenService.getTokenFromCookie(context);
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		logger.debug("Got a token from the context! " + token);
		
        tokenObject = tokenService.verifyToken(token, properties.getOrDie(AUTH_TOKEN_SECRET));
        return tokenObject;
	}
	
	protected String getCacheKey(String prefix, Context context) {
		return prefix + context.getSession().getId();
	}
}
