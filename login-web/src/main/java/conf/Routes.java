package conf;

import controllers.HomeController;
import controllers.api.DashboardController;
import controllers.ninja.ExternalAssetsController;
import net.binggl.ninja.oauth.NinjaOauthController;
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
        
        // dashboard routes
        router.GET().route("/api/user").with(DashboardController.class, "currentUser");
                        
        // static content
        router.GET().route("/assets/{fileName: .*}").with(ExternalAssetsController.class, "serveStatic");
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/.*").with(HomeController.class, "index");
    }

}
