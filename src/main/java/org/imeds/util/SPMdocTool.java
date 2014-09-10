package org.imeds.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.imeds.data.ComorbidDataSetConfig;
import org.imeds.feature.selection.MMRFSConfig;
import org.imeds.feature.selection.basicItemsets;
import org.imeds.feature.selection.discrimItemsets;
import org.imeds.feature.selection.labelItemsets;

public class SPMdocTool  implements DocumentTool{ 

	public SPMdocTool() {
		// TODO Auto-generated constructor stub
	}

	/********************
	 *  Write			*	
	 * 					*
	 ********************/
	public void createDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}
	public void createFeatureFile(String fileName, ArrayList<discrimItemsets>  arrayList) {
		
		 FileWriter fstream;
		try {
				fstream = new FileWriter(fileName);
		
		      BufferedWriter out = new BufferedWriter(fstream);
		      for(discrimItemsets row:arrayList){
					 for(TreeSet<Integer> ri:row.getItemsets().getItemsets()){
						 String setstr = ri.toString();
						 setstr = setstr.substring(setstr.indexOf("[")+1,setstr.indexOf("]")).replace(",","");
						 
						 out.write(setstr+" -1 ");
					 }
					 out.write(" GAIN: "+row.getGain());
					 out.newLine();
				}
		      //Close the output stream
		      out.close();    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/********************
	 *  Read			*	
	 * 					*
	 ********************/
	public void parserDoc(String fileName) {
		// TODO Auto-generated method stub
		
	}
	public void parserConfigDoc(String fileName,MMRFSConfig cdsc) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element L0 = document.getRootElement();
			for (Iterator L1 = L0.elementIterator(); L1.hasNext();) {
				Element L1_emt = (Element) L1.next();
				if(L1_emt.getName().equals("MMRFSConfig")){
					
					cdsc.setBasicItemsetsFileName(L1_emt.element("basicItemsetsFileName").getText());
					cdsc.setDiscrimItemsetsFileName(L1_emt.element("discrimItemsetsFileName").getText());
					cdsc.setLabelDefineThreshold(Double.parseDouble(L1_emt.element("labelDefineThreshold").getText().trim()));
					cdsc.setCoverageRate(Double.parseDouble(L1_emt.element("coverageRate").getText().trim()));
					cdsc.setOutlierSource(L1_emt.element("outlierSource").getText().trim());
					cdsc.setOutlierThreshold(Double.parseDouble(L1_emt.element("outlierThreshold").getText().trim()));
					cdsc.setFeatureItemsetFileName(L1_emt.element("featureItemsetFileName").getText().trim());
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
	public void parserDiscrimDoc(String FileName, ArrayList<discrimItemsets> discrimSeqList) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(FileName);
			Scanner scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				
				String line =scanner.nextLine();
				line = line.substring(0, line.indexOf("SUP"));
				basicItemsets<Integer> itemsets = new  basicItemsets<Integer>();
				genItemsets(line,itemsets);
				
				if(itemsets.getItemsets().size()>0){
					discrimItemsets oneSeq = new discrimItemsets();
					oneSeq.setItemsets(itemsets);
					discrimSeqList.add(oneSeq);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void genItemsets(String line, basicItemsets<Integer> oneSeq){
		
		 oneSeq.setItemsets(new ArrayList<TreeSet<Integer>>());
		String[] itemsets = line.split("-1");
		for(String itemset:itemsets){
			if(itemset!=null && itemset.trim()!=null && !itemset.trim().equals("")){
				String[] items=itemset.split(" ");
				TreeSet<Integer>set = new TreeSet<Integer>();
				for(String item:items){
					if(item !=null && item.trim()!=null && !item.trim().equals("")){
						
						set.add(Integer.parseInt(item.trim()));
					}
				}
			
				oneSeq.setItemset(set);
			}
		}
		
		
	}
	public void parserLabelDoc(String FileName, ArrayList<labelItemsets> SeqList, HashMap<Long, Integer> labelList) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(FileName);
			Scanner scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				
				String line =scanner.nextLine();
				String[] id_itemsets = line.split(",");
				Long id = Long.parseLong(id_itemsets[0].trim());
				basicItemsets<Integer> itemsets = new  basicItemsets<Integer>(id);
				genItemsets(id_itemsets[1],itemsets);
				if(labelList.containsKey(id)){
					labelItemsets labelItems = new labelItemsets();
					labelItems.setItemsets(itemsets);
					labelItems.setLabel(labelList.get(id));
					SeqList.add(labelItems);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
