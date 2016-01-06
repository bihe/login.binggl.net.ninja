package net.binggl.login.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.models.Site;
import net.binggl.login.core.models.Site.SiteBuilder;
import net.binggl.login.core.models.User;
import net.binggl.login.core.models.User.UserBuilder;
import net.binggl.login.core.service.LoginService;
import net.binggl.login.core.service.impl.NinjaLoginService;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

	private LoginService loginService;
	
	@Before
	public void setup() {
		this.loginService = new NinjaLoginService(null, null, null, null, null, null);
	}
	
	
	@Test
	public void testRedirectUrl() {
		User user = this.getUserTestData();
		String siteName = "site1";
		String redirectUrl = "http://www.site1.com/a/b/c";
		
		boolean result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertTrue(result);
		
		siteName = "site2";
		redirectUrl = "http://www.site1.com/a/b/c";
		result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertFalse(result);
		
		siteName = "site1";
		redirectUrl = "http://www.site1.com/";
		result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertTrue(result);
		
		siteName = "site1";
		redirectUrl = "http://www.site1.com";
		result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertFalse(result);
		
		user = this.getUserTestData1();
		siteName = "site1";
		redirectUrl = "http://www.site1.com";
		result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertFalse(result);
		
		siteName = "site1";
		redirectUrl = "http://www.site1.com/a/b/";
		result = this.loginService.isValidRedirectUrl(user, siteName, redirectUrl);
		assertTrue(result);
	}
		
	
	
	private User getUserTestData() {
		
		Site site1 = new SiteBuilder()
				.name("site1")
				.url("http://www.site1.com/")
				.permissions(Arrays.asList("permission1", "permission2"))
				.build();
		
		return new UserBuilder()
			.id("ABC")
			.displayName("DisplayName")
			.userName("userName")
			.email("a.b@c.de")
			.sites(Arrays.asList(site1))
			.build();
		
	}
		
	private User getUserTestData1() {
		
		Site site1 = new SiteBuilder()
				.name("site1")
				.url("http://www.site1.com/a")
				.permissions(Arrays.asList("permission1", "permission2"))
				.build();
		
		return new UserBuilder()
			.id("ABC")
			.displayName("DisplayName")
			.userName("userName")
			.email("a.b@c.de")
			.sites(Arrays.asList(site1))
			.build();
		
	}

}
