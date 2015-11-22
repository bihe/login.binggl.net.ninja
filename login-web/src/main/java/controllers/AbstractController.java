package controllers;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import ninja.Result;
import ninja.utils.NinjaProperties;

/**
 * base class for controllers providing basic logic
 * values, configuration, ...
 * @author henrik
 */
public class AbstractController {

	private static final String CONFIG_BASE_PATH = "application.basepath";
	private static final String BASE_PATH = "basepath";
	
	@Inject private NinjaProperties properties;
	
	
	private final String getBasePath() {
		String basePath = properties.get(CONFIG_BASE_PATH);
		return basePath;
	}
	
	private final Map<String,Object> getBaseRenderObjects() {
		Map<String,Object> renderObjects = new HashMap<>();
		renderObjects.put(BASE_PATH, this.getBasePath());
		return renderObjects;
	}
	
	protected Result processTemplateResult(Result result) {
		Result r = result;
		r.render(this.getBaseRenderObjects());
		return result;
	}
	
}
