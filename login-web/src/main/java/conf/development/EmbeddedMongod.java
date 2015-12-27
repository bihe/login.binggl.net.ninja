package conf.development;

import static net.binggl.login.core.util.ExceptionHelper.wrap;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongod {

	private final MongodStarter starter = MongodStarter.getDefaultInstance();
	private MongodExecutable mongodExe;
	private MongodProcess mongod;
	
	private static final int PORT = 27017;
	
	
	public EmbeddedMongod() {
	}
	
	public void startup() {
		
		wrap(() -> {
			IMongodConfig mongodConfig = new MongodConfigBuilder()
	    	        .version(Version.Main.PRODUCTION)
	    	        .net(new Net(PORT, Network.localhostIsIPv6()))
	    	        .build();
	    	
	        mongodExe = starter.prepare(mongodConfig);
	        mongod = mongodExe.start();
		});
	}
	
	public void shutdown() {
		wrap(() -> {
			mongod.stop();
	        mongodExe.stop();
		});
	}
	
}
