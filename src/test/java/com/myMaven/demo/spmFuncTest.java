package com.myMaven.demo;

import java.util.TreeSet;

import org.imeds.feature.selection.basicItemsets;
import org.imeds.feature.selection.discrimItemsets;
import org.imeds.feature.selection.label;
import org.imeds.feature.selection.labelItemsets;
import org.imeds.feature.selection.statInfo;

public class spmFuncTest {

	public spmFuncTest() {
		// TODO Auto-generated constructor stub
	}

	public void TreeSetTest(){
		TreeSet<Integer> treeSmall = new TreeSet<Integer>();
		TreeSet<Integer> treeBig = new TreeSet<Integer>();
		treeSmall.add(1);
		treeSmall.add(2);
		treeSmall.add(3);
		treeSmall.add(4);
		treeSmall.add(5);
		
//		treeBig.add(1);
//		treeBig.add(2);
//		treeBig.add(3);
//		treeBig.add(4);
//		treeBig.add(6);
//		treeBig.add(7);
		
		System.out.println(treeSmall.toString()+" belong "+treeBig.toString()+" : "+treeBig.containsAll(treeSmall));
	}
	public void SeqCompare(){
		long bSmallId= 0;
		long bBigId=1;
		basicItemsets<Integer> bSmall = new basicItemsets<Integer>(bSmallId); 		
		bSmall.setItemset( new TreeSet<Integer>());
		bSmall.setItemset( new TreeSet<Integer>());
		bSmall.setItemset( new TreeSet<Integer>());
//		bSmall.setItemset( new TreeSet<Integer>());
//		bSmall.setItemset( new TreeSet<Integer>());
		bSmall.setItem(0, 4);
		bSmall.setItem(1, 5);
		bSmall.setItem(2, 6);
		bSmall.setItem(2, 7);
//		bSmall.setItem(3, 4);
//		bSmall.setItem(4, 9);
		
		basicItemsets<Integer> bBig = new basicItemsets<Integer>(bBigId);
		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItemset(new TreeSet<Integer>());
//		bBig.setItemset(new TreeSet<Integer>());
		bBig.setItem(0, 1);
		bBig.setItem(1, 2);
		bBig.setItem(2, 3);
		bBig.setItem(3, 4);
		bBig.setItem(4, 5);
		bBig.setItem(5, 6);
		bBig.setItem(5, 7);
//		bBig.setItem(6, 7);
		
		
		System.out.println(bSmall.toString());
		System.out.println(bBig.toString());
		System.out.println(bBig.isContained(bSmall));
		
	}
	public static void fisherscoreTest(){
		//This function has been verified in data\resultTmp\fisherscoreSimpleVerify.xlsx
		discrimItemsets f1 = new discrimItemsets();
		discrimItemsets f2 = new discrimItemsets();
		discrimItemsets f3 = new discrimItemsets();
		discrimItemsets f4 = new discrimItemsets();
		
		f1.addDatapoints(new label(0,2.0,(long) 1));
		f1.addDatapoints(new label(0,2.0,(long) 2));
		f1.addDatapoints(new label(0,2.0,(long) 3));
		f1.addDatapoints(new label(0,0.0,(long) 4));
		f1.addDatapoints(new label(0,0.0,(long) 5));
		f1.addDatapoints(new label(1,2.0,(long) 6));
		f1.addDatapoints(new label(1,2.0,(long) 7));
		f1.addDatapoints(new label(1,2.0,(long) 8));
		f1.addDatapoints(new label(1,2.0,(long) 9));
		f1.addDatapoints(new label(1,2.0,(long) 10));
		
		f2.addDatapoints(new label(0,2.0,(long) 1));
		f2.addDatapoints(new label(0,0.0,(long) 2));
		f2.addDatapoints(new label(0,0.0,(long) 3));
		f2.addDatapoints(new label(0,0.0,(long) 4));
		f2.addDatapoints(new label(0,0.0,(long) 5));
		f2.addDatapoints(new label(1,2.0,(long) 6));
		f2.addDatapoints(new label(1,2.0,(long) 7));
		f2.addDatapoints(new label(1,0.0,(long) 8));
		f2.addDatapoints(new label(1,0.0,(long) 9));
		f2.addDatapoints(new label(1,0.0,(long) 10));
		
		
		f3.addDatapoints(new label(0,1.0,(long) 1));
		f3.addDatapoints(new label(0,1.0,(long) 2));
		f3.addDatapoints(new label(0,1.0,(long) 3));
		f3.addDatapoints(new label(0,0.0,(long) 4));
		f3.addDatapoints(new label(0,0.0,(long) 5));
		f3.addDatapoints(new label(1,1.0,(long) 6));
		f3.addDatapoints(new label(1,1.0,(long) 7));
		f3.addDatapoints(new label(1,1.0,(long) 8));
		f3.addDatapoints(new label(1,1.0,(long) 9));
		f3.addDatapoints(new label(1,1.0,(long) 10));
		
		f4.addDatapoints(new label(0,0.0,(long) 1));
		f4.addDatapoints(new label(0,1.0,(long) 2));
		f4.addDatapoints(new label(0,1.0,(long) 3));
		f4.addDatapoints(new label(0,1.0,(long) 4));
		f4.addDatapoints(new label(0,1.0,(long) 5));
		f4.addDatapoints(new label(1,0.0,(long) 6));
		f4.addDatapoints(new label(1,0.0,(long) 7));
		f4.addDatapoints(new label(1,0.0,(long) 8));
		f4.addDatapoints(new label(1,0.0,(long) 9));
		f4.addDatapoints(new label(1,0.0,(long) 10));
		

		System.out.println("f1 fisher: "+	f1.getGain(discrimItemsets.TYPE_FISHER_GAIN));
		System.out.println("f2 fisher: "+	f2.getGain(discrimItemsets.TYPE_FISHER_GAIN));
		System.out.println("f3 fisher: "+	f3.getGain(discrimItemsets.TYPE_FISHER_GAIN));
		System.out.println("f4 fisher: "+	f4.getGain(discrimItemsets.TYPE_FISHER_GAIN));

	}
	
	public static void statTest(){
		statInfo si = new statInfo();
		si.addCnt();
		si.addSum(96.0);
		si.addSumSquare(96.0);
		
		si.addCnt();
		si.addSum(90.0);
		si.addSumSquare(90.0);
		
		si.addCnt();
		si.addSum(85.0);
		si.addSumSquare(85.0);
		
		si.addCnt();
		si.addSum(78.0);
		si.addSumSquare(78.0);
		
		si.addCnt();
		si.addSum(92.0);
		si.addSumSquare(92.0);
		
		si.addCnt();
		si.addSum(67.0);
		si.addSumSquare(67.0);
		
		
		System.out.println("avg: "+si.getMean()+" var: "+si.getVar()+ " std: "+si.getStd());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		spmFuncTest.fisherscoreTest();
//		spmFuncTest.statTest();
		
	}

}
