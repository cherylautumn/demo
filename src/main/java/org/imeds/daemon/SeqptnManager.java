package org.imeds.daemon;

import org.apache.log4j.Logger;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.seqmining.SequenceDataSetWorker;
import org.imeds.seqmining.seqRSinterpreter;
import org.imeds.util.OSValidator;
import org.imeds.util.writeException;

public class SeqptnManager extends ImedsManager {
	private static Logger logger = Logger.getLogger(SeqptnManager.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SeqPtnDataPrepare();
		
	}
	public void SeqPtnDataPrepare(){
		for(String folderP:ImedsDaemonConfig.getSeqPtnPrepareFolders()){
			logger.info("Seq Ptn Prepare Processing "+folderP);
			
			SequenceDataSetWorker sdsw = new SequenceDataSetWorker(folderP,"SPMConfig.xml",logger);
			sdsw.prepare();
			if(sdsw.getCdsc().getEnable())sdsw.ready();
			if(sdsw.getCdsc().getVMSPenable())sdsw.go();
			if(sdsw.getCdsc().isMMRFSenable()) sdsw.done();
			
			seqRSinterpreter srip = new seqRSinterpreter();
			try {
				srip.processFile(folderP+"seqDsmc", folderP+"seqptnSemantic");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("fail to transform to semantic meaning"+writeException.toString(e));
				
			}
				
		}
	}
}
