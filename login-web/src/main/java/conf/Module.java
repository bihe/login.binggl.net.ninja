/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package conf;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import conf.development.InitialData;
import net.binggl.login.core.CoreModule;
import net.binggl.ninja.oauth.NinjaOauthModule;
import net.binggl.ninja.oauth.OauthAuthorizationService;
import service.OauthTokenAuthorizationService;

@Singleton
public class Module extends AbstractModule {

	@Override
    protected void configure() {

        install(new NinjaOauthModule());
        install(new CoreModule());

        // startup
        bind(CustomObjectMapper.class);

        // bind a specific authorization implementation
        bind(OauthAuthorizationService.class).to(OauthTokenAuthorizationService.class);
        
        bind(InitialData.class).in(Scopes.SINGLETON);
    }
}
