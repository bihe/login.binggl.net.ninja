package filters;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import models.Token;
import net.binggl.ninja.oauth.OauthAuthorizationService;
import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.Results;
import ninja.session.FlashScope;
import ninja.utils.NinjaProperties;
import util.InternationalizationHelper;
import util.TokenHelper;

/**
 * check the loged-in user
 * @author henrik
 */
public class OauthSecurityFilter implements Filter {

	// members
	private static final Logger logger = LoggerFactory
			.getLogger(OauthSecurityFilter.class);
	
	@Inject
	OauthAuthorizationService securityService;

	@Inject
	InternationalizationHelper i18n;
	
	@Inject
	NinjaProperties ninjaProperties;

	@Override
	public Result filter(FilterChain chain, Context context) {

		logger.debug("start: security check.");

		try {

			// check if the session is available
			if (context.getSession() == null) {
				logger.warn("No session available, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "oauth.session.missing"));
			}

			// check for the login-data in the session
			Token t = TokenHelper.getToken(context);
			if (t == null) {
				logger.warn("No token in session, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "oauth.token.missing"));
			}
			
			// check the token checksum
			if(!t.getChecksum().equals(t.getCalculatedChecksum())) {
				logger.warn("The token-checksum does not match, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "oauth.token.checksum.error"));
			}

			// check the token TTL
			DateTime created = new DateTime(t.getTimeStamp());
			long diff = DateTime.now().getMillis() - created.getMillis();
			long ttl = ninjaProperties.getIntegerWithDefault("auth.token.ttl", 3600);
			
			if (diff / 1000 > ttl) {
				logger.warn("The token has expired, show view 403");
				return getNoAccessResult(context, i18n.getMessage(context, "oauth.session.expired"));
			}
			
		} catch (Exception EX) {
			logger.error("Error during security filter check: " + EX.getMessage(), EX);
			return getErrorResult(EX);
		} catch (Throwable e) {
			logger.error("Error during security filter check: " + e.getMessage(), e);
			return getErrorResult(e);
		}

		logger.debug("done: security check!");
		
		return chain.next(context);
	}
	
	
	private Result getNoAccessResult(Context context, String message) {
		Result r = Results.forbidden();
		context.getSession().clear();
		FlashScope flashScope = context.getFlashScope();
		flashScope.error(message);
		r = Results.redirect(ninjaProperties.get("/noaccess"));
		return r;
	}
	
	private Result getErrorResult(Throwable thr) {
		Result result = Results.internalServerError();
		result.render("message", thr.getMessage());
		return result.html().template("/views/system/500error.ftl.html");
	}
	
	
}