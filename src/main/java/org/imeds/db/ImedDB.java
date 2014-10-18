package org.imeds.db;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.imeds.daemon.imedsDaemon;
import org.imeds.data.ComorbidDataSetWorker;
import org.imeds.data.common.CCIDictionary;
import org.imeds.util.ImedDateFormat;
import org.imeds.util.ImedStringFormat;

public class ImedDB {

	public ImedDB() {
		// TODO Auto-generated constructor stub
	}
	protected static Connection conn = null;
	protected static Statement stmt = null;
	
	private static Logger logger = Logger.getLogger(ImedDB.class);
	public static void connDB(String dbDriver, String dbURL, String dbUser, String dbPassword, String search_path) throws Exception {
	     
		try{
	            Class.forName(dbDriver);
	       
	            conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
	            logger.info("SQL Connection to database established!");
	            stmt = conn.createStatement();
	            logger.info("SQL Statement is prepared!");
	            PreparedStatement s = conn.prepareStatement("set search_path to "+search_path+";"); 
	            s.execute(); 
	          
	     } catch (SQLException e) {
	            logger.error("Connection Failed! Check output console");
	            throw e;
	     }
	        
	  }
	public static void closeDB() {

	        try
	        {
	            if(conn != null)conn.close();            
	            if(stmt != null) stmt.close();
	            logger.info("Connection closed !!");
	        } catch (SQLException e) {
	        	logger.error("connection close fail\n"+e.getMessage());
	            
	        }
	}  

