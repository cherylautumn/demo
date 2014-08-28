package org.imeds.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.imeds.data.SparkLRDataSetWorker.DataPoint;
import org.imeds.util.CCIcsvTool;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import scala.reflect.internal.Trees.This;

public class PearsonResidualOutlier extends Outlier{
	private List<DataPoint> DataPointList;
	private Map<Long,Double> OutlierList;
	private Double threshold = 0.0;
	private String outFileName;
	
	public PearsonResidualOutlier(String fileName, String outFileName, Double threshold) {
		DataPointList = new ArrayList<DataPoint>();
		OutlierList = new HashMap<Long,Double>();
		this.outFileName = outFileName;
		this.threshold = threshold;
		CCIcsvTool.LRPredictResultParserDoc(fileName, this.DataPointList);
	
	}
	
	public List<DataPoint> getDataPointList() {
		return DataPointList;
	}

	public void setDataPointList(List<DataPoint> dataPointList) {
		DataPointList = dataPointList;
	}


	public Map<Long, Double> getOutlierList() {
		return OutlierList;
	}

	public void setOutlierList(Map<Long, Double> outlierList) {
		OutlierList = outlierList;
	}

	@Override
	public void oulierGen() {
		// TODO Auto-generated method stub
		this.OutlierList.clear();
		Matrix hMatrix = calXtVX(getDataPointList());
		for(DataPoint dl:this.DataPointList){
			Double Ri = isOutlier(hMatrix, dl);
			
			if(Ri>this.threshold){
				this.OutlierList.put(dl.getId(), Ri);			
			}						
		}
		CCIcsvTool.OutlierCreateDoc(this.outFileName,  (HashMap<Long, Double>) this.OutlierList);
	}
	
	public static Double isOutlier(Matrix hMatrix,DataPoint v1){
		List<DataPoint> dl = new ArrayList<DataPoint>();
		dl.add(v1);
		Basic2DMatrix Xi = formMx(dl);
		Matrix Mi = Xi.multiply(hMatrix).multiply(Xi.transpose());
		Double Pi = v1.getPredictP();
		Double Hi = Pi*(1-Pi)*Mi.get(0, 0);
//		if(Hi>1)System.out.println("Hi: "+Hi);
		
		Double Yi = v1.getTrainP().label();
		Double Ri = (Yi-Pi)/Math.pow((Pi*(1-Pi)*(1-Hi)),0.5);
		Ri = Math.abs(Ri);
		return Ri;
	}


	 public static  Matrix calXtVX(List<DataPoint> DataPoint){
		 
		  Basic2DMatrix mX = formMx(DataPoint);
		  Basic2DMatrix mV = formMv(DataPoint);
		  Matrix XtVX = mX.transpose().multiply(mV).multiply(mX);
		  GaussJordanInverter GJ = new GaussJordanInverter(XtVX);
		  try{
				XtVX =  GJ.inverse();
//				System.out.println("XVXinverse\n"+GJ.inverse().toString());
			}catch(Exception e){
				e.printStackTrace();
			}
//		  System.out.println("Verify\n"+XtVX.multiply(GJ.inverse()));
		  
		  return XtVX;
	  }

	  public static  Basic2DMatrix formMx(List<DataPoint> DataPoint){
		  
		  int rowN = DataPoint.size();
		  int colN = DataPoint.get(0).getTrainP().features().size()+1;
		  Basic2DMatrix  mX = new Basic2DMatrix(rowN,colN);
		  
		  for (int i = 0; i < rowN; i++) {
			  mX.set(i,0,DataPoint.get(i).getTrainP().label());
			  double[] arr = DataPoint.get(i).getTrainP().features().toArray();
			  for(int j=1; j< colN; j++){
				  mX.set(i, j, arr[j-1]);
			  }
		  }
		  
		 
//		  System.out.println("X\n"+mX.toString());
		  return mX;
	  }
	  public static Basic2DMatrix formMv(List<DataPoint> DataPoint){
		  int rowN = DataPoint.size();
		  int colN = rowN;
		  Basic2DMatrix  mV = new Basic2DMatrix(rowN,colN);
		  double minVal=0;
		  for (int i = 0; i < rowN; i++) {	
			  minVal = DataPoint.get(i).getPredictP();
			  for(int j=0; j< colN; j++){
				  if(i==j){
					  mV.set(i, j, minVal*(1-minVal));
//					  System.out.println(minVal*(1-minVal));
				  }else mV.set(i, j, 0);
			  }
		  }
		 
//		  System.out.println("V\n"+mV.toString());
		  return mV;
	  }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PearsonResidualOutlier prlo = new PearsonResidualOutlier("data\\IMEDS\\DiabeteComorbidDS\\trainDSf_300_1.0.csv","data\\IMEDS\\DiabeteComorbidDS\\trainDSf_300_1.0_ol.csv", 1.0);
		prlo.oulierGen();
		System.out.println(prlo.getOutlierList().toString());
	}

	
}
