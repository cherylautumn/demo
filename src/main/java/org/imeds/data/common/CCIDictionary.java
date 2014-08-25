package org.imeds.data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imeds.util.CCIcsvTool;

public class CCIDictionary {
	private String CCIstandard;
	private String CCIfileName;
	protected CCIcsvTool csvCCI = new CCIcsvTool();
	private HashMap<String, CCIcode> codeList = new HashMap<String, CCIcode>();
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
	
	public void buildDictionary(){
		this.csvCCI.parserDoc(this.CCIfileName, this.codeList);
		Iterator iter = codeList.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
//		    Object key = entry.getKey(); 
		    CCIcode code = (CCIcode) entry.getValue(); 
			code.tranIcdList();
//		    System.out.println(entry.toString());
		} 
		
//		System.out.println(this.codeList.toString());
	}
	
	
}
