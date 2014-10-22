package org.imeds.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.imeds.data.SparkLRDataSetWorker.DataPoint;
import org.imeds.data.common.CCIcode;
import org.imeds.db.ImedDB;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Itemset;

public class CCIcsvTool implements DocumentTool{ 
	private static Double infinity=1000000.0;
	public CCIcsvTool() {
		// TODO Auto-generated constructor stub
	}
	public void ComorbidDataSetCreateDoc(String fileName, ArrayList<String> arrayList, HashMap<Long, ArrayList<Double>> features, boolean append, boolean withTitle) {
	
		//CSV Write Example using CSVPrinter
//		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		CSVFormat format = CSVFormat.RFC4180.withDelimiter(',');
		
       
		try {
			
			CSVPrinter printer = new CSVPrinter(new FileWriter(fileName, append), format);

	        Iterator<Entry<Long, ArrayList<Double>>> iter =features.entrySet().iterator();
	        
	        if(withTitle)printer.printRecord(arrayList);
	        
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
	public static void OutlierCreateDoc(String fileName,  Map<Long, ArrayList<Double>> list) {
		
		//CSV Write Example using CSVPrinter
//		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		CSVFormat format = CSVFormat.RFC4180.withDelimiter(',');
		
       
		try {
			
			CSVPrinter printer = new CSVPrinter(new FileWriter(fileName), format);
			printer.print("Id");
	        printer.print("TrainP");
	        printer.print("PredictP");
	        printer.print("Ri");
	        printer.println();
	        
	        Iterator<Entry<Long, ArrayList<Double>>> iter =list.entrySet().iterator();
		        
			while (iter.hasNext()) { 
				 Entry<Long, ArrayList<Double>> entry = iter.next(); 
				 printer.print(entry.getKey());
					
				 printer.print(entry.getValue().get(0));
				 printer.print(entry.getValue().get(1));
				 printer.print(entry.getValue().get(2));
				 printer.println();
			}
			 printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	}
	
	public static void SequenceDataSetCreateDoc(String fileName, HashMap<Long, ArrayList<String>> mapList) {
		
		SequenceDataSetCreateDoc(fileName,new ArrayList<ArrayList<String>>(mapList.values()));
		FileWriter fstream;
		try {
			 fstream = new FileWriter(fileName.substring(0,fileName.indexOf("."))+"_withId.csv");
		
		      BufferedWriter out = new BufferedWriter(fstream);
		      Iterator<Entry<Long, ArrayList<String>>> itemIter = mapList.entrySet().iterator();
		      while(itemIter.hasNext()){
		    	  Entry<Long, ArrayList<String>> item = itemIter.next();	
		    	  out.write(item.getKey()+",");
		    	  for(String row:item.getValue())out.write(row);
		    	  out.newLine();
		      }
		      //Close the output stream
		      out.close();    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void SequenceDataSetCreateDoc(String fileName, ArrayList<ArrayList<String>> arrayList) {
	
		 FileWriter fstream;
		try {
				fstream = new FileWriter(fileName);
		
		      BufferedWriter out = new BufferedWriter(fstream);
		      for(ArrayList<String> row:arrayList){
					 for(String ri:row)out.write(ri);
					 out.newLine();
				}
		      //Close the output stream
		      out.close();    
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
	public static void preSequenceDataParserDoc(String fileName,ArrayList<ArrayList<String>> DataPointList) {

		 //Create the CSVFormat object
		
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
        //initialize the CSVParser object
        CSVParser parser;
		try {
			parser = new CSVParser(new FileReader(fileName), format);
			
	        for(CSVRecord record : parser){
	        	
	        	ArrayList<String> row = new ArrayList<String>();
	        	row.add(record.get("person_id").trim());
	        	row.add(record.get("date_t").trim());
	        	row.add(record.get("concept_id").trim());
	       
	        	DataPointList.add(row);
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
	public static HashMap<Long,Integer> OutlierParserDoc(String fileName) {
		return OutlierParserDoc(fileName, 0.0);
	}
	
	public static void OutlierClassParserDoc(String fileName, Double threshold, HashMap<Long, Integer> labelList, HashMap<Long, Double> classList) {
		
		 CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
	        //initialize the CSVParser object
	        CSVParser parser;
			try {
				parser = new CSVParser(new FileReader(fileName), format);
				
		        for(CSVRecord record : parser){
		        	Long id = Long.parseLong(record.get("Id"));
		        	//FIXME: OUTLIERS IN THIS SET MAY NOT HAVE DRUG SEQ PTN 
		        	if( Double.parseDouble(record.get("Ri"))>=threshold)labelList.put(id, LabelType.yesOutlier);
		        	else labelList.put(id, LabelType.notOutlier);
		        	
		        	
		        	classList.put(id, Double.parseDouble(record.get("TrainP")));
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
	
	public static HashMap<Long, Integer> OutlierParserDoc(String fileName, Double threshold) {
		HashMap<Long,Integer> labelItemSet = new HashMap<Long,Integer>();
		 CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
         
	        //initialize the CSVParser object
	        CSVParser parser;
			try {
				parser = new CSVParser(new FileReader(fileName), format);
				
		        for(CSVRecord record : parser){
		        	Long id = Long.parseLong(record.get("Id"));
		        	//FIXME: OUTLIERS IN THIS SET MAY NOT HAVE DRUG SEQ PTN 
		        	if( Double.parseDouble(record.get("Ri"))>=threshold)labelItemSet.put(id, 1); //1 is outlier
		        	else labelItemSet.put(id, 0);
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
			return labelItemSet;
   
	}
	public static void OutlierParserDoc(String fileName,Integer flush, Integer fileId, Logger logger) throws Exception {
		Map<Long, ArrayList<Double>> list = new HashMap<Long,ArrayList<Double>>();
		//CSV Write Example using CSVPrinter
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
	
		CSVParser parser;
		//data\IMEDS\CgstHfComorbidDS\outlier\trainDS_5000_10_prol.csv
		try {
			
			parser = new CSVParser(new FileReader(fileName), format);
			int counter = 0;
	        for(CSVRecord record : parser){
	        	ArrayList<Double> arr = new ArrayList<Double>();
	        	arr.add(Double.parseDouble(record.get("TrainP")));
	        	arr.add(Double.parseDouble(record.get("PredictP")));
	        	if(record.get("Ri").trim().equalsIgnoreCase("Infinity")){
	        		arr.add(infinity);
	        	}else{
	        		arr.add(Double.parseDouble(record.get("Ri")));
	        	}
	        	list.put(Long.parseLong(record.get("Id")), arr);
	        	counter++;
	        	if((counter%flush)==0){
	        		logger.info(fileName+" finish "+counter);
	        		ImedDB.writeOutlier(list, fileId);
	        		list =  new HashMap<Long,ArrayList<Double>>();
	        		
	        	}
	        	
	        }
	        if(list.size()>0){
	        	ImedDB.writeOutlier(list, fileId);
	        }
	        //close the parser
	        parser.close();	        
			
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
