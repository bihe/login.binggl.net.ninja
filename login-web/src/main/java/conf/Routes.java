package conf;

import controllers.HomeController;
import controllers.api.DashboardController;
import controllers.flow.AuthenticationController;
import controllers.ninja.ExternalAssetsController;
import net.binggl.ninja.oauth.NinjaOauthController;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
    	// authentication routes
        router.GET().route("/startauth").with(NinjaOauthController.class, "startauth");
        router.GET().route("/oauth2callback").with(NinjaOauthController.class, "oauth2callback");
    	router.GET().route("/login").with(HomeController.class, "login");
    	router.GET().route("/logout").with(HomeController.class, "logout");
        
        router.GET().route("/api/user").with(DashboardController.class, "currentUser");
        router.GET().route("/auth/flow").with(AuthenticationController.class, "startFlow");
        
                        
        // static content
        router.GET().route("/static/{fileName: .*}").with(AssetsController.class, "serveStatic");
        router.GET().route("/ui/{fileName: .*}").with(ExternalAssetsController.class, "serveStatic");
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        
        router.GET().route("/403*").with(HomeController.class, "show403");
        router.GET().route("/404*").with(HomeController.class, "show404");
        
        router.GET().route("/.*").with(HomeController.class, "index");
    }

}
