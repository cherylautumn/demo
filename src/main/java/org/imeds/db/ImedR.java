package org.imeds.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.imeds.util.writeException;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class ImedR {

	private static Logger logger = Logger.getLogger(ImedR.class);
	protected static Rengine re = null;
	public static void connR() throws Exception {
	   		re = new Rengine(new String[] { "--vanilla" }, false, null);
			logger.info("Rengine created, waiting for R");
	 
	        // the engine creates R is a new thread, so we should wait until it's
	        // ready
	        if (!re.waitForR()) {
	        	logger.info("Cannot load R");
	            return;
	        }
	        re.eval("library(survival)");
	        re.eval("library(KMsurv)");
	     
	        
	  }
	public static void closeR() {
		   re.end();
	       re=null;
	} 

	public static void getCoxOutliler(String rFileName,ArrayList<Double> coxResidual,ArrayList<Double> coxPredict) throws Exception{
        ResultSet rs;
        //ArrayList<Double> value = new ArrayList<Double>();
        try {
            synchronized (ImedR.class) {
            	

		       
//		        re.eval("coxData<-read.csv(\"/Users/cheryl/DevWorkSpace/javastat/DataSet/test10.csv\", header=T, sep=\",\") ");
		        re.eval("coxData<-read.csv(\""+rFileName+"\", header=T, sep=\",\") ");
		        //re.eval("coxData");
		        String coxph="coxfit<-coxph(Surv(Period,Censored)~D0+D1+D2+D3+D4+D5+D6+D7+D8+D9+D10+D11+D12+D13+D14+D15+D16, data=coxData)";
		        re.eval(coxph);
		        String deviance="devi<-resid(coxfit,type='deviance')";
		        
		        System.out.println(deviance);
		        re.eval(deviance);
		        REXP devianceResi=re.eval("devi");        
		        double[] devianceList=devianceResi.asDoubleArray();
		        
		        REXP hazard=re.eval("coxfit$linear.predictors");
		        
		        double[] hazardList =hazard.asDoubleArray();
		        
		        System.out.println(devianceList.length+" / "+hazardList.length);
		        for (int i=0;i<devianceList.length;i++){
		        	coxResidual.add(devianceList[i]);
		        	coxPredict.add(hazardList[i]);
		
		        }
		        
            }
        }catch (Exception ex) {
            throw ex;
        }
       
	}
}
