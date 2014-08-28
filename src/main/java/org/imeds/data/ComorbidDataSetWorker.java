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


public class ComorbidDataSetWorker extends DataSetWorker {

	public static final int death = 0;
	public static final int alive = 1;

	private String configFile="";
	private ComorbidDataSetConfig cdsc = new ComorbidDataSetConfig();
	private ComorbidDSxmlTool cfgparser = new ComorbidDSxmlTool();
	private CCIcsvTool csvparser = new CCIcsvTool();
	private HashMap<Integer,Integer> featureIdx = new HashMap<Integer, Integer>();
	
	
	private HashMap<Long, ArrayList<Double>> features = new HashMap<Long, ArrayList<Double>>();
	
	private CCIDictionary ccid;
	//TODO Experiment sample pick methods.
	private Integer sample_size = 500;
	private Integer sample_Kfold = 0;
	private boolean sample_random = false;
	private int sample_label=alive;
	public ComorbidDataSetWorker(String configFile,  CCIDictionary ccid) {
		this.configFile = configFile;
		this.ccid = ccid;
	}

	@Override
	public void prepare() {
		//Initialize config file
		this.cfgparser.parserDoc(this.configFile,this.cdsc);
		//Map DeyoCCI ID to my config col id
		MapFeature();
	}

	@Override
	public void ready() {
		try {	
			//1. Select all patient with Diabetes
			
			HashMap<Long, ArrayList<Double>> patients = new HashMap<Long, ArrayList<Double>>();
			ArrayList<Integer> cptlistTotal = new ArrayList<Integer>();
			for(String dga:cdsc.getIndex_diagnoses()){
				if(this.ccid.getCodeList().containsKey(dga)){
					ArrayList<Integer> cptlist = this.ccid.getCodeList().get(dga).getIcdCptId();
					if(cptlist.size()>0){
						//TODO: modify, pick patient with specific label. Should be modified with sample methods
						patients.putAll(ImedDB.getPatientsWithIndexDiagnose(cptlist, this.cdsc.getColList(), sample_size, sample_random,sample_label));
						cptlistTotal.addAll(cptlist);
					}					
				}				
			}
			
			
			//2. Select the first date when the patient was diagnosed with Diabetes. Iterate patients one by one
			Iterator<Entry<Long, ArrayList<Double>>> iter = patients.entrySet().iterator();
			int count=0;
			while (iter.hasNext()) {
				if(count%100==0)System.out.println(count+"......");
				count++;
				
				Entry<Long, ArrayList<Double>> entry = iter.next();
				
				ArrayList<Double> csvFeature =  entry.getValue();
				for(int i=csvFeature.size();i<this.cdsc.getColList().size();i++){
					csvFeature.add(0.0);	
				}
				
				//3. create a binary var=1 for each of the comorbidities if the date it was recorded was before the index date
				Long key = entry.getKey(); 			    												
				ArrayList<Integer> feature =  ImedDB.getPatientDisFeature(key,cptlistTotal);
				for(Integer ft:feature){
					if(this.ccid.getCptCat().containsKey(ft)){
						int cptCat = this.ccid.getCptCat().get(ft);
						if(this.featureIdx.containsKey(cptCat)){
							int arrIdx = this.featureIdx.get(cptCat);
							csvFeature.set(arrIdx, 1.0);
						}						
					}					
				}
			}
			this.features = patients;
		
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
		csvparser.ComorbidDataSetCreateDoc(this.cdsc.getTargetFileName(),this.cdsc.getColList(), features);
	}
		

	@Override
	public void done() {
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
		} 
		
	}
}
