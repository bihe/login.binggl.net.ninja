package net.binggl.login.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import net.binggl.login.core.repository.LoginRepository;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.repository.impl.MongoDbLoginRepository;
import net.binggl.login.core.repository.impl.MongoDbUserRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.SessionService;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.UserService;
import net.binggl.login.core.service.impl.UserServiceImpl;
import net.binggl.login.core.service.impl.NinjaCacheService;
import net.binggl.login.core.service.impl.NinjaJwtTokenService;
import net.binggl.login.core.service.impl.NinjaLoginService;
import net.binggl.login.core.service.impl.NinjaSessionService;

public class CoreModule extends AbstractModule {

	@Override
    protected void configure() {
		 bind(UserRepository.class).to(MongoDbUserRepository.class);
		 bind(LoginRepository.class).to(MongoDbLoginRepository.class);
		 bind(UserService.class).to(UserServiceImpl.class);
		 bind(TokenService.class).to(NinjaJwtTokenService.class);
		 bind(CacheService.class).to(NinjaCacheService.class).in(Scopes.SINGLETON);
		 bind(LoginService.class).to(NinjaLoginService.class);
		 bind(SessionService.class).to(NinjaSessionService.class);
	}
}