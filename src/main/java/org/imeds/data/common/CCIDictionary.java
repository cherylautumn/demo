package org.imeds.data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.imeds.db.ImedDB;
import org.imeds.util.CCIcsvTool;

public class CCIDictionary {
	private String CCIstandard;
	private String CCIfileName;
	protected CCIcsvTool csvCCI = new CCIcsvTool();
	private HashMap<String, CCIcode> codeList = new HashMap<String, CCIcode>();
	private HashMap<Integer,Integer> cptCat = new HashMap<Integer, Integer>();
	public CCIDictionary() {
		// TODO Auto-generated constructor stub
	}
	public CCIDictionary(String fileName) {
		// TODO Auto-generated constructor stub
		this.CCIfileName = fileName;
	}
	public String getCCIstandard() {
		return CCIstandard;
	}
	public void setCCIstandard(String cCIstandard) {
		CCIstandard = cCIstandard;
	}
	public String getCCIfileName() {
		return CCIfileName;
	}
	public void setCCIfileName(String cCIfileName) {
		CCIfileName = cCIfileName;
	}
	public HashMap<String, CCIcode> getCodeList() {
		return codeList;
	}
	public void setCodeList( HashMap<String, CCIcode> codeList) {
		this.codeList = codeList;
	}
	
	public HashMap<Integer, Integer> getCptCat() {
		return cptCat;
	}
	public void setCptCat(HashMap<Integer, Integer> cptCat) {
		this.cptCat = cptCat;
	}
	public void buildDictionary(){
		this.csvCCI.parserDoc(this.CCIfileName, this.codeList);
		Iterator<Entry<String, CCIcode>> iter = codeList.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Entry<String, CCIcode> entry = iter.next(); 
//		    Object key = entry.getKey(); 
		    CCIcode code = (CCIcode) entry.getValue(); 
			code.tranIcdList();
//		    System.out.println(entry.toString());
		} 
		
//		System.out.println(this.codeList.toString());
	}
	
	public void buildCptMap() throws Exception{
		Iterator<Entry<String, CCIcode>> iter = codeList.entrySet().iterator(); 
		ArrayList<Integer> cptTmp;
		while (iter.hasNext()) { 
			Entry<String, CCIcode> entry = iter.next(); 
		    CCIcode code = (CCIcode) entry.getValue(); 
			
		    String range = getRangeStr(code.getIcdDouble());
		    System.out.println(code.getID()+" "+code.getName());	
		    cptTmp = ImedDB.getCptCatMap(range);		    
		    code.setIcdCptId(cptTmp);
		    for(Integer cid:cptTmp) this.cptCat.put(cid, code.getID());
//		    System.out.println(entry.toString());
		} 
	}
	
	public String getRangeStr(ArrayList<IcdPair> pair){
//		 WHERE (source_concept_code >= '250.00' AND source_concept_code <= '250.39' ) OR
//	 		(source_concept_code >= '250.70' AND source_concept_code <= '250.79')\
		StringBuffer str = new StringBuffer();
		for(IcdPair ipr:pair){
			str.append(" (source_concept_code >= '"+ipr.getStart()+"' AND source_concept_code < '"+ipr.getEnd()+"')	OR ");			
		}
		
		str.delete(str.lastIndexOf("OR"), str.length()-1);
	
		return str.toString();
		
	}
	
	
}
