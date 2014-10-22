package org.imeds.daemon;

import org.apache.log4j.Logger;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.PearsonResidualOutlier;
import org.imeds.data.common.CCIDictionary;
import org.imeds.util.writeException;

public class ComorbidManager extends ImedsManager {
	private static CCIDictionary cdt;
	private static Logger logger = Logger.getLogger(ComorbidManager.class);
	private static String DSConfig = "DSConfig.xml";
	public ComorbidManager(String DeyoCCIPath) {

		cdt = new CCIDictionary(DeyoCCIPath);
		cdt.buildDictionary();
		try {
			cdt.buildCptMap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("CCIDictionary build fail"+writeException.toString(e));
		}
	}

	public void run(){
		//Stage 1. Generate train.csv in each disease folder. 
    	//		   Related config file: ImedsDaemonConfig, DSConfig
		if(ImedsDaemonConfig.getPatientFeatureExpFolders().size()>0){
    		GenPatientFeature();
		}
		//Stage 2.1. Train and predict LR model. Done by Spark.
		
		//Stage 2.2. Generate outlier.
		if(ImedsDaemonConfig.getPearsonOutlierExpFolders().size()>0){
			for(String folderP:ImedsDaemonConfig.getPearsonOutlierExpFolders()){
				logger.info("Processing PearsonOutlierGen: "+folderP);
				PearsonResidualOutlier prlo = new PearsonResidualOutlier();				
				prlo = new PearsonResidualOutlier(folderP+DSConfig);
				prlo.oulierGen();
				prlo = null;
			}
		}
		//Stage 2.3. Write outlier into database
		if(ImedsDaemonConfig.getPearsonOutlierToDB().size()>0){
			for(String folderP:ImedsDaemonConfig.getPearsonOutlierToDB()){
				logger.info("Processing Pearson Outlier To DB: "+folderP);
				PearsonResidualOutlier prlo = new PearsonResidualOutlier();				
				prlo = new PearsonResidualOutlier(folderP+DSConfig);
//				prlo.oulierGen();
				prlo.writeOulierToDB();
				prlo = null;
			}
		}
	}
	public void GenPatientFeature(){
		for(String folderP:ImedsDaemonConfig.getPatientFeatureExpFolders()){
			logger.info("Processing GenPatientFeature: "+folderP);
			createtrainDS(folderP);
			
		}
	}
	public void createtrainDS(String expFolderPath){
		ComorbidDataSetWorker cdsw = new ComorbidDataSetWorker(expFolderPath+DSConfig, cdt);
    		cdsw.prepare();
    		cdsw.ready();
    		cdsw.go();
	}
	
}
