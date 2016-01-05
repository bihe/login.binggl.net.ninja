package controllers;

import static net.binggl.login.core.Constants.CONFIG_BASE_PATH;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.session.FlashScope;
import ninja.utils.NinjaProperties;
import util.InternationalizationHelper;

/**
 * base class for controllers providing basic logic values, configuration, ...
 * 
 * @author henrik
 */
public abstract class AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);
    
	private static final String BASE_PATH = "basepath";
	private static final String YEAR = "year";

	@Inject	protected NinjaProperties properties;
	@Inject protected InternationalizationHelper i18n;
	
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
	
	
	protected Result getNoAccessResult(Context context) {
		logger.warn("No user available, show login view!");
		return getNoAccessResult(context, i18n.getMessage(context, "auth.user.invalid"));
	}
	
	private Result getNoAccessResult(Context context, String message) {
		Result r = Results.forbidden();
		context.getSession().clear();
		FlashScope flashScope = context.getFlashScope();
		flashScope.error(message);
		r = Results.redirect(properties.get(CONFIG_BASE_PATH) + "login");
		return r;
	}

}
