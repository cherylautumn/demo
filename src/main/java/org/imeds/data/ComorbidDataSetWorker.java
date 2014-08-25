package org.imeds.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;
import org.imeds.data.common.CCIDictionary;
import org.imeds.data.common.CCIcode;
import org.imeds.db.ImedDB;
import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;

public class ComorbidDataSetWorker extends DataSet {
	private String configFile="";
	private ComorbidDataSetConfig cdsc = new ComorbidDataSetConfig();
	private ComorbidDSxmlTool cfgparser = new ComorbidDSxmlTool();
	private CCIcsvTool csvparser = new CCIcsvTool();
	private HashMap<Integer,Integer> featureIdx = new HashMap<Integer, Integer>();
	//TODO feature could be two dimension.
	
	private HashMap<Integer, Double[]> features = new HashMap<Integer, Double[]>();
	
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
		try {	
			//1. Select all patient with Diabetes
			
			HashMap<Integer, ArrayList<Double>> patients = null;
			ArrayList<Integer> cptlist = null;
			for(String dga:cdsc.getIndex_diagnoses()){
				if(this.ccid.getCodeList().containsKey(dga)){
					cptlist = this.ccid.getCodeList().get(dga).getIcdCptId();
					if(cptlist.size()>0){
						patients = ImedDB.getPatientsWithIndexDiagnose(cptlist, this.cdsc.getColList());
					}
					cptlist = null;
				}				
			}
			
			
			//2. Select the first date when the patient was diagnosed with Diabetes. Iterate patients one by one
			Iterator<Entry<Integer, ArrayList<Double>>> iter = patients.entrySet().iterator(); 
			while (iter.hasNext()) { 
				Entry<Integer, ArrayList<Double>> entry = iter.next();
				Double[] csvFeature = (Double[]) Arrays.copyOf(entry.getValue().toArray(), this.cdsc.getColList().size());
			    Integer key = entry.getKey(); 			    												
				ArrayList<Integer> feature =  ImedDB.getPatientDisFeature(key,cptlist);
				
				//3. create a binary var=1 for each of the comorbidities if the date it was recorded was before the index date
				for(Integer ft:feature){
					int cptCat = this.ccid.getCptCat().get(ft);
					int arrIdx = this.featureIdx.get(cptCat);
					csvFeature[arrIdx]=1.0;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		
		
		//4. assign the outcome- first round witll be died vs not. 
		
		//5. estimate the logit to find the outliers
	}
	
	@Override
	public void go() {
		// output patient feature csv
		csvparser.createDoc(this.cdsc.getTargetFileName(),this.cdsc.getColList(), features);
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
