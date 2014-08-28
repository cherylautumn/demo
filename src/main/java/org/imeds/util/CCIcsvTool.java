package org.imeds.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.imeds.data.SparkLRDataSetWorker.DataPoint;
import org.imeds.data.common.CCIcode;

public class CCIcsvTool implements DocumentTool{ 

	public CCIcsvTool() {
		// TODO Auto-generated constructor stub
	}
	public void ComorbidDataSetCreateDoc(String fileName, ArrayList<String> arrayList, HashMap<Long, ArrayList<Double>> features) {
	
		//CSV Write Example using CSVPrinter
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		
       
		try {
			
			CSVPrinter printer = new CSVPrinter(new FileWriter(fileName, true), format);

	        Iterator<Entry<Long, ArrayList<Double>>> iter =features.entrySet().iterator();
	        
	        printer.printRecord(arrayList);
	        
			while (iter.hasNext()) { 
				Entry<Long, ArrayList<Double>> entry = iter.next(); 
				 ArrayList<Double> feature = entry.getValue();
				 printer.print(entry.getKey());
				 feature.remove(0);
				 
				 printer.printRecord(feature);
			}
			 printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	}
	public static void OutlierCreateDoc(String fileName,  Map<Long, Double> list) {
		
		//CSV Write Example using CSVPrinter
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		
       
		try {
			
			CSVPrinter printer = new CSVPrinter(new FileWriter(fileName), format);
			printer.print("Id");
	        printer.print("Ri");
	        printer.println();
	        
	        Iterator<Entry<Long, Double>> iter =list.entrySet().iterator();
		        
			while (iter.hasNext()) { 
				 Entry<Long, Double> entry = iter.next(); 
				 printer.print(entry.getKey());
				 printer.print(entry.getValue());
				 printer.println();
			}
			 printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	}
	/***********************
	 * Csv Read
	 * 
	 ************************/
	public void DeyoCCIparserDoc(String fileName, HashMap<String, CCIcode>  codeList) {
		// TODO Auto-generated method stub
		 //Create the CSVFormat object
		
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
        //initialize the CSVParser object
        CSVParser parser;
		try {
			parser = new CSVParser(new FileReader(fileName), format);
			
	        for(CSVRecord record : parser){
	        	CCIcode ccicode = new CCIcode();
	        	ccicode.setID(Integer.parseInt(record.get("Id").trim()));
	            ccicode.setName(record.get("Name").trim());
	        	ccicode.setWeight(Integer.parseInt(record.get("Weight").trim()));
	        	ccicode.setIcdList(record.get("ICD-9").trim());
	        	codeList.put(record.get("Name"), ccicode);
	        }
	        //close the parser
	        parser.close();
	     //   System.out.println(codeList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void LRPredictResultParserDoc(String fileName,List<DataPoint> DataPointList) {
		// TODO Auto-generated method stub
		 //Create the CSVFormat object
		
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
        //initialize the CSVParser object
        CSVParser parser;
		try {
			parser = new CSVParser(new FileReader(fileName), format);
			
	        for(CSVRecord record : parser){
	        	Long Id = Long.parseLong(record.get("Id").trim());
	        	Double label = Double.parseDouble(record.get("Label"));
	        	Double predict = Double.parseDouble(record.get("Prediction"));
	        	
	        	String[] vector = record.get("Feature").split(",");
	        	double[] feature = new double[vector.length];
	        	for(int i=0;i<vector.length;i++) feature[i]=Double.parseDouble(vector[i]);
	        	
	        	LabeledPoint lpt = new LabeledPoint(label, Vectors.dense(feature));
	        	
	        	DataPointList.add(new DataPoint(Id,lpt,predict));
	        }
	        //close the parser
	        parser.close();
	     //   System.out.println(codeList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void parserDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void createDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}
	
}
