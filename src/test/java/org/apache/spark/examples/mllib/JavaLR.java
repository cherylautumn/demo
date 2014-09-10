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

package org.apache.spark.examples.mllib;

import java.io.Serializable;
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

import scala.Tuple2;

/**
 * Logistic regression based classification using ML Lib.
 */
public final class JavaLR {
  static class DataPoint implements Serializable {
	    DataPoint(double x, double y) {
	      this.x = x;
	      this.y = y;
	    }

	    double x;
	    double y;  
	    public String toString(){
	    	return "("+x+","+y+")";
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

  public static void main(String[] args) {
    if (args.length != 3) {
      System.err.println("Usage: JavaLR <input_dir> <step_size> <niters>");
      System.exit(1);
    }
    SparkConf sparkConf = new SparkConf().setAppName("JavaLR");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);
    JavaRDD<String> lines = sc.textFile(args[0]);
    JavaRDD<LabeledPoint> points = lines.map(new ParsePoint()).cache();
    double stepSize = Double.parseDouble(args[1]);
    int iterations = Integer.parseInt(args[2]);

    // Another way to configure LogisticRegression
    //
    // LogisticRegressionWithSGD lr = new LogisticRegressionWithSGD();
    // lr.optimizer().setNumIterations(iterations)
    //               .setStepSize(stepSize)
    //               .setMiniBatchFraction(1.0);
    // lr.setIntercept(true);
    // LogisticRegressionModel model = lr.train(points.rdd());

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
  }
}
