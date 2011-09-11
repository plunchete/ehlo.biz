package tasks;

import java.util.Properties;

import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import siena.PersistenceManagerFactory;
import siena.jdbc.JdbcPersistenceManager;
import utils.BigBrotherHelper;
import utils.C3poConnectionManager;

@OnApplicationStart
public class Bootstrap extends Job {
	
	@Override
	public void doJob() {
		// configure path for static resources
		if(Play.mode == Play.Mode.PROD) {
			BigBrotherHelper.CLIENT_ID = "NV2XRQZWLLPRQ3GG5LELUHHCD0G2XMHLMDZRMQOFHK1U4OST";
			BigBrotherHelper.CLIENT_SECRET = "HDXAVYCOWW3QBHZ1WD1JHZCB1OFSJJDICHPOQBM2NYTWD0OQ";
		}

		Properties propertiesFrontendDB = new Properties();
		propertiesFrontendDB.setProperty("driver", Play.configuration.getProperty("db.driver"));
		propertiesFrontendDB.setProperty("url", Play.configuration.getProperty("db.url"));
		propertiesFrontendDB.setProperty("user", Play.configuration.getProperty("db.user"));
		propertiesFrontendDB.setProperty("password", Play.configuration.getProperty("db.pass"));
		propertiesFrontendDB.setProperty("idleConnectionTestPeriod", "1300");
		
		C3poConnectionManager cmToFrontend = new C3poConnectionManager();
		cmToFrontend.init(propertiesFrontendDB);
		JdbcPersistenceManager frontendPersistenceManager = new JdbcPersistenceManager(cmToFrontend, null);
		PersistenceManagerFactory.install(frontendPersistenceManager, User.class);
		
		try {
			PersistenceManagerFactory.install(frontendPersistenceManager, User.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
