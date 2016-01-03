package net.binggl.login.core;

import static net.binggl.login.core.Constants.COOKIE_DOMAIN;
import static net.binggl.login.core.Constants.COOKIE_HTTP_ONLY;
import static net.binggl.login.core.Constants.COOKIE_MAXAGE;
import static net.binggl.login.core.Constants.COOKIE_PATH;
import static net.binggl.login.core.Constants.COOKIE_SECURE;
import static net.binggl.login.core.Constants.TOKEN_COOKIE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.models.Site;
import net.binggl.login.core.models.Site.SiteBuilder;
import net.binggl.login.core.models.User;
import net.binggl.login.core.models.User.UserBuilder;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.impl.NinjaJwtTokenService;
import ninja.Context;
import ninja.Cookie;
import ninja.util.TestingNinjaContext;
import ninja.utils.NinjaProperties;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
	
	@Mock
	NinjaProperties properties;
	
	Context context = new TestingNinjaContext();
	TokenService tokenService;
	
	@Before
	public void setup() {
		when(properties.get(TOKEN_COOKIE_NAME)).thenReturn("cookie");
		when(properties.get(COOKIE_DOMAIN)).thenReturn("localhost");
		when(properties.getInteger(COOKIE_MAXAGE)).thenReturn(-1);
		when(properties.get(COOKIE_PATH)).thenReturn("/");
		when(properties.getBoolean(COOKIE_SECURE)).thenReturn(false);
		when(properties.getBoolean(COOKIE_HTTP_ONLY)).thenReturn(true);
		
		tokenService = new NinjaJwtTokenService(properties);
	}
	
	/**
	 * test the creation of a token from a user object
	 */
	@Test
	public void testTokenCreationAndVerification() {
		String token = tokenService.getToken(null, null);
		assertNull(token);
		
		User user = this.getUserTestData();
		token = tokenService.getToken(user, "secret");
		assertNotNull(token);
		
		boolean verify = tokenService.verifyToken(token, "secret");
		assertTrue(verify);
	}
	
	/**
	 * test the save "storage" of a token within a cookie
	 */
	@Test
	public void testSetGetTokenCookie() {
		
		String token = "A_TOKEN";
	
		tokenService.setCookie(context, token);
		List<Cookie> cookies = context.getCookies();
		assertNotNull(cookies);
		assertTrue(cookies.size() > 0);
		Optional<Cookie> cookie = cookies.stream().findFirst();
		assertTrue(cookie.isPresent());
		assertEquals(token, cookie.get().getValue());
		
		String retrievedToken = tokenService.getTokenFromCookie(context);
		assertNotNull(retrievedToken);
		assertEquals(token, retrievedToken);
		
		tokenService.unsetCookie(context);
		cookies = context.getCookies();
		assertNotNull(cookies);
		assertTrue(cookies.size() == 0);
	}
	
	
	private User getUserTestData() {
		
		Site site1 = new SiteBuilder()
				.name("site1")
				.url("http://www.site1.com")
				.permissions(Arrays.asList("permission1", "permission2"))
				.build();
		
		Site site2 = new SiteBuilder()
				.name("site2")
				.url("http://www.site2.com")
				.permissions(Arrays.asList("permission3"))
				.build();
		
		return new UserBuilder()
			.id("ABC")
			.displayName("DisplayName")
			.userName("userName")
			.email("a.b@c.de")
			.sites(Arrays.asList(site1, site2))
			.build();
		
	}

}
