package net.binggl.login.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import net.binggl.login.core.entity.User;
import net.binggl.login.core.repository.UserRepository;
import net.binggl.ninja.mongodb.MongoDB;
import ninja.NinjaTest;

public class MongoDBIntegrationTests extends NinjaTest {

	private static MongoDB mongoDB;
	private static final MongodStarter starter = MongodStarter.getDefaultInstance();
	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	
	private static UserRepository userRepo;
	
	@BeforeClass
    public static void init() throws UnknownHostException, IOException {
    	
    	IMongodConfig mongodConfig = new MongodConfigBuilder()
    	        .version(Version.Main.PRODUCTION)
    	        .net(new Net(29019, Network.localhostIsIPv6()))
    	        .build();
    	
        mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
    }
    
    @AfterClass
    public static void shutdown() {
		mongod.stop();
        mongodExe.stop();	
    }
    
    @Before
    public void setup() throws Exception{
    	mongoDB = getInjector().getInstance(MongoDB.class);
    	mongoDB.deleteAll(User.class);
    	
    	userRepo = getInjector().getInstance(UserRepository.class);
    }
    
    @Test
    public void testInjector() {
    	assertNotNull(userRepo);
    }
    
    @Test
    public void saveUserTest() {
    	User u = new User();
    	u.setDisplayName("displayName");
    	u.setEmail("email");
    	u.setUserName("username");
    	
    	User saved = userRepo.save(u);
    	assertNotNull(saved);
    	assertEquals("email", saved.getEmail());
    	assertEquals("displayName", saved.getDisplayName());
    	assertEquals("username", saved.getUserName());
    	assertNotNull(saved.getId());

    	assertTrue(saved.getCreated().before(new Date()));
    	saved.setDisplayName("displayName1");
    	saved = userRepo.save(saved);
    	assertEquals("displayName1", saved.getDisplayName());
    	
    	assertTrue(saved.getCreated().before(saved.getModified()));
    } 
}