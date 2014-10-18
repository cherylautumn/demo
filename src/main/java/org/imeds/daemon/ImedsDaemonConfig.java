package org.imeds.daemon;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.imeds.data.sampleConfig;
import org.imeds.util.OSValidator;

public class ImedsDaemonConfig {
	
	 private static String omopDbName ="db_OMOPS";
	 private static String omopDbDriver;
	 private static String omopDbUrl;
	 private static String omopDbUser;
	 private static String omopDbPassword;
	 private static String omopDbSearchPath;
	 private static ArrayList<String> patientFeatureExpFolders = new ArrayList<String>();  	
	 private static ArrayList<String> pearsonOutlierExpFolders = new ArrayList<String>();  
	 private static ArrayList<String> seqPtnPrepareFolders = new ArrayList<String>();
	 public static String getOmopDbName() {
		return omopDbName;
	}
	public static void setOmopDbName(String omopDbName) {
		ImedsDaemonConfig.omopDbName = omopDbName;
	}
	public static String getOmopDbDriver() {
		return omopDbDriver;
	}
	public static void setOmopDbDriver(String omopDbDriver) {
		ImedsDaemonConfig.omopDbDriver = omopDbDriver;
	}
	public static String getOmopDbUrl() {
		return omopDbUrl;
	}
	public static void setOmopDbUrl(String omopDbUrl) {
		ImedsDaemonConfig.omopDbUrl = omopDbUrl;
	}
	public static String getOmopDbUser() {
		return omopDbUser;
	}
	public static void setOmopDbUser(String omopDbUser) {
		ImedsDaemonConfig.omopDbUser = omopDbUser;
	}
	public static String getOmopDbPassword() {
		return omopDbPassword;
	}
	public static void setOmopDbPassword(String omopDbPassword) {
		ImedsDaemonConfig.omopDbPassword = omopDbPassword;
	}
	public static String getOmopDbSearchPath() {
		return omopDbSearchPath;
	}
	public static void setOmopDbSearchPath(String omopDbSearchPath) {
		ImedsDaemonConfig.omopDbSearchPath = omopDbSearchPath;
	}
	public static ArrayList<String> getPatientFeatureExpFolders() {
		return patientFeatureExpFolders;
	}
	public static void setPatientFeatureExpFolders(
		ArrayList<String> patientFeatureExpFolders) {
		ImedsDaemonConfig.patientFeatureExpFolders = patientFeatureExpFolders;
	}
	public static ArrayList<String> getPearsonOutlierExpFolders() {
		return pearsonOutlierExpFolders;
	}
	public static void setPearsonOutlierExpFolders(
		ArrayList<String> pearsonOutlierExpFolders) {
		ImedsDaemonConfig.pearsonOutlierExpFolders = pearsonOutlierExpFolders;
	}
	public static ArrayList<String> getSeqPtnPrepareFolders() {
		return seqPtnPrepareFolders;
	}
	public static void setSeqPtnPrepareFolders(
			ArrayList<String> seqPtnPrepareFolders) {
		ImedsDaemonConfig.seqPtnPrepareFolders = seqPtnPrepareFolders;
	}
	public ImedsDaemonConfig() {
		// TODO Auto-generated constructor stub
	}
	public static void loadImedsDaemonConfig(String fileName) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element L0 = document.getRootElement();
			for (Iterator L1 = L0.elementIterator(); L1.hasNext();) {
				Element L1_emt = (Element) L1.next();
				if(L1_emt.getName().equals("dbConfig")){
					
					omopDbDriver		= L1_emt.element(omopDbName).elementText("db_Driver");
					omopDbUrl		= L1_emt.element(omopDbName).elementText("dbURL");
					omopDbUser		= L1_emt.element(omopDbName).elementText("dbUser");
					omopDbPassword	= L1_emt.element(omopDbName).elementText("dbPassword");
					omopDbSearchPath	= L1_emt.element(omopDbName).elementText("search_path");
					
				}else if(L1_emt.getName().equals("expConfig")){
					List<Element> expList = L1_emt.element("patientFeaturePrepare").elements("folder");
					getFolderList(expList, patientFeatureExpFolders);
					expList = L1_emt.element("pearsonOutlier").elements("folder");
					getFolderList(expList,pearsonOutlierExpFolders);
					
					expList = L1_emt.element("seqPtnPrepare").elements("folder");
					getFolderList(expList,seqPtnPrepareFolders);
					
//					for(Element ele: expList){
//						String folderName=ele.getText();
//						
//						if(!OSValidator.isWindows()){folderName = folderName.replace("\\", "/");}
//						patientFeatureExpFolders.add(folderName);											
//					}
					
					
				}else{
					
				}
			}
			
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public static void getFolderList(List<Element> expList, List folderlist){
		
		for(Element ele: expList){
			String folderName=ele.getText();
			
			if(!OSValidator.isWindows()){folderName = folderName.replace("\\", "/");}
			folderlist.add(folderName);											
		}
	}
	public static String getConfigString() {
		 
		return	"omopDbName	= " + omopDbName + "\n" + 
				"omopDbDriver	= " + omopDbDriver + "\n" +
				"omopDbUrl	= " + omopDbUrl + "\n" +
				"omopDbUser	= " + omopDbUser	+ "\n" +
				"omopDbPassword	= " + omopDbPassword + "\n" +
				"omopDbSearchPath= "+ omopDbSearchPath + "\n" ;
	}

}
