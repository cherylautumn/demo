package com.myMaven.demo;

import org.imeds.data.ComorbidDataSetConfig;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.common.CCIDictionary;
import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//    	ComorbidDSxmlTool xh = new ComorbidDSxmlTool(new ComorbidDataSetConfig());
//    	xh.createDoc("testxml.xml");
//    	//xh.parserXml("testxml.xml");
//    	xh.parserDoc("data\\IMEDS\\DiabeteComorbidDS\\DSConfig.xml");
//    	CCIcsvTool xh = new CCIcsvTool();
//    	
//    	xh.parserDoc("data\\IMEDS\\DeyoCCI.csv");    	
//    	System.out.println(Math.ceil(410.9));
//    	System.out.println(Math.floor(410.9));
    	
    	CCIDictionary cdt = new CCIDictionary("data\\IMEDS\\DeyoCCI.csv");
    	cdt.buildDictionary();
//    	
//    	System.out.println(Math.ceil(41.99));
//    	System.out.println(Math.floor(41.99));
    	
    	ComorbidDataSetWorker cdsw = new ComorbidDataSetWorker("data\\IMEDS\\DiabeteComorbidDS\\DSConfig.xml", cdt);
    	cdsw.prepare();
    }
}
