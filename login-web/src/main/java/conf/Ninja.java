package conf;

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
			/**
			 * start the embedded mongodb
			 */
			try {
				mongod.startup();
			} catch(Exception EX) {
				try {
					mongod.startup();
				} catch(Exception innerEX){}
			}
		}
		
		// init mongodb
		this.mongodb.connect();
		this.mongodb.initMorphia();
		
		if(properties.isDev()) {
			/**
			 * setup test-/development-data
			 */
			this.initData.init(properties.get("ninja.mongodb.dbname"), "users", "sites");
			this.initData.setupData();
		}
	}
}
