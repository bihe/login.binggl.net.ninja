package net.binggl.login.core.service;

import net.binggl.login.core.models.User;

public interface UserService {

	/**
	 * get the user by the unique email
	 * @param email the email of the user
	 * @return a User object
	 */
	User findUserByEmail(String email);
	
	/**
	 * retrieve the user by a given id
	 * @param id the identifier of the user
	 * @return a User object
	 */
	User findUserByAlternativId(String id);
}
