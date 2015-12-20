package net.binggl.login.core.repository;

import net.binggl.login.core.entity.User;

public interface UserRepository {
	
    /**
     * get the user defined by the userId
     * @param username the specific username
     * @return user object
     */
    User getUserByName(String username);
    
    /**
     * retrieve the user by the given id
     * @param id user identfier
     * @return user object
     */
    User getUserById(String id);

    /**
     * get the user identified by the email
     * @param email the unique email of the user
     * @return user object
     */
    User getUserByEmail(String email);

    /**
     * save the user
     * @param user the user object
     * @return the user object
     */
    User save(User user);

}
