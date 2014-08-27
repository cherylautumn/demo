/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.imeds.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.sparse.CCSMatrix;

import scala.Tuple2;

/**
 * Purpose: find out outlier set which classified by Logistic regression
 * Logistic regression based classification using ML Lib.
 */
public class LROutlierDataSetWorker implements Serializable {

	private static final long serialVersionUID = 7572888959583425286L;
	static class DataPoint implements Serializable {
		LabeledPoint trainP;
	    double predictP;  
	    DataPoint(LabeledPoint trainP, double predictP) {
	      this.trainP = trainP;
	      this.predictP = predictP;
	    }
		public LabeledPoint getTrainP() {
			return trainP;
		}


		public void setTrainP(LabeledPoint trainP) {
			this.trainP = trainP;
		}


		public double getPredictP() {
			return predictP;
		}


		public void setPredictP(double predictP) {
			this.predictP = predictP;
		}
	    
	    public String toString(){
	    	return "("+trainP.label()+","+predictP+")";
	    }
  }
  static class ParsePoint implements Function<String, LabeledPoint> {
    private static final Pattern COMMA = Pattern.compile(",");
    private static final Pattern SPACE = Pattern.compile(" ");

    public LabeledPoint call(String line) {
      String[] parts = COMMA.split(line);
      double y = Double.parseDouble(parts[0]);
      String[] tok = SPACE.split(parts[1]);
      double[] x = new double[tok.length];
      for (int i = 0; i < tok.length; ++i) {
        x[i] = Double.parseDouble(tok[i]);
      }
      return new LabeledPoint(y, Vectors.dense(x));
    }
  }
  private transient JavaSparkContext sc;
  private String  resIn;
  private Double  stepSize;
  private Integer iterations;
  
  public  void paraInit(String confPath){
	  String[] paras = confPath.split(",");
	  
	  this.resIn 		= paras[0];
	  this.stepSize		= Double.parseDouble(paras[1]);
	  this.iterations 	= Integer.parseInt(paras[2]);
  }
  
  public void SparkAppInit(){
	  SparkConf sparkConf = new SparkConf().setAppName(this.getClass().getName());
	  this.sc = new JavaSparkContext(sparkConf);
  }
  public void SparkAppStop(){
	  this.sc.stop();
  }
  public void LRclassify(){
	  JavaRDD<String> lines = sc.textFile(this.resIn);
	  //build training set
	  JavaRDD<LabeledPoint> points = lines.map(new ParsePoint()).cache();
 
      LogisticRegressionWithSGD lr = new LogisticRegressionWithSGD();
      lr.optimizer().setNumIterations(iterations)
                   .setStepSize(stepSize)
                   .setMiniBatchFraction(1.0);
      lr.setIntercept(true);
      final LogisticRegressionModel model = lr.train(points.rdd(),  iterations, stepSize);

      System.out.println("Final w: " + model.weights());
      
      
      model.clearThreshold();
      
      //predicting each point
      JavaRDD<DataPoint> predictPoints = points.map(new Function<LabeledPoint,DataPoint>(){
  		
		public DataPoint call(LabeledPoint v1) throws Exception {
  			// TODO Auto-generated method stub
 // 			System.out.println(model.predict(v1.features()));
  			return new DataPoint(v1,model.predict(v1.features()));
  		}
      	
      });
//      System.out.println("train"+points.count());

//      System.out.println("predict");
//     System.out.println(predictPoints.count());
      List<DataPoint> result = predictPoints.collect();
 //     System.out.println("result "+result.toString());
      
      //find outliers
      final Matrix hMatrix = calXtVX(result);
      System.out.println("HMatrix\n"+hMatrix.toString());
      
      JavaRDD<Double> pearsonPoints =  predictPoints.map(new Function<DataPoint,Double>(){

		public Double call(DataPoint v1) throws Exception {
			List<DataPoint> dl = new ArrayList<DataPoint>();
			dl.add(v1);
			Basic2DMatrix Xi = formMx(dl);
			Matrix Mi = Xi.multiply(hMatrix).multiply(Xi.transpose());
			Double Pi = v1.getPredictP();
			Double Hi = Pi*(1-Pi)*Mi.get(0, 0);
			if(Hi>1)System.out.println("Hi: "+Hi);
			
			Double Yi = v1.getTrainP().label();
			Double Ri = (Yi-Pi)/Math.pow((Pi*(1-Pi)*(1-Hi)),0.5);
			Ri = Math.abs(Ri);
			if(Ri>3)System.out.println("Ri: "+Ri+" / "+v1.getTrainP().features().toString());
  			return Hi;
		  }    	  
      	}
      );
      
      System.out.println("total:"+pearsonPoints.count());
      
  }
  
