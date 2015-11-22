package net.binggl.login.core.service;

import net.binggl.login.common.models.User;

public interface UserService {

	/**
	 * get the user by the unique email
	 * @param email the email of the user
	 * @return a User object
	 */
	User findUserByEmail(String email);
}
