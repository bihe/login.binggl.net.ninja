package conf;

import static net.binggl.login.core.Constants.APPLICATION_MODE;
import static net.binggl.login.core.Constants.MODE_DEVELOPMENT;

import com.google.inject.Inject;

import conf.development.EmbeddedMongod;
import conf.development.InitialData;
import net.binggl.ninja.mongodb.MongoDB;
import ninja.NinjaDefault;
import ninja.utils.NinjaProperties;

public class Ninja extends NinjaDefault implements ninja.Ninja {

	private static EmbeddedMongod mongod = new EmbeddedMongod();
	
	@Inject NinjaProperties properties;
	@Inject MongoDB mongodb;
	@Inject InitialData initData;
	
	@Override
	public void onFrameworkStart() {
		super.onFrameworkStart();
		
		if(properties.isDev()) {
			try {
				mongod.startup();
			} catch(Exception EX) {
				try {
					// if it is already running the process cannot
					// bind to the existing port; but as a result of the
					// exception the running instance is terminated
					// so if started again we have it perfectly up and running
					mongod.startup();
				} catch(Exception innerEX){
					// there is a problem if I cannot start the embedded mongo
					// end it here
					throw new RuntimeException("Cannot start embedded mongo for development!");
				}
			}
		}
		
		// init mongodb
		this.mongodb.connect();
		this.mongodb.initMorphia();
		
		if(properties.isDev()) {
			// additional safe-guard
			// the process has to be in dev mode, check additional flag in application.conf
			
			if(MODE_DEVELOPMENT.equals(properties.getBoolean(APPLICATION_MODE))) {
			
				// init the db and the collections
				this.initData.init(properties.get("ninja.mongodb.dbname"), "users", "sites");
				// put some initial data into the database
				this.initData.setupData();
			}
		}
	}
}
