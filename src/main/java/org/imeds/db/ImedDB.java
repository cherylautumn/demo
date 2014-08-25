package org.imeds.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.imeds.data.common.CCIDictionary;
import org.imeds.util.ImedDateFormat;

public class ImedDB {

	public ImedDB() {
		// TODO Auto-generated constructor stub
	}
	protected static Connection conn = null;
	protected static Statement stmt = null;
	

	public static void connDB(String dbDriver, String dbURL, String dbUser, String dbPassword) throws Exception {
	     
		try{
	            Class.forName(dbDriver);
	       
	            conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
	            System.out.println("SQL Connection to database established!");
	            stmt = conn.createStatement();
	            System.out.println("SQL Statement is prepared!");
	     } catch (SQLException e) {
	            System.out.println("Connection Failed! Check output console");
	            throw e;
	     }
	        
	  }
	public static void closeDB() {

	        try
	        {
	            if(conn != null)conn.close();            
	            if(stmt != null) stmt.close();
	            System.out.println("Connection closed !!");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}  

	public static ArrayList<Integer> getCptCatMap(String range) throws Exception{
        ResultSet rs;
        ArrayList<Integer> value = null;
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();
            	queryStr.append(" SELECT DISTINCT concept_id FROM condition_vocab_cache WHERE " + range);            	
            	System.out.printf("Query Str :%s\n", queryStr.toString() );
                rs = stmt.executeQuery(queryStr.toString());
        
                while (rs.next()) {
                   value.add(rs.getInt("concept_id"));
                }
        
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
        return value;
	}
	
	public static  HashMap<Integer, ArrayList<Double>> getPatientsWithIndexDiagnose(ArrayList<Integer> cptIdList, ArrayList<String> colList) throws Exception{
        ResultSet rs;
        HashMap<Integer, ArrayList<Double>> value = new  HashMap<Integer, ArrayList<Double>>();
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();
            	
            	queryStr.append(" SELECT DISTINCT p.person_id as ID, p.gender_concept_id as Gender, year_of_birth as Age, race_concept_id as Race, ethnicity_concept_id as Ethnicity, location_id as Location, death.person_id as Label ");            	
            	queryStr.append(" FROM condition_occurrence co, person p LEFT OUTER JOIN death  ON (p.person_id = death.person_id) ");
            	queryStr.append(" WHERE p.person_id = co.person_id AND condition_concept_id IN ("+tranListIn(cptIdList)+")");
//           	queryStr.append(" limit 1000");
            	System.out.printf("Query Str :%s\n", queryStr.toString() );
                rs = stmt.executeQuery(queryStr.toString());
               
                while (rs.next()) {
                   ArrayList<Double> tmp = new ArrayList<Double>();
 
                   tmp.add(rs.getDouble("ID"));
        		   if(rs.getInt("Label")>0) tmp.add((double) 1);
                   else tmp.add((double) 0);
        		   if(colList.contains("Gender")) tmp.add(rs.getDouble("Gender"));
        		   if(colList.contains("Age")) tmp.add(rs.getDouble("Age"));
        		   if(colList.contains("Race")) tmp.add(rs.getDouble("Race"));
        		   if(colList.contains("Ethinity")) tmp.add(rs.getDouble("Ethinity"));
        		   if(colList.contains("Location")) tmp.add(rs.getDouble("Location"));
                   
                  value.put(rs.getInt("ID"), tmp);
                }
        
                rs.close();
            }
        }catch (Exception ex) {
            throw ex;
        }
        return value;
	}
	
	public static ArrayList<Integer> getPatientDisFeature(Integer pid, ArrayList<Integer> cptIdList) throws Exception{
        ResultSet rs;
        ArrayList<Integer> value = new ArrayList<Integer>();
        try {
            synchronized (ImedDB.class) {
            	StringBuffer queryStr = new StringBuffer();

            	queryStr.append(" SELECT person_id, condition_start_date as START_TIME " );
            	queryStr.append(" FROM condition_occurrence co_d ");
            	queryStr.append(" WHERE condition_concept_id IN ("+tranListIn(cptIdList)+") AND person_id ="+pid);
            	queryStr.append(" ORDER BY condition_start_date LIMIT 1");            	            	
            	System.out.printf("Query Str :%s\n", queryStr.toString() );
                rs = stmt.executeQuery(queryStr.toString());
                
                if (rs.next()) {
                   ResultSet fs;
                   queryStr = new StringBuffer();
                   Date IdxDisStart = rs.getTimestamp("START_TIME");                  
                   queryStr.append(" SELECT DISTINCT  condition_concept_id FROM condition_occurrence "); 
                   queryStr.append(" WHERE person_id = "+pid+" AND condition_start_date <= '"+ new ImedDateFormat().format(IdxDisStart)+"'");
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
	
	
	public static String tranListIn(ArrayList<Integer> lst){
		StringBuffer str = new StringBuffer();
		
		for(Integer id: lst){
			str.append(id+",");
		}
		str.delete(str.lastIndexOf(","), str.length());
		return str.toString();
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dbDriver = "org.postgresql.Driver";
		String dbURL = "jdbc:postgresql://omop-datasets.cqlmv7nlakap.us-east-1.redshift.amazonaws.com:5439/truven";
		String dbUser = "hchiu";
		String dbPassword = "1QAZ2wsx";
		
		connDB(dbDriver, dbURL,dbUser, dbPassword);
		closeDB();
	}

}
