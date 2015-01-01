package org.imeds.feature.screening;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ModelFreeScreen {
	public enum MFStype {General, Survival};
	public ArrayList<feature> generalScreen(ArrayList<Tuple> DataPointList){
		Collections.sort(DataPointList);		
		ArrayList<feature> xMargin=new ArrayList<feature>();
		double culmulatemar=0;
		double currentmar=0;
		double lastmar=0;
		double currentY=0;
		double lastY=0;
		double totalN= DataPointList.size();
		for(int i=0;i<DataPointList.get(0).getxList().size();i++){
			culmulatemar=0;
			currentmar=0;
			lastmar=0;
			for(int j=1;j<DataPointList.size();j++){
				currentY=DataPointList.get(j).getyList().get(0);
				
				lastY=DataPointList.get(j-1).getyList().get(0);
				currentmar = currentmar+DataPointList.get(j-1).getxList().get(i);				
				if(currentY>lastY){
					culmulatemar+=Math.pow((currentmar/totalN),2);
					lastmar=currentmar;
				}else{
					culmulatemar+=Math.pow((lastmar/totalN),2);
				}
				
//				double sumtmp=0;				
//				for(int k=0;k<j;k++){
//					lastY=DataPointList.get(k).getyList().get(0);
//					if(currentY>lastY){
//						sumtmp+=DataPointList.get(k).getxList().get(i);
//					}
//				}
//				culmulatemar+=Math.pow((sumtmp/totalN),2);
			}
			
			xMargin.add(new feature((long)i,culmulatemar/totalN));
		}
		Collections.sort(xMargin);
		return xMargin;
	}
	public ArrayList<feature> generalScreenCox(ArrayList<Tuple> DataPointList,HashMap<Integer, Double> survFunc){
		Collections.sort(DataPointList);		
		ArrayList<feature> xMargin=new ArrayList<feature>();
		double culmulatemar=0;
		double currentmar=0;
		double lastmar=0;
		double currentY=0;
		double lastY=0;
		double totalN= DataPointList.size();
		for(int i=0;i<DataPointList.get(0).getxList().size();i++){
			culmulatemar=0;
			currentmar=0;
			lastmar=0;
			for(int j=1;j<DataPointList.size();j++){
				currentY=DataPointList.get(j).getyList().get(0);
				
				lastY=DataPointList.get(j-1).getyList().get(0);
				
				double surv= Math.pow(survFunc.get((int)lastY), 2);
//				sumtmp+=DataPointList.get(k).getxList().get(i)*DataPointList.get(k).getyList().get(1)/surv;
				currentmar = currentmar+DataPointList.get(j-1).getxList().get(i)*DataPointList.get(j-1).getyList().get(1)/surv;			
				if(currentY>lastY){
					culmulatemar+=Math.pow((currentmar/totalN),2);
					lastmar=currentmar;
				}else{
					culmulatemar+=Math.pow((lastmar/totalN),2);
				}
				
//				double sumtmp=0;
//				double surv=0;
//				for(int k=0;k<j;k++){
//					lastY=DataPointList.get(k).getyList().get(0);
//					if(currentY>lastY){
//						surv= Math.pow(survFunc.get((int)lastY), 2);
//						sumtmp+=DataPointList.get(k).getxList().get(i)*DataPointList.get(k).getyList().get(1)/surv;
//					}
//				}
//				culmulatemar+=Math.pow((sumtmp/totalN),2);
			}
			
			xMargin.add(new feature((long)i,culmulatemar/totalN));
		}
		Collections.sort(xMargin);
		return xMargin;
	}
}
