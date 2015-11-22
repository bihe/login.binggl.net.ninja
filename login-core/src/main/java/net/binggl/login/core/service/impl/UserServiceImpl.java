package net.binggl.login.core.service.impl;

import com.google.inject.Inject;

import net.binggl.login.common.models.User;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.service.UserService;

public class UserServiceImpl implements UserService {

	@Inject private UserRepository userRepo;
	
	/**
	 * find the user by the given email
	 * @param email
	 * @return user object
	 */
	public User findUserByEmail(String email) {
		net.binggl.login.core.entity.User entityUser = userRepo.getUserByEmail(email);
		if(entityUser != null) {
			User user = new User(entityUser.getEmail(),
					entityUser.getDisplayName(),
					entityUser.getUserName());
			return user;
		}
		
		return null;
	}
}
