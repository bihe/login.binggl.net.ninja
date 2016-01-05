package net.binggl.login.core.repository.impl;

import com.google.inject.Inject;

import net.binggl.login.core.entity.Login;
import net.binggl.login.core.repository.LoginRepository;
import net.binggl.ninja.mongodb.MongoDB;

public class MongoDbLoginRepository extends AbstractMongoDbRepository<Login> implements LoginRepository {

	@Inject
	protected MongoDbLoginRepository(MongoDB mongodb) {
		super(mongodb); 
	}

	@Override
	public Login save(Login entry) {
		Login l = this.save(entry, Login.class, (source, target) -> {
			target.setType(source.getType());
			target.setUserId(source.getUserId());
			target.setUserName(source.getUserName());
		});
		return l;
	}
}
