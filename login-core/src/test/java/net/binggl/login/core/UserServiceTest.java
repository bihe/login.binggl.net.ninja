package net.binggl.login.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.entity.UserSite;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.UserService;
import net.binggl.login.core.service.impl.CacheAwareUserService;
import ninja.Context;
import ninja.util.TestingNinjaContext;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock
	UserRepository userRepo;
	@Mock
	CacheService cacheService;
	
	
	Context context = new TestingNinjaContext();
	UserService userService;
	
	private final String EMAIL = "test@example.com";
	private final String ID = User.newObjectId();
	
	@Before
	public void setup() {
		
		when(userRepo.getUserByEmail(EMAIL)).thenReturn(getUserTestData());
		
		// do not test the caching-service !!
		doAnswer(invocation -> {
			return null;
		}).when(cacheService).put(EMAIL, getUserTestData());
		doAnswer(invocation -> {
			return null;
		}).when(cacheService).get(EMAIL, net.binggl.login.core.models.User.class);
		
		userService = new CacheAwareUserService(userRepo, cacheService);
	}
	
	/**
	 * test the correct structure of a user-object with the correct permissions
	 */
	@Test
	public void testUserServiceObject() {
		net.binggl.login.core.models.User user = userService.findUserByEmail("1@example.com");
		assertNull(user);
		user = userService.findUserByEmail("test@example.com");
		assertNotNull(user);
		
		assertEquals("test@example.com", user.getEmail());
		assertEquals("Test User", user.getDisplayName());
		assertEquals(ID, user.getId());
		assertEquals(2, user.getSitePermissions().size());
		
		assertEquals("site1", user.getSitePermissions().get(0).getName());
		assertEquals("http://www.example.com/1", user.getSitePermissions().get(0).getUrl());
		assertEquals("permission1,permission2", StringUtils.join(user.getSitePermissions().get(0).getPermissions(), ","));
		
		assertEquals("site2", user.getSitePermissions().get(1).getName());
		assertEquals("http://www.example.com/2", user.getSitePermissions().get(1).getUrl());
		assertEquals("permission3", StringUtils.join(user.getSitePermissions().get(1).getPermissions(), ","));
	}
	
	
	protected User getUserTestData() {
		User user = new User();
		user.setCreated(new Date());
		user.setDisplayName("Test User");
		user.setEmail(EMAIL);
		user.setAlternativeId(ID);
		user.setModified(new Date());
		user.setUserName("username");
		
		List<UserSite> sites = new ArrayList<>();
		
		UserSite site1 = new UserSite();
		site1.setName("site1");
		site1.setUrl("http://www.example.com/1");
		site1.setPermissions(Arrays.asList("permission1", "permission2"));
		
		sites.add(site1);
		
		UserSite site2 = new UserSite();
		site2.setName("site2");
		site2.setUrl("http://www.example.com/2");
		site2.setPermissions(Arrays.asList("permission3"));
		
		sites.add(site2);
		
		user.setSites(sites);
		
		return user;
	}
}
