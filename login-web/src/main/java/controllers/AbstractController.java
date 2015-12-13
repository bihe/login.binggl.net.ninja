package controllers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.inject.Inject;

import ninja.Result;
import ninja.utils.NinjaProperties;

import static net.binggl.login.common.Constants.CONFIG_BASE_PATH;

/**
 * base class for controllers providing basic logic values, configuration, ...
 * 
 * @author henrik
 */
public class AbstractController {

	private static final String BASE_PATH = "basepath";
	private static final String YEAR = "year";

	@Inject
	private NinjaProperties properties;

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

}
