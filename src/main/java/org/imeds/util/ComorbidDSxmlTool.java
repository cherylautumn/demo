package org.imeds.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.imeds.data.ComorbidDataSetConfig;

public class ComorbidDSxmlTool implements DocumentTool{ 
	
	public ComorbidDSxmlTool() {	
	}
	public void createDoc(String fileName) {
//		Document document = DocumentHelper.createDocument();
//		Element employees = document.addElement("employees");
//		Element employee = employees.addElement("employee");
//		Element name = employee.addElement("name");
//		name.setText("ddvip");
//		Element sex = employee.addElement("sex");
//		sex.setText("m");
//		Element age = employee.addElement("age");
//		age.setText("29");
//		try {
//			Writer fileWriter = new FileWriter(fileName);
//			XMLWriter xmlWriter = new XMLWriter(fileWriter);
//			xmlWriter.write(document);
//			xmlWriter.close();
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
	}

	public void parserDoc(String fileName,ComorbidDataSetConfig cdsc) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element L0 = document.getRootElement();
			for (Iterator L1 = L0.elementIterator(); L1.hasNext();) {
				Element L1_emt = (Element) L1.next();
				if(L1_emt.getName().equals("Index_Diagnoses")){
					cdsc.setIndex_diagnoses(L1_emt.elements("Diagnose"));
					//System.out.println(this.cdsc.getIndex_diagnoses().toString());
				}else if(L1_emt.getName().equals("target")){
					cdsc.setTargetFileName(L1_emt.element("fileName").getText());
					cdsc.setColList(L1_emt.element("columns").elements("col"));
					//getTarget(L1_emt);
				}else if(L1_emt.getName().equals("DataSetParas")){
					cdsc.setSample_sets(L1_emt.elements("sampleConfig"));
					
				}else if(L1_emt.getName().equals("PearsonResidualOutlier")){
					cdsc.setPearsonResidualOutlierInputFolder(L1_emt.element("inputFolder").getText());
					cdsc.setPearsonResidualOutlierOutputFolder(L1_emt.element("outputFolder").getText());
					cdsc.setPearsonResidualThreshold(Double.parseDouble(L1_emt.element("threshold").getText()));
					cdsc.setSparkLRmodelParas(L1_emt.element("SparkLRmodelParas").elements("para"));
					cdsc.setSparkLRmodelDataSets(L1_emt.element("SparkLRmodelDataSets").elements("dataset"));
		
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
	
	public void parserDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}
	
}