package controllers;

import static net.binggl.login.core.Constants.CONFIG_BASE_PATH;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.inject.Inject;

import ninja.Context;
import ninja.Result;
import ninja.utils.NinjaProperties;

/**
 * base class for controllers providing basic logic values, configuration, ...
 * 
 * @author henrik
 */
public abstract class AbstractController {

	private static final String BASE_PATH = "basepath";
	private static final String YEAR = "year";

	@Inject	private NinjaProperties properties;
	
	
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
	
	protected String getBaseUrl(Context context) {
		return String.format("%s://%s", context.getScheme(), context.getHostname());
	}

}
