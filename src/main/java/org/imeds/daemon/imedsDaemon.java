package org.imeds.daemon;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.common.CCIDictionary;
import org.imeds.db.ImedDB;
import org.imeds.util.OSValidator;


public class imedsDaemon {
	/*####################################################################################*
     * Static fields
     *####################################################################################*/

    private static imedsDaemon singleton = null;
    
    private static Logger logger = Logger.getLogger(imedsDaemon.class);
    private String ImedsDaemonConfigPath = "data\\IMEDS\\ImedsDaemonConfig.xml";
    private String DeyoCCIPath = "data\\IMEDS\\DeyoCCI.csv";
    private ImedsManager manager ;
	public imedsDaemon() {
		
		OsPathChk();
		logger.info("Load data\\IMEDS\\ImedsDaemonConfig.xml.");
		ImedsDaemonConfig.loadImedsDaemonConfig(ImedsDaemonConfigPath);
		
		logger.info("IMED database connection.");
		DbInit();
		
	}
	/*####################################################################################*
     * Public Instance methods
     *####################################################################################*/
	public void OsPathChk(){
		if(!OSValidator.isWindows()){
			ImedsDaemonConfigPath =ImedsDaemonConfigPath.replace("\\", "/");
			DeyoCCIPath = DeyoCCIPath.replace("\\", "/");
		}
//		System.out.println(ImedsDaemonConfigPath);
	}
    public void DbInit(){
    
		try {
			ImedDB.connDB(ImedsDaemonConfig.getOmopDbDriver(), ImedsDaemonConfig.getOmopDbUrl(),ImedsDaemonConfig.getOmopDbUser(), ImedsDaemonConfig.getOmopDbPassword(), ImedsDaemonConfig.getOmopDbSearchPath());
		} catch (Exception e) {
			logger.error("IMED database connection fail!");
			logger.error(e.toString());
		}
    }
    public static void DbClose(){
    		ImedDB.closeDB();
    }
    
   
    public void service() {
        logger.info("Start scheduling.....");
    		manager = new ComorbidManager(DeyoCCIPath);
    		manager.run();
    		
    		manager = new SeqptnManager();
    		manager.run();
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		singleton = new imedsDaemon();
		singleton.service();
		DbClose();
	}

}
