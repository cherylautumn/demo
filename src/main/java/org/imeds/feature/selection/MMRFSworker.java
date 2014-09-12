package org.imeds.feature.selection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import org.imeds.data.ComorbidDataSetConfig;
import org.imeds.data.Worker;
import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;
import org.imeds.util.SPMdocTool;

public class MMRFSworker extends Worker {
	
	private ArrayList<labelItemsets> labelSeqList = new  ArrayList<labelItemsets>();
	private ArrayList<discrimItemsets> discrimSeqList = new  ArrayList<discrimItemsets>();
	private HashMap<Long, Integer> labelList = new HashMap<Long, Integer>();
	private String configFile="";
	private MMRFSConfig cdsc = new MMRFSConfig();
	private SPMdocTool cfgparser = new SPMdocTool();
	
	private ArrayList<discrimItemsets> featureSeqList = new  ArrayList<discrimItemsets>();

	public MMRFSworker() {
		// TODO Auto-generated constructor stub
		
	}
	public MMRFSworker(String configFile) {
		this.configFile = configFile;
	}


	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		this.cfgparser.parserConfigDoc(this.configFile,this.cdsc);
		this.cfgparser.parserDiscrimDoc(this.cdsc.getDiscrimItemsetsFileName(), this.discrimSeqList);
		this.labelList = CCIcsvTool.OutlierParserDoc(this.cdsc.getOutlierSource(),this.cdsc.getOutlierThreshold());
		this.cfgparser.parserLabelDoc(this.cdsc.getBasicItemsetsFileName(), this.labelSeqList, this.labelList);
	}

	@Override
	public void ready() {
		// TODO Auto-generated method stub
		
		for(discrimItemsets dscmset:this.discrimSeqList){
			for(labelItemsets lbset:this.labelSeqList){
				if(lbset.getItemsets().isContained(dscmset.getItemsets())){					
//					dscmset.addLabelCount(lbset.getLabel(), lbset.getItemsets().getId(), true);	
					
					dscmset.addDatapoints(new label(lbset.getLabel(), 2.0,lbset.getItemsets().getId()));
					
				}else{
//					dscmset.addLabelCount(lbset.getLabel(), lbset.getItemsets().getId(), false);
					dscmset.addDatapoints(new label(lbset.getLabel(), 0.0,lbset.getItemsets().getId()));
				}
			}
			dscmset.getGain(discrimItemsets.TYPE_FISHER_GAIN);
		}
	}


	@Override
	public void go() {
		// TODO Auto-generated method stub
		Collections.sort(this.discrimSeqList, new Comparator<discrimItemsets>(){			 
			public int compare(discrimItemsets e1, discrimItemsets e2) {
		        if(e1.getGain() < e2.getGain()){
		            return 1;
		        } else {
		            return -1;
		        }
		    }
		});
		
		Collections.sort(this.labelSeqList, new Comparator<labelItemsets>(){			 
			public int compare(labelItemsets e1, labelItemsets e2) {
		        if(e1.getItemsets().getId() > e2.getItemsets().getId()){
		            return 1;
		        } else {
		            return -1;
		        }
		    }
		});
		int i=0;
		Integer[] cnt= new Integer[1];
		cnt[0]=0; // total cover
		
		while(true){
			discrimItemsets dscmset = this.discrimSeqList.get(i);
			Boolean isDscmset = isDiscriminative(dscmset,cnt);
			if(isDscmset){
				this.featureSeqList.add(dscmset);
			}
			i++;
			if((i>=this.discrimSeqList.size())|| (cnt[0]>=this.labelSeqList.size()))break;
		}
		
		System.out.println(this.labelSeqList.size()+"/classify total "+cnt[0]);
	}

	public Boolean isDiscriminative(discrimItemsets dscmset, Integer[] cnt){
		Boolean rs = false;
		Collections.sort(dscmset.getDatapoints(), new Comparator<label>(){			 
			public int compare(label e1,label e2) {
		        if(e1.getData_id()> e2.getData_id()){
		            return 1;
		        } else {
		            return -1;
		        }
		    }
		});
		
		Integer[] sca = new Integer[3];
		sca[0]=0;
		sca[1]=0;
		sca[2]=0;
		for(label lb: dscmset.getDatapoints()){
			if(lb.getFeature_v()>0.0){
				sca[0]++;//total data has this feature
				if(lb.getClass_id()==0)sca[1]++;
				else sca[2]++;
				
				for(labelItemsets e1:this.labelSeqList){
					if((e1.getItemsets().getId()==lb.getData_id()) && (e1.getItemsets().getReferenceCount()==0)){
						//data has not been covered
						rs = true;
						e1.getItemsets().increaseReferenceCount();
						cnt[0]++;
					}
				}
			}
		}
		System.out.println(dscmset.getItemsets().toString()+" total: "+sca[0]+" pos: "+sca[1]+" neg: "+sca[2]+ " fisher: "+dscmset.getGain());
		return rs;
	}

	@Override
	public void done() {
		this.cfgparser.createFeatureFile(this.cdsc.getFeatureItemsetFileName(), this.featureSeqList);
//		printNotCoveredSeq();
	}
	
	public void printNotCoveredSeq(){
		for(labelItemsets e1:this.labelSeqList){
			if(e1.getItemsets().getReferenceCount()==0)System.out.println(e1.getItemsets().toString());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MMRFSworker mrfw = new MMRFSworker("data\\IMEDS\\DiabeteComorbidDS\\SPMConfig.xml");
		mrfw.prepare();
		mrfw.ready();
		mrfw.go();
		mrfw.done();
	}

}