	public static ArrayList<Integer> getCptCatMap(String range) throws Exception{
        ResultSet rs;
        ArrayList<Integer> value = new ArrayList<Integer>();
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();

            	queryStr.append(" SELECT DISTINCT concept_id FROM condition_vocab_cache WHERE " + range);            	
  //          	System.out.printf(queryStr.toString());
                rs = stmt.executeQuery(queryStr.toString());
        
                while (rs.next()) {
                   value.add(rs.getInt("concept_id"));
//                	System.out.println(rs.getInt("concept_id"));
                }
        
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
        return value;
	}
	
	
	public static HashMap<Long, ArrayList<Double>>  getPatientsWithIndexDiagnose(ArrayList<Integer> cptIdList, ArrayList<String> colList, int sample_start, int sample_end, boolean random,  int LabelType) throws Exception{
		String orderCdt="";
		String labelCdt="";		
		HashMap<Long, ArrayList<Double>> value = new  HashMap<Long, ArrayList<Double>>();

		
		switch(LabelType){
		case ComorbidDataSetWorker.death:
			labelCdt = " WHERE label IS NOT NULL "; //death
			break;
		case ComorbidDataSetWorker.alive:
			labelCdt = " WHERE label IS NULL "; //alive
			break;
		default:
			labelCdt ="";
			break;
		}
		
		if(random){

			orderCdt = " ORDER BY random() LIMIT "+(sample_end - sample_start)+" OFFSET "+sample_start;			
		}else{
			//TODO experiment sample method is waiting for further modify
			orderCdt = " LIMIT "+(sample_end - sample_start)+" OFFSET "+sample_start;	
		}
		value = getPatientsWithIndexDiagnose(cptIdList, colList, labelCdt+orderCdt);
		return value;
	}
	public static  HashMap<Long, ArrayList<Double>> getPatientsWithIndexDiagnose(ArrayList<Integer> cptIdList, ArrayList<String> colList, String Cdt) throws Exception{
        ResultSet rs;
        HashMap<Long, ArrayList<Double>> value = new  HashMap<Long, ArrayList<Double>>();
        try {
            synchronized (ImedDB.class) {
//				SELECT * from (
//				select p.person_id as ID, p.gender_concept_id as Gender, year_of_birth as Age, race_concept_id as Race, ethnicity_concept_id as Ethnicity, location_id as Location, death.person_id as Label
//				FROM condition_occurrence co, person p LEFT OUTER JOIN death  ON (p.person_id = death.person_id)  
//				WHERE p.person_id = co.person_id AND condition_concept_id 
//
//				IN (201820,201826,201254,40482801,40484648,443727,443734,439770,4096666,201530,201531,443592,443735,321822,443729,318712)
//				) --WHERE label IS  NULL
//				ORDER BY random()
//				LIMIT 1000;
            	StringBuffer queryStr = new StringBuffer();            	
            	queryStr.append(" SELECT * FROM (");
            	queryStr.append(" SELECT DISTINCT p.person_id as ID, p.gender_concept_id as Gender, year_of_birth as Age, race_concept_id as Race, ethnicity_concept_id as Ethnicity, location_id as Location, death.person_id as Label ");            	
            	queryStr.append(" FROM condition_occurrence co, person p LEFT OUTER JOIN death  ON (p.person_id = death.person_id) ");
            	queryStr.append(" WHERE p.person_id = co.person_id AND condition_concept_id IN ("+ImedStringFormat.tranListIn(cptIdList)+")");
            	queryStr.append(" ) "+Cdt);
            	
            	logger.info("Query getPatientsWithIndexDiagnose :"+ queryStr.toString()+"\n" );
                rs = stmt.executeQuery(queryStr.toString());
               
                while (rs.next()) {
                   ArrayList<Double> tmp = new ArrayList<Double>();
 
                   tmp.add(rs.getDouble("ID"));
        		   if(rs.getLong("Label")>0) tmp.add((double) ComorbidDataSetWorker.death);  //means death
                   else tmp.add((double) ComorbidDataSetWorker.alive);
        		   if(colList.contains("Gender")){tmp.add((rs.getDouble("Gender")-8500));}
        		   if(colList.contains("Age")){
            		   Date now = new Date();
        			   Calendar cal = Calendar.getInstance();
        			   cal.setTime(now);
        			    int year = cal.get(Calendar.YEAR);
        			   tmp.add((year-rs.getDouble("Age"))/10);
        		   }
        		   if(colList.contains("Race")) tmp.add(rs.getDouble("Race"));
        		   if(colList.contains("Ethnicity")) tmp.add(rs.getDouble("Ethnicity"));
        		   if(colList.contains("Location")){
        			   if(rs.getDouble("Location")>0)
        			   tmp.add(rs.getDouble("Location")/10000000);
        		   }
                   
                  value.put(rs.getLong("ID"), tmp);
                }
        
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
        return value;
	}

	public static ArrayList<Integer> getPatientDisFeature(Long pid, ArrayList<Integer> cptIdList) throws Exception{
        ResultSet rs;
        ArrayList<Integer> value = new ArrayList<Integer>();
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();

            	queryStr.append(" SELECT person_id, condition_start_date as START_TIME " );
            	queryStr.append(" FROM condition_occurrence co_d ");
            	queryStr.append(" WHERE condition_concept_id IN ("+ImedStringFormat.tranListIn(cptIdList)+") AND person_id ="+pid);
            	queryStr.append(" ORDER BY condition_start_date LIMIT 1");            	            	
            	logger.info("Query getPatientDisFeature "+queryStr.toString()+"\n");
                rs = stmt.executeQuery(queryStr.toString());
                
                if (rs.next()) {
                   ResultSet fs;
                   queryStr = new StringBuffer();
                   Date IdxDisStart = rs.getTimestamp("START_TIME");                  
                   queryStr.append(" SELECT DISTINCT  condition_concept_id FROM condition_occurrence "); 
                   queryStr.append(" WHERE person_id = "+pid+" AND condition_start_date <= '"+ new ImedDateFormat().format(IdxDisStart)+"'");
                  
                   logger.info("Query getPatientDisFeature "+queryStr.toString()+"\n");
                   fs = stmt.executeQuery(queryStr.toString());
                   while(fs.next()){
                	   value.add( fs.getInt("condition_concept_id"));                	   
                   }
                   fs.close();
                }
        
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
        return value;
	}
	
	public static void getDisSemanticConcept(HashMap<Integer, String> cptmap) throws Exception{
        ResultSet rs = null;
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();
            	String cptkeys = cptmap.keySet().toString();
            	cptkeys = cptkeys.substring(1, cptkeys.length()-1);
            	queryStr.append(" SELECT concept_id, concept_name ");
            	queryStr.append(" FROM  vocabulary.concept ");
            	queryStr.append(" WHERE concept_id in ("+cptkeys+")");

                   rs = stmt.executeQuery(queryStr.toString());
                   while(rs.next()){
                	   cptmap.put(rs.getInt("concept_id"), rs.getString("concept_name"));            	   
                   }
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
   
	}
//	public static String tranListIn(ArrayList<Integer> lst){
//		StringBuffer str = new StringBuffer();
//		
//		for(Integer id: lst){
//			str.append(id+",");
//		}
//		str.delete(str.lastIndexOf(","), str.length());
//		return str.toString();
//	}
	public static void main(String[] args) throws FileNotFoundException {
		
		ImedDB.logger.info("test");
	}
}
