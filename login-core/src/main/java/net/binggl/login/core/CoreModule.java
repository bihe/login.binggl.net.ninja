package net.binggl.login.core;

import com.google.inject.AbstractModule;

import net.binggl.login.core.repository.SiteRepository;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.repository.impl.MongoDbSiteRepository;
import net.binggl.login.core.repository.impl.MongoDbUserRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import net.binggl.login.core.service.impl.JwtTokenService;
import net.binggl.login.core.service.impl.NinjaCacheService;
import net.binggl.login.core.service.impl.CacheAwareUserService;

public class CoreModule extends AbstractModule {

	@Override
    protected void configure() {
		 bind(UserRepository.class).to(MongoDbUserRepository.class);
		 bind(SiteRepository.class).to(MongoDbSiteRepository.class);
		 bind(UserService.class).to(CacheAwareUserService.class);
		 bind(TokenService.class).to(JwtTokenService.class);
		 bind(CacheService.class).to(NinjaCacheService.class);
	}
}