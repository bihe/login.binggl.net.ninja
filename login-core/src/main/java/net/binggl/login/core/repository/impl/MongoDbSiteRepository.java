package net.binggl.login.core.repository.impl;

import java.util.List;

import org.mongodb.morphia.Datastore;

import com.google.inject.Inject;

import net.binggl.login.core.entity.Site;
import net.binggl.login.core.repository.SiteRepository;
import net.binggl.ninja.mongodb.MongoDB;

public class MongoDbSiteRepository extends AbstractMongoDbRepository<Site> implements SiteRepository {

	/**
     * constructor with dpendency injection
     * @param mongodb the mongo backend object
     */
    @Inject
    public MongoDbSiteRepository(MongoDB mongodb) {
    	super(mongodb);
    }
	
	@Override
	public List<Site> getAll() {
		Datastore ds = this.mongo.getDatastore();
		return ds.find(Site.class).asList();
	}

	@Override
	public List<String> getPermissionsForSite(String name) {
		Datastore ds = this.mongo.getDatastore();
		List<String> permissions = 
				ds.createQuery(Site.class).field("name").equal(name).get().getPermissions();
		return permissions;
	}
	
	@Override
    public Site save(Site site) {
		Site s = this.save(site, Site.class, (Site target, Site source) -> {
			target.setName(source.getName());
			target.setUrl(source.getUrl());
			target.setPermissions(source.getPermissions());
		});
		return s;
    }

}
