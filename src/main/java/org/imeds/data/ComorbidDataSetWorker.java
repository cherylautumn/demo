package org.imeds.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.imeds.data.common.CCIDictionary;
import org.imeds.data.common.CCIcode;
import org.imeds.util.ComorbidDSxmlTool;

public class ComorbidDataSetWorker extends DataSet {
	private String configFile="";
	private ComorbidDataSetConfig cdsc = new ComorbidDataSetConfig();
	private ComorbidDSxmlTool cfgparser = new ComorbidDSxmlTool();
	private HashMap<Integer,Integer> featureIdx = new HashMap<Integer, Integer>();
	//TODO feature could be two dimension.
	private Double[] feature;
	
	private CCIDictionary ccid;
	
	public ComorbidDataSetWorker(String configFile,  CCIDictionary ccid) {
		this.configFile = configFile;
		this.ccid = ccid;
	}

	@Override
	public void prepare() {
		this.cfgparser.parserDoc(this.configFile,this.cdsc);
//		this.feature = new Double[this.cdsc.getColList().size()];
//		System.out.println(this.cdsc.toString());
		 MapFeature();
	}

	@Override
	public void ready() {
		
	}

	@Override
	public void go() {
		// TODO Auto-generated method stub
		
	}
	
	public void MapFeature(){
		List<String> colList = this.cdsc.getColList();
	    HashMap<String, CCIcode> codeList  = this.ccid.getCodeList();    
	    
		for(int i=0;i<colList.size();i++){
			if(codeList.containsKey(colList.get(i).trim()))
			{
				this.featureIdx.put(codeList.get(colList.get(i).trim()).getID(), i);			
			}
		}
		
		Iterator iter = featureIdx.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue(); 
		    System.out.println(entry.toString());
		} 
		
	}

}
