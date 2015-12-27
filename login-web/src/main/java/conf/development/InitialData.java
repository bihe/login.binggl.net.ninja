package conf.development;

import java.util.Date;

import com.google.inject.Inject;

import net.binggl.login.core.entity.User;
import net.binggl.login.core.repository.UserRepository;
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
    	u.setDisplayName("Henrik Binggl");
    	u.setEmail("henrik@binggl.net");
    	u.setUserName("bihe");
    	u.setCreated(new Date());
    	
    	this.userRepo.save(u);
	}
}
