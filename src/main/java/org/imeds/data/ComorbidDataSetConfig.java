package org.imeds.data;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class ComorbidDataSetConfig extends DataSetConfig {
	private ArrayList<String> index_diagnoses = new ArrayList<String>();
	public ArrayList<String> getIndex_diagnoses() {
		return index_diagnoses;
	}

	public void setIndex_diagnoses(ArrayList<String> index_diagnoses) {
		this.index_diagnoses = index_diagnoses;
	}
	public void setIndex_diagnoses(List<Element> index_diagnoses) {
		for(Element ele:index_diagnoses){
			this.index_diagnoses.add(ele.getText());
		}
	//	this.index_diagnoses = index_diagnoses;
	}
	public void setColList(List<Element> colList) {
		for(Element ele: colList){
			this.colList.add(ele.getText());
			for(String idx_dia:this.index_diagnoses){
				if(idx_dia.equals(ele.getText())){
					this.colList.remove(idx_dia);
					break;
				}
			}			
		}
		
		
	}
	public ComorbidDataSetConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return super.toString()+ "\n ComorbidDataSetConfig [index_diagnoses=" + index_diagnoses
				+ "]";
	}

}
