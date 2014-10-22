package org.imeds.seqmining;

import java.util.ArrayList;

import org.imeds.data.common.CCIDictionary;

public class SequenceDataSetConfig {
	
	private boolean enable=false;
	private String inputFolder; //seqDataset input folder, preSeq
	private String outputFolder; //seqDataset output folder, seqDS
	private boolean VMSPenable=false;
	private ArrayList<Integer> VMSPMaxLen = new ArrayList<Integer>();
	private ArrayList<Double> VMSPthreshold = new ArrayList<Double>();
	private String VMSPinputFolder;
	private String VMSPoutputFolder;
	
	private boolean MMRFSenable=false;
	private String MMRFSbasicItemsetsFolder;
	private String MMRFSdiscrimItemsetsFolder;
	private String MMRFSoutlierSourceFolder;
	private ArrayList<Double> MMRFSlabelDefineThreshold = new ArrayList<Double>();
	private ArrayList<Double> MMRFScoverage = new ArrayList<Double>();		
	private String MMRFSfeatureItemsetFolder;
	
	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getVMSPinputFolder() {
		return VMSPinputFolder;
	}

	public void setVMSPinputFolder(String vMSPinputFolder) {
		VMSPinputFolder = vMSPinputFolder;
	}

	public String getVMSPoutputFolder() {
		return VMSPoutputFolder;
	}

	public void setVMSPoutputFolder(String vMSPoutputFolder) {
		VMSPoutputFolder = vMSPoutputFolder;
	}

	public ArrayList<Double> getVMSPthreshold() {
		return VMSPthreshold;
	}

	public void setVMSPthreshold(ArrayList<Double> vMSPthreshold) {
		VMSPthreshold = vMSPthreshold;
	}
	public void addVMSPthreshold(Double vMSPthreshold) {
		this.VMSPthreshold.add(vMSPthreshold);
	}

	public ArrayList<Integer> getVMSPMaxLen() {
		return VMSPMaxLen;
	}

	public void setVMSPMaxLen(ArrayList<Integer> vMSPMaxLen) {
		VMSPMaxLen = vMSPMaxLen;
	}
	public void addVMSPMaxLen(Integer MaxLen) {
		this.VMSPMaxLen.add(MaxLen);
	}
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getVMSPenable() {
		return VMSPenable;
	}

	public void setVMSPenable(Boolean vMSPenable) {
		VMSPenable = vMSPenable;
	}

	public boolean isMMRFSenable() {
		return MMRFSenable;
	}

	public void setMMRFSenable(boolean mMRFSenable) {
		MMRFSenable = mMRFSenable;
	}

	public String getMMRFSbasicItemsetsFolder() {
		return MMRFSbasicItemsetsFolder;
	}

	public void setMMRFSbasicItemsetsFolder(String mMRFSbasicItemsetsFolder) {
		MMRFSbasicItemsetsFolder = mMRFSbasicItemsetsFolder;
	}

	public String getMMRFSdiscrimItemsetsFolder() {
		return MMRFSdiscrimItemsetsFolder;
	}

	public void setMMRFSdiscrimItemsetsFolder(String mMRFSdiscrimItemsetsFolder) {
		MMRFSdiscrimItemsetsFolder = mMRFSdiscrimItemsetsFolder;
	}

	public String getMMRFSoutlierSourceFolder() {
		return MMRFSoutlierSourceFolder;
	}

	public void setMMRFSoutlierSourceFolder(String mMRFSoutlierSourceFolder) {
		MMRFSoutlierSourceFolder = mMRFSoutlierSourceFolder;
	}

	public ArrayList<Double> getMMRFSlabelDefineThreshold() {
		return MMRFSlabelDefineThreshold;
	}

	public void setMMRFSlabelDefineThreshold(
			ArrayList<Double> mMRFSlabelDefineThreshold) {
		MMRFSlabelDefineThreshold = mMRFSlabelDefineThreshold;
	}
	public void addMMRFSlabelDefineThreshold(Double mMRFSlabelDefineThreshold) {
		this.MMRFSlabelDefineThreshold.add(mMRFSlabelDefineThreshold);
	}

	public ArrayList<Double> getMMRFScoverage() {
		return MMRFScoverage;
	}

	public void setMMRFScoverage(ArrayList<Double> mMRFScoverage) {
		MMRFScoverage = mMRFScoverage;
	}
	public void addMMRFScoverage(Double mMRFScoverage) {
		this.MMRFScoverage.add(mMRFScoverage);
	}

	public String getMMRFSfeatureItemsetFolder() {
		return MMRFSfeatureItemsetFolder;
	}

	public void setMMRFSfeatureItemsetFolder(String mMRFSfeatureItemsetFolder) {
		MMRFSfeatureItemsetFolder = mMRFSfeatureItemsetFolder;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setVMSPenable(boolean vMSPenable) {
		VMSPenable = vMSPenable;
	}

	public SequenceDataSetConfig() {
		// TODO Auto-generated constructor stub
	}

}
