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
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.imeds.data.common.CCIcode;

public class CCIcsvTool implements DocumentTool{ 

	public CCIcsvTool() {
		// TODO Auto-generated constructor stub
	}
	public void ComorbidDataSetCreateDoc(String fileName, ArrayList<String> arrayList, HashMap<Long, ArrayList<Double>> features) {
	
		// TODO Auto-generated method stub
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

	public void parserDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void createDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}
	
}
