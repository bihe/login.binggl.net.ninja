package net.binggl.login.core.repository.impl;

import org.mongodb.morphia.Datastore;

import com.google.inject.Inject;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.ninja.mongodb.MongoDB;

public class MongoDbUserRepository  implements UserRepository {

    private MongoDB mongo;

    /**
     * constructor with dpendency injection
     * @param mongodb the mongo backend object
     */
    @Inject
    public MongoDbUserRepository(MongoDB mongodb) {
        this.mongo = mongodb;
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
    public void save(User user) {
        Datastore ds = this.mongo.getDatastore();
        ds.save(user);
    }
}
