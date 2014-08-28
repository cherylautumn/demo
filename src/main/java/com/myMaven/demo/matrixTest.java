package com.myMaven.demo;

import org.la4j.inversion.GaussJordanInverter;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.matrix.sparse.CCSMatrix;




public class matrixTest {
	public static int userN= 10;
	public static double[][] dx= new double[][]{
		{0.0,-0.19138793197590276, 0.7834675900121327},
		{1.0,3.712420417753061, 3.55967640829891},
		{0.0,-0.3173743619974614, 0.9034702789806682},
		{1.0,4.759494447180777, 3.407011867344781},
		{0.0,-0.7078607074437426, -0.7866705652344417},
		{1.0,2.6708084832010215, 2.5322909406378016},
		{0.0,-0.07553885038446313, -0.1297104483563081},
		{1.0,2.759487072285262, 2.474689814713741},
		{0.0,-2.2199161547238107, 0.7543109438660762},
		{1.0,1.922617509832946, 1.9412373902594937}};
	public static double[][] dv = new double[userN][userN];

	public  matrixTest() {
		// TODO Auto-generated constructor stub
		System.out.println("hello");
		for(int i=0;i<userN;i++){
			for(int j=0;j<userN;j++){
				if (i==j) dv[i][j]=(i+1)*0.001;
				else dv[i][j]=0;				
			}
		}
	}
	/**
	public static void JamaTest(){
		Matrix mX = new Matrix(dx);
		Matrix mV = new Matrix(dv);
		System.out.println("V");
		mV.print(2, 4);
		
		System.out.println("X");
		mX.print(4, 2);
		System.out.println("Xt");
		mX.transpose().print(4, 2);
		System.out.println("Xt*V\n");
		mX.transpose().times(mV).print(4,2);
		System.out.println("Xt*V*X\n");
		mX.transpose().times(mV).times(mX).print(4, 2);;
		
		
		
		Matrix XVX = mX.transpose().times(mV).times(mX);
		System.out.println("XVX\n");
		XVX.print(4, 2);
		System.out.println("XVX inverse\n");
		try{
			
			XVX.inverse().print(4, 3);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	**/
	public static void la4jTest(){
		Basic2DMatrix mX = new Basic2DMatrix(dx);
		CCSMatrix mV = new CCSMatrix(dv);
		System.out.println("X\n"+mX.toString());
		System.out.println("Xt\n"+mX.transpose().toString());
		System.out.println("Xt*V\n"+mX.transpose().multiply(mV).toString());
		System.out.println("Xt*V*X\n"+mX.transpose().multiply(mV).multiply(mX).toString());
		org.la4j.matrix.Matrix XVX = mX.transpose().multiply(mV).multiply(mX);
		System.out.println("XVX\n"+XVX.toString());
		System.out.println("XVX inverse\n");

		try{
			
			GaussJordanInverter GJ = new GaussJordanInverter(XVX);
			System.out.println("XVXinverse\n"+GJ.inverse().toString());
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
    public static void main( String[] args )
    {
    	matrixTest mt = new matrixTest();
    	System.out.println("Jama test");
    //	mt.JamaTest();
    	System.out.println("la4j teset");
    	mt.la4jTest();
    	System.out.println("end");
	}
}
