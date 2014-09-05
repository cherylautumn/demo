package org.imeds.seqmining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import org.imeds.db.ImedDB;
import org.imeds.util.CCIcsvTool;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Itemset;


public class seqRSinterpreter {
	private ArrayList<ArrayList<Integer>> indataList = new ArrayList<ArrayList<Integer>>();
	private HashMap<Integer, String> cptmap = new HashMap<Integer, String>();
	private ArrayList<ArrayList<String>> outdataList = new ArrayList<ArrayList<String>>();
	public seqRSinterpreter() {
		// TODO Auto-generated constructor stub
		String dbDriver = "org.postgresql.Driver";
		String dbURL = "jdbc:postgresql://omop-datasets.cqlmv7nlakap.us-east-1.redshift.amazonaws.com:5439/truven";
		String dbUser = "hchiu";
		String dbPassword = "1QAZ2wsx";
		String search_path = "ccae_cdm4";

		try {
			ImedDB.connDB(dbDriver, dbURL,dbUser, dbPassword, search_path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processFile(String infolderName, String outfolderName) throws Exception{
		File directory = new File(infolderName);
		File[] fList = directory.listFiles();
		for (File file : fList){		
			if (file.isFile()){
				readFile(infolderName+"\\"+file.getName());
				ImedDB.getDisSemanticConcept(cptmap);
				writeFile(outfolderName+"\\semantic_"+file.getName());				 
			}
		}
	}
	public void readFile(String FileName) throws FileNotFoundException{
		FileInputStream fis = new FileInputStream(FileName);
		Scanner scanner = new Scanner(fis);
		 indataList = new ArrayList<ArrayList<Integer>>();
		 cptmap = new HashMap<Integer, String>();
		// reading file line by line using Scanner in Java
		System.out.println("Reading file line by line in Java using Scanner");
		int idx = 0;
		String line = "";
		String[] linesplit;
		
		while (scanner.hasNextLine()) {
			
			idx++;
			line = scanner.nextLine();
			linesplit = line.split(" ");
			ArrayList<Integer> itemarr = new ArrayList<Integer>();
			for(int i=0;i<linesplit.length;i++){
				
				String itemtmp = linesplit[i].trim();
				if(itemtmp!=null && !itemtmp.contains("SUP")){
					Integer cpttmp = Integer.parseInt(itemtmp);
					if(cpttmp>100){
						cptmap.put(cpttmp,"");
					}
					itemarr.add(cpttmp);					
				}
				
			}
			indataList.add(itemarr);
		}
	}
	
	public void writeFile(String fileName){
		 FileWriter fstream;
			try {
					fstream = new FileWriter(fileName);
			
			      BufferedWriter out = new BufferedWriter(fstream);
			      for(ArrayList<Integer> row:indataList ){
			    	  	 String line="";
						 for(Integer ri:row){
							 if(this.cptmap.containsKey(ri))
							 {
								 line = line + "'"+this.cptmap.get(ri)+"' ";
							 }else{
								 line = line + ri+" ";
							 }
						 }
						 out.write(line);
						 out.newLine();
					}
			      //Close the output stream
			      out.close();    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 seqRSinterpreter srsi = new  seqRSinterpreter();
		 srsi.processFile("data\\IMEDS\\DiabeteComorbidDS\\seqptn", "data\\IMEDS\\DiabeteComorbidDS\\seqptnSemantic");
		 ImedDB.closeDB();
	}

}
