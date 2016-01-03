package conf.development;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.entity.UserSite;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.login.core.util.HashHelper;
import net.binggl.ninja.mongodb.MongoDB;

public class InitialData {

	private UserRepository userRepo;
	private MongoDB mongodb;
	
	@Inject
	public InitialData(MongoDB mongodb, UserRepository userRepo) {
		this.mongodb = mongodb;
		this.userRepo = userRepo;
	}
	
	public void init(String databaseName, String ... collections) {
		this.mongodb.getMongoClient().dropDatabase(databaseName);
		for(String collection : collections) {
			this.mongodb.getMongoClient().getDatabase(databaseName).createCollection(collection);	
		}	
	}
	
	public void setupData() {
		User u = new User();
		u.setAlternativeId(HashHelper.getSHA("TestUser"));
    	u.setDisplayName("Henrik Binggl");
    	u.setEmail("henrik@binggl.net");
    	u.setUserName("bihe");
    	u.setCreated(new Date());
    	
    	List<UserSite> sites = new ArrayList<>();
		
		UserSite site1 = new UserSite();
		site1.setName("site1");
		site1.setUrl("http://www.example.com/1");
		site1.setPermissions(Arrays.asList("permission1", "permission2"));
		
		sites.add(site1);
		
		UserSite site2 = new UserSite();
		site2.setName("site2");
		site2.setUrl("http://www.example.com/2");
		site2.setPermissions(Arrays.asList("permission3"));
		
		sites.add(site2);
		
		u.setSites(sites);
    	
    	
    	this.userRepo.save(u);
	}
}
