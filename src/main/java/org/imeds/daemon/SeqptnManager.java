package org.imeds.daemon;

import org.apache.log4j.Logger;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.SequenceDataSetWorker;

public class SeqptnManager extends ImedsManager {
	private static Logger logger = Logger.getLogger(ComorbidManager.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	public void SeqPtnDataPrepare(){
		for(String folderP:ImedsDaemonConfig.getSeqPtnPrepareFolders()){
			logger.info("Seq Ptn Prepare Processing "+folderP);
			
			SequenceDataSetWorker sdsw = new SequenceDataSetWorker(folderP,"DSConfig.xml");
			sdsw.prepare();
			sdsw.ready();
		}
	}
	

}
