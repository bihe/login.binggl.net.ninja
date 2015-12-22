package controllers;

import static net.binggl.login.core.Constants.CONFIG_BASE_PATH;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.inject.Inject;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.repository.UserRepository;
import ninja.Context;
import ninja.Result;
import ninja.cache.NinjaCache;
import ninja.utils.NinjaProperties;

/**
 * base class for controllers providing basic logic values, configuration, ...
 * 
 * @author henrik
 */
public class AbstractController {

	private final static String USER_CACHE = "cache.user";
	
	private static final String BASE_PATH = "basepath";
	private static final String YEAR = "year";

	@Inject
	private NinjaProperties properties;
	@Inject
	private UserRepository userRepo;
	@Inject 
	NinjaCache ninjaCache;

	private final Map<String, Object> getBaseRenderObjects() {
		Map<String, Object> renderObjects = new HashMap<>();
		renderObjects.put(BASE_PATH, this.getBasePath());
		renderObjects.put(YEAR, new DateTime().getYear());
		return renderObjects;
	}

	protected final String getBasePath() {
		String basePath = properties.get(CONFIG_BASE_PATH);
		return basePath;
	}
	
	protected Result processTemplateResult(Result result) {
		Result r = result;
		r.render(this.getBaseRenderObjects());
		return result;
	}
	
	protected User getUserById(String id) {
		User user = null;
		user = ninjaCache.get(USER_CACHE + "_" + id, User.class);
		if(user == null) {
			user = userRepo.getUserById(id);
			if(user != null) {
				ninjaCache.add(USER_CACHE + "_" + id, user);
			}
		}
		return user;
	}
	
	protected String getBaseUrl(Context context) {
		return String.format("%s://%s", context.getScheme(), context.getHostname());
	}

}
