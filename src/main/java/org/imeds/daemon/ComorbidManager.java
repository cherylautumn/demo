package org.imeds.daemon;

import org.apache.log4j.Logger;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.common.CCIDictionary;

public class ComorbidManager extends ImedsManager {
	private static CCIDictionary cdt;
	private static Logger logger = Logger.getLogger(ComorbidManager.class);
	public ComorbidManager(String DeyoCCIPath) {

		cdt = new CCIDictionary(DeyoCCIPath);
		cdt.buildDictionary();
		try {
			cdt.buildCptMap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("CCIDictionary build fail"+e.getMessage());
		}
	}

	public void run(){
		//Stage 1. Generate train.csv in each disease folder. 
    	//		   Related config file: ImedsDaemonConfig, DSConfig
    		GenPatientFeature();	
	}
	public void GenPatientFeature(){
		for(String folderP:ImedsDaemonConfig.getPatientFeatureExpFolders()){
			logger.info("Processing "+folderP);
			createtrainDS(folderP);
		}
	}
	public void createtrainDS(String expFolderPath){
		ComorbidDataSetWorker cdsw = new ComorbidDataSetWorker(expFolderPath+"DSConfig.xml", cdt, this.logger);
    		cdsw.prepare();
    		cdsw.ready();
    		cdsw.go();
	}
	
}
