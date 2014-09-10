package com.myMaven.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class SeqAccuracy {
	private ArrayList<String> groundTruth = new ArrayList<String>();
	public SeqAccuracy() {
		// TODO Auto-generated constructor stub
	}
	public ArrayList<String> readFile(String FileName) throws FileNotFoundException{
		ArrayList<String> seqResult = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(FileName);
		Scanner scanner = new Scanner(fis);
	
		String line;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			line = line.substring(0, line.indexOf("S")).trim();
			seqResult.add(line);
		}
		return seqResult;
	}
	public Integer countIntersection(ArrayList<String> listA,  ArrayList<String> listB){
		Integer count = 0;
		for(String strA:listA){
			for(String strB:listB){
				if(strB.equals(strA))count++;
			}
		}
		return count;		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		 int s=4;
		SeqAccuracy setacu = new  SeqAccuracy();
		 ArrayList<String> groundTruth = setacu.readFile("data\\VMSP_out.txt");
		 ArrayList<String> sampleSeq = new ArrayList<String>();
		 Double avgprecision=0.0;
		 Double avgrecall =0.0;
		 for(int i=0;i<s;i++){
			 sampleSeq = setacu.readFile("data\\VMSP"+i+"_out.txt");
			 Integer hit = setacu.countIntersection(groundTruth, sampleSeq);
			 System.out.println("Precision: "+  (hit*1.0)/sampleSeq.size()+" / Recall: "+ (hit*1.0)/groundTruth.size());
			 avgrecall = avgrecall+(hit*1.0)/groundTruth.size();
			 avgprecision = avgprecision+(hit*1.0)/sampleSeq.size();
			
			 
		 }
		 
		 System.out.println("Avg Precision: "+ avgprecision/s+" /Avg Recall: "+avgrecall/s);
		 
		 System.out.println("-----------------------");
		 HashSet<String> sampleSeqAll = new HashSet<String>();
		 for(int i=0;i<s;i++){
			 sampleSeq = setacu.readFile("data\\VMSP"+i+"_out.txt");
			 for(String str:sampleSeq){
				 sampleSeqAll.add(str);
			 }
		 }
		 ArrayList<String> sampleSeqArrAll = new  ArrayList<String>(sampleSeqAll);
		 Integer hitAll = setacu.countIntersection(groundTruth, sampleSeqArrAll);
		 System.out.println("Precision: "+(hitAll*1.0)/sampleSeqArrAll.size()+" / Recall: "+(hitAll*1.0)/groundTruth.size());
		 	 
	}

}
