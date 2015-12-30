package net.binggl.login.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.entity.UserSite;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.service.UserService;
import net.binggl.login.core.service.impl.UserServiceImpl;
import ninja.Context;
import ninja.util.TestingNinjaContext;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@Mock
	UserRepository userRepo;
	
	Context context = new TestingNinjaContext();
	UserService userService;
	
	private final String EMAIL = "test@example.com";
	private final String ID = User.newObjectId();
	
	@Before
	public void setup() {
		
		when(userRepo.getUserByEmail(EMAIL)).thenReturn(getUserTestData());
		
		userService = new UserServiceImpl(userRepo);
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
		
		String permission1 = "site1|permission1;permission2";
		String permission2 = "site2|permission3";
		
		assertEquals(permission1, user.getSitePermissions().get(0));
		assertEquals(permission2, user.getSitePermissions().get(1));
	}
	
	
	protected User getUserTestData() {
		User user = new User();
		user.setCreated(new Date());
		user.setDisplayName("Test User");
		user.setEmail(EMAIL);
		user.setId(ID);
		user.setModified(new Date());
		user.setUserName("username");
		
		List<UserSite> sites = new ArrayList<>();
		
		UserSite site1 = new UserSite();
		site1.setName("site1");
		site1.setUrl("http://www.example.com/1");
		List<String> permissions = new ArrayList<>();
		permissions.add("permission1");
		permissions.add("permission2");
		site1.setPermissions(permissions);
		
		sites.add(site1);
		
		UserSite site2 = new UserSite();
		site2.setName("site2");
		site2.setUrl("http://www.example.com/2");
		permissions = new ArrayList<>();
		permissions.add("permission3");
		site2.setPermissions(permissions);
		
		sites.add(site2);
		
		user.setSites(sites);
		
		return user;
	}
}
