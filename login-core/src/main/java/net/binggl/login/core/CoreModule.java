package net.binggl.login.core;

import com.google.inject.AbstractModule;

import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.repository.impl.MongoDbUserRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import net.binggl.login.core.service.impl.CacheAwareUserService;
import net.binggl.login.core.service.impl.NinjaJwtTokenService;
import net.binggl.login.core.service.impl.NinjaCacheService;

public class CoreModule extends AbstractModule {

	@Override
    protected void configure() {
		 bind(UserRepository.class).to(MongoDbUserRepository.class);
		 bind(UserService.class).to(CacheAwareUserService.class);
		 bind(TokenService.class).to(NinjaJwtTokenService.class);
		 bind(CacheService.class).to(NinjaCacheService.class);
	}
}