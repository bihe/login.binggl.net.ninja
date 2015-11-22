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

import controllers.HomeController;
import controllers.SecurityAwareAssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

import net.binggl.ninja.oauth.NinjaOauthController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
    	// authentication routes
        router.GET().route("/login").with(NinjaOauthController.class, "startauth");
        router.GET().route("/oauth2callback").with(NinjaOauthController.class, "oauth2callback");
    	
        
        
        // static content
        router.GET().route("/assets/{fileName: .*}").with(SecurityAwareAssetsController.class, "serveStatic");
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/.*").with(HomeController.class, "index");
    }

}