  public Matrix calXtVX(List<DataPoint> DataPoint){
	 
	  Basic2DMatrix mX = formMx(DataPoint);
	  Basic2DMatrix mV = formMv(DataPoint);
	  Matrix XtVX = mX.transpose().multiply(mV).multiply(mX);
	  GaussJordanInverter GJ = new GaussJordanInverter(XtVX);
	  try{
			XtVX =  GJ.inverse();
			System.out.println("XVXinverse\n"+GJ.inverse().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
//	  System.out.println("Verify\n"+XtVX.multiply(GJ.inverse()));
	  
	  return XtVX;
  }

  public Basic2DMatrix formMx(List<DataPoint> DataPoint){
	  
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
	  
	 return mX;
	 // System.out.println("X\n"+mX.toString());
  }
  public Basic2DMatrix formMv(List<DataPoint> DataPoint){
	  int rowN = DataPoint.size();
	  int colN = rowN;
	  Basic2DMatrix  mV = new Basic2DMatrix(rowN,colN);
	  double minVal=0;
	  for (int i = 0; i < rowN; i++) {	
		  minVal = DataPoint.get(i).getPredictP();
		  for(int j=0; j< colN; j++){
			  if(i==j){
				  mV.set(i, j, minVal*(1-minVal));
				  System.out.println(minVal*(1-minVal));
			  }else mV.set(i, j, 0);
		  }
	  }
	  return mV;
//	  System.out.println("V\n"+mV.toString());
  }
  
  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.println("Usage: JavaLR <input_dir> <step_size> <niters>");
      System.exit(1);
    }
    LROutlierDataSetWorker og = new LROutlierDataSetWorker();
//    
    og.paraInit(args[0]+","+args[1]+","+args[2]);
    og.SparkAppInit();
    og.LRclassify();
//    
//    
    og.SparkAppStop();
    
   // System.out.println("outlier test done!");
/**     
    SparkConf sparkConf = new SparkConf().setAppName("JavaLR");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);
    
   
    JavaRDD<String> lines = sc.textFile(args[0]);
    JavaRDD<LabeledPoint> points = lines.map(new ParsePoint()).cache();
    double stepSize = Double.parseDouble(args[1]);
    int iterations = Integer.parseInt(args[2]);
   
    final LogisticRegressionModel model = LogisticRegressionWithSGD.train(points.rdd(),
      iterations, stepSize);

    System.out.println("Final w: " + model.weights());
    
 // Evaluate model on training examples and compute training error
    model.clearThreshold();
    JavaRDD<DataPoint> predictPoints = points.map(new Function<LabeledPoint,DataPoint>(){

		public DataPoint call(LabeledPoint v1) throws Exception {
			// TODO Auto-generated method stub
			
			return new DataPoint(v1.label(),model.predict(v1.features()));
		}
    	
    });
//    JavaRDD<Double> predictResult = model.predict(predictPoints);
   
    JavaRDD<Double> classtype = points.map(new Function<LabeledPoint,Double>(){

		public Double call(LabeledPoint v1) throws Exception {
			// TODO Auto-generated method stub
			
			return v1.label();
		}
    	
    });
    
 
//    List<Double> train = classtype.collect();
//    System.out.println("train "+train.toString());
    
    List<DataPoint> result = predictPoints.collect();
    System.out.println("result "+result.toString());
    
//    JavaRDD<Double,>
//    val valuesAndPreds = lines.map { point =>
//      val prediction = model.predict(point.features)
//      (point.label, prediction)
//    }
//    val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
//    println("training Mean Squared Error = " + MSE)
    sc.stop();
   **/
  }
}
