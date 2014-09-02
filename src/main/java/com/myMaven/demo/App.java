package com.myMaven.demo;

import java.util.ArrayList;

import org.imeds.data.ComorbidDataSetConfig;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.common.CCIDictionary;
import org.imeds.data.common.CCIcode;
import org.imeds.db.ImedDB;
import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
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
    	
//    	ArrayList<Integer> cptlist = new ArrayList<Integer>();
//    	
//    	
//    	cptlist.add(201826);
//    	cptlist.add(201254);
//    	cptlist.add(40482801);
//    	cptlist.add(40484648);
//    	cptlist.add(443727);
//    	cptlist.add(443729);
//    	cptlist.add(439770);
//    	cptlist.add(443734);    	
//    	cptlist.add(4096666);
//    	cptlist.add(201530);
//    	cptlist.add(201531);
//    	cptlist.add(201530);
//    	cptlist.add(40482801);
//    	cptlist.add(443592);
//    	cptlist.add(443735);
//    	cptlist.add(318712);
    	
//    	CCIcode code = cdt.getCodeList().get("Diabetes");
  //  	code.setIcdCptId(cptlist);
		
		CCIDictionary cdt = new CCIDictionary("data\\IMEDS\\DeyoCCI.csv");
    	cdt.buildDictionary();
    	cdt.buildCptMap();

    	
    	ComorbidDataSetWorker cdsw = new ComorbidDataSetWorker("data\\IMEDS\\DiabeteComorbidDS\\DSConfig.xml", cdt);
    	cdsw.prepare();
    	cdsw.ready();
    	cdsw.go();
    	ImedDB.closeDB();
//    	System.exit(0);
    }
}
