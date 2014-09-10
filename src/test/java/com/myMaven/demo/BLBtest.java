package com.myMaven.demo;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class BLBtest {
	private ArrayList<String> bigData = new ArrayList<String>();
	private ArrayList<ArrayList<Integer>> sbin = new ArrayList<ArrayList<Integer>>(); 
	public BLBtest() {
		// TODO Auto-generated constructor stub
	}
	public void readFile(String FileName) throws FileNotFoundException{
		FileInputStream fis = new FileInputStream(FileName);
		Scanner scanner = new Scanner(fis);
		 
		System.out.println("Reading file line by line in Java using Scanner");
		
		while (scanner.hasNextLine()) {
			bigData.add(scanner.nextLine());
		}
	}
	public void genSampleIdx(Integer s, Double r,Integer randomRange){
		Integer b = (int) Math.round(Math.pow(bigData.size(),r));
		Set<Integer> noRpls= new HashSet<Integer>();
		
		Random ran = new Random();
		Integer random=0;
		for(int i=0;i<s;i++){
			ArrayList<Integer> bin = new ArrayList<Integer>();
			while(bin.size()<=b){
				while(noRpls.contains(random)){
					random = ran.nextInt(randomRange);	
				}
				noRpls.add(random);
				bin.add(random);
			}
			noRpls= new HashSet<Integer>();
			sbin.add(bin);
		}
//		System.out.println(sbin.toString());
	}
	public void writeFile(String fileName){
		FileWriter fstream;
		Random ran = new Random();
			try {
				 for(int i=0;i<sbin.size();i++ ){
					 String outfileName = fileName.substring(0, fileName.indexOf("."))+i+"_out.txt";
					 fstream = new FileWriter(outfileName);
					  ArrayList<Integer> bset = sbin.get(i);
				      BufferedWriter out = new BufferedWriter(fstream);
				      int j=0;
				      Integer bGenN = ran.nextInt(bset.size()%10);
				      Integer bidx =0;		
				      while((j+bGenN) <bigData.size()){
				    	  j = j+bGenN;
				    	 		    	  			    	  	 
				    	  String line = bigData.get(bidx);
				    	  for(int k=0;k<bGenN;k++){	 
							 out.write(line);
							 out.newLine();
				    	  }
				    	  if(bidx < bset.size())bidx++;
				    	  else bidx=0;
				    	  
				    	  bGenN = ran.nextInt(bset.size()%10);
				    	  if(bGenN>=(bigData.size()-j)) bGenN =(bigData.size()-j); 
				    	  
				      }	 
					out.close();    
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void writeFileNoRandomGen(String fileName){
		FileWriter fstream;
		
			try {
				 for(int i=0;i<sbin.size();i++ ){
					 String outfileName = fileName.substring(0, fileName.indexOf("."))+i+"_out.txt";
					 fstream = new FileWriter(outfileName);
					  ArrayList<Integer> bset = sbin.get(i);
				      BufferedWriter out = new BufferedWriter(fstream);
			
				      for(Integer bidx:sbin.get(i)){
				       String line = bigData.get(bidx);
				    	 
						out.write(line);
						out.newLine();
				      }
					   out.close();    
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		BLBtest blbt = new BLBtest();
		blbt.readFile("data\\LEVIATHAN.txt");
		blbt.genSampleIdx(4,0.6,5833);
//		blbt.writeFile("data\\LEVIATHAN.txt");
		blbt.writeFileNoRandomGen("data\\LEVIATHAN.txt");
	
	}

}
