package org.imeds.data;

import java.util.ArrayList;

public class DataSetConfig {
	
	private String targetFileName="";
	protected ArrayList<String> colList=new ArrayList<String>();
	

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public ArrayList<String> getColList() {
		return colList;
	}

	public void setColList(ArrayList<String> colList) {
		this.colList = colList;
	}

	public DataSetConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DataSetConfig [ targetFileName=" + targetFileName + ", colList=" + colList
				+ "]";
	}

}
