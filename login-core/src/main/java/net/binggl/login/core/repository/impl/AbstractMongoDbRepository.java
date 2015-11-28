package net.binggl.login.core.repository.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import net.binggl.login.core.entity.BaseEntity;
import net.binggl.ninja.mongodb.MongoDB;

/**
 * lambda logic which helps to set the values of 
 * a found object
 * @author henrik
 *
 * @param <T> entity extending BaseEntit
 */
@FunctionalInterface
interface EntityHandler<T> {
	void set(T target, T source);
}


/**
 * abstract baseclass for mongodb repositories
 * provide common methods for all child-implementations
 * using generics
 * @author henrik
 *
 * @param <T> entity extending BaseEntity
 */
public abstract class AbstractMongoDbRepository<T extends BaseEntity> {

	 protected MongoDB mongo;
	 
	 protected AbstractMongoDbRepository(MongoDB mongodb) {
		 this.mongo = mongodb;
	 }
	 
	 protected T save(T object, Class<T> clasz, EntityHandler<T> handler) {
	    T entry = object;
    	Datastore ds = this.mongo.getDatastore();
    	
    	if(StringUtils.isEmpty(object.getId())) {
    		ds.save(object);
    	} else {
    		entry = null;
    		// there is an existing entry available - search for it
    		T foundEntry = ds.get(clasz, new ObjectId(object.getId()));
    		if(foundEntry != null) {
    			foundEntry.setModified(new Date());
        		// use a lambda expression to overwrite the values
        		// of the found object
    			handler.set(foundEntry, object);
    			
    			ds.save(foundEntry);
    			entry = foundEntry;
    		}
    	}
		return entry;
    }
}



