package org.imeds.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.imeds.data.common.CCIcode;

public class CCIcsvTool implements DocumentTool{ 

	public CCIcsvTool() {
		// TODO Auto-generated constructor stub
	}

	public void createDoc(String fileName) {
		// TODO Auto-generated method stub
		//CSV Write Example using CSVPrinter
//        CSVPrinter printer = new CSVPrinter(System.out, format.withDelimiter('#'));
//        System.out.println("********");
//        printer.printRecord("ID","Name","Role","Salary");
//        for(Employee emp : emps){
//            List<String> empData = new ArrayList<String>();
//            empData.add(emp.getId());
//            empData.add(emp.getName());
//            empData.add(emp.getRole());
//            empData.add(emp.getSalary());
//            printer.printRecord(empData);
//        }
//        //close the printer
//        printer.close();
	}

	public void parserDoc(String fileName, HashMap<String, CCIcode>  codeList) {
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

}
