package net.binggl.login.core;

import static net.binggl.login.core.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.models.User;
import net.binggl.login.core.service.TokenService;
import net.binggl.login.core.service.impl.JwtTokenService;
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
		
		tokenService = new JwtTokenService(properties);
	}
	
	/**
	 * test the creation of a token from a user object
	 */
	@Test
	public void testTokenCreationAndVerification() {
		String token = tokenService.getToken(null, null);
		assertNull(token);
		
		User user = new User("a.b@c.de", "Name", "id", "UserName");
		List<String> permissions = new ArrayList<String>();
		permissions.add("site1|permission1");
		permissions.add("site2|permission1;permission3");
		user.setSitePermissions(permissions);;
		token = tokenService.getToken(user, "secret");
		assertNotNull(token);
		// roundtrip!
		User verify = tokenService.verifyToken(token, "secret");
		assertNotNull(verify);
		assertEquals(user, verify);
		assertEquals(2, user.getSitePermissions().size());
		assertEquals("site1|permission1", user.getSitePermissions().get(0));
		assertEquals("site2|permission1;permission3", user.getSitePermissions().get(1));
		
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
	}

}
