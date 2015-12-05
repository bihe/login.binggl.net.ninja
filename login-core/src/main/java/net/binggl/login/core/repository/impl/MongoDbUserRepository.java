package net.binggl.login.core.repository.impl;

import org.mongodb.morphia.Datastore;

import com.google.inject.Inject;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.ninja.mongodb.MongoDB;

public class MongoDbUserRepository extends AbstractMongoDbRepository<User> implements UserRepository {

	/**
	 * constructor with dpendency injection
	 * 
	 * @param mongodb
	 *            the mongo backend object
	 */
	@Inject
	public MongoDbUserRepository(MongoDB mongodb) {
		super(mongodb);
	}

	@Override
	public User getUserByName(String username) {
		Datastore ds = this.mongo.getDatastore();
		User user = ds.createQuery(User.class).field("userName").equal(username).get();
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		Datastore ds = this.mongo.getDatastore();
		User user = ds.createQuery(User.class).field("email").equal(email).get();
		return user;
	}

	@Override
	public User save(User user) {
		User u = this.save(user, User.class, (User source, User target) -> {
			target.setDisplayName(source.getDisplayName());
			target.setEmail(source.getEmail());
			target.setUserName(source.getUserName());
			target.setSites(source.getSites());
		});
		return u;
	}
}
