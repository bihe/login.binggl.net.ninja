package net.binggl.login.core.repository;

import net.binggl.login.core.entity.Login;

public interface LoginRepository {

	/**
	 * save a new login entry
	 * @param entry
	 * @return
	 */
	Login save(Login entry);
}
