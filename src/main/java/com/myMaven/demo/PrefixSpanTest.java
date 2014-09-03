package com.myMaven.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan.AlgoPrefixSpan;
import ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan.SequentialPatterns;
import ca.pfv.spmf.input.sequence_database_list_integers.SequenceDatabase;

public class PrefixSpanTest {

	public PrefixSpanTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String [] arg) throws IOException{    
		// Load a sequence database
		SequenceDatabase sequenceDatabase = new SequenceDatabase(); 
		sequenceDatabase.loadFile("data\\IMEDS\\DiabeteComorbidDS\\seqDS\\seq_pseq.csv");
		// print the database to console
		sequenceDatabase.print();
		
		// Create an instance of the algorithm 
		AlgoPrefixSpan algo = new AlgoPrefixSpan(); 
//		algo.setMaximumPatternLength(3);
		
		// execute the algorithm with minsup = 50 %
		SequentialPatterns patterns = algo.runAlgorithm(sequenceDatabase, 0.2, null);    
		algo.printStatistics(sequenceDatabase.size());
//		System.out.println(" == PATTERNS ==");
//		for(List<SequentialPattern> level : patterns.levels) {
//			for(SequentialPattern pattern : level){
//				System.out.println(pattern + " support : " + pattern.getAbsoluteSupport());
//			}
//		}
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = PrefixSpanTest.class.getResource(filename);
		
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
