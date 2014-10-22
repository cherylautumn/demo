package org.imeds.seqmining;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.imeds.daemon.SeqptnManager;
import org.imeds.data.Worker;
import org.imeds.feature.selection.MMRFSConfig;
import org.imeds.feature.selection.MMRFSworker;
import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;
import org.imeds.util.ImedDateFormat;
import org.imeds.util.OSValidator;
import org.imeds.util.SPMdocTool;
import org.imeds.util.writeException;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Item;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.ItemFactory;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.spam.AlgoVMSP;

//This Worker is to transform patient druglist in vertical into horizontal
public class SequenceDataSetWorker extends Worker {
	
	private SequenceDataSetConfig cdsc = new SequenceDataSetConfig();
	private SPMdocTool cfgparser = new SPMdocTool();

	private String configFile="";
	private String folderpath="";
	private Logger logger = Logger.getLogger(SeqptnManager.class);
	public SequenceDataSetWorker() {
		// TODO Auto-generated constructor stub
	}
	public SequenceDataSetWorker(String configFile) {
		this.configFile = configFile;
	}
	public SequenceDataSetWorker(String folderpath,String configFile, Logger logger) {
		this.folderpath = folderpath;
		this.configFile = folderpath+configFile;
		this.logger = logger;
	}
	public SequenceDataSetConfig getCdsc() {
		return this.cdsc;
	}

	public void setCdsc(SequenceDataSetConfig cdsc) {
		this.cdsc = cdsc;
	}
	@Override
	public void prepare() {
		
		//Initialize config file
		this.cfgparser.parserDoc(this.configFile,this.cdsc);

	}

	@Override
	public void ready() {
		// STAGE 3. transform data format for SPM
		String inputfolder = this.folderpath+this.cdsc.getInputFolder();
		String outputfolder = this.folderpath+this.cdsc.getOutputFolder();
		
		File directory = new File(inputfolder);
		File[] fList = directory.listFiles();
		for (File file : fList){		
			if (file.isFile()){
				logger.info("Prepare SPM data format for "+file.getAbsolutePath());
				ArrayList<ArrayList<String>> DataPointList = new ArrayList<ArrayList<String>>();
				
				CCIcsvTool.preSequenceDataParserDoc(inputfolder+OSValidator.getPathSep()+file.getName(),DataPointList);
				HashMap<Long, HashMap<Date, Itemset>> sequences = processSequence(DataPointList);
				
				
				HashMap<Long, ArrayList<String>> seqList = transSequencesToList(sequences);
				CCIcsvTool.SequenceDataSetCreateDoc(outputfolder+OSValidator.getPathSep()+"seq_"+file.getName(), seqList);
//				ArrayList<ArrayList<String>> seqTocsv = new ArrayList<ArrayList<String>>(seqList.values());
				
//				ArrayList<ArrayList<String>> seqTocsv = transSequencesToList(sequences);
//				CCIcsvTool.SequenceDataSetCreateDoc(this.cdsc.getOutputFolder()+"\\seq_"+file.getName(), seqTocsv); 
			}
		}
	}

	public  HashMap<Long, HashMap<Date, Itemset>> processSequence(ArrayList<ArrayList<String>> DataPointList) {	    
	    HashMap<Long, HashMap<Date, Itemset>> sequences = new  HashMap<Long, HashMap<Date, Itemset>>();
	    ItemFactory<Integer> itemFactory = new ItemFactory<Integer>();
	    
		for(ArrayList<String> row: DataPointList){

			try {
				Long Id = Long.parseLong(row.get(0));
				Date itemDate = new ImedDateFormat().parse(row.get(1));
			
				Item item = itemFactory.getItem(Integer.parseInt(row.get(2)));
	
				if(!sequences.containsKey(Id)){
					sequences.put(Id, new HashMap<Date, Itemset>());
				}
				
				HashMap<Date, Itemset> itemset = sequences.get(Id);
				if(!itemset.containsKey(itemDate)) itemset.put(itemDate, new Itemset());
				
				Itemset its = itemset.get(itemDate);
				its.addItem(item);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sequences;
	}

	public  HashMap<Long,ArrayList<String>> transSequencesToList(HashMap<Long, HashMap<Date, Itemset>> sequences ){
		 HashMap<Long, ArrayList<String>> csvlist = new HashMap<Long,ArrayList<String>>();
		Iterator<Entry<Long, HashMap<Date, Itemset>>> iter = sequences.entrySet().iterator();
		
		while (iter.hasNext()) {
			
			Entry<Long, HashMap<Date, Itemset>> sequence = iter.next();			
			HashMap<Date, Itemset> itemSets =  sequence.getValue();
			Map<Date, Itemset> itemTree = new TreeMap<Date, Itemset>(itemSets);
			Iterator<Entry<Date,Itemset>> itemIter = itemTree.entrySet().iterator();
			
			ArrayList<String> itemStr = new ArrayList<String>();
			
			while(itemIter.hasNext()){
				Entry<Date, Itemset> item = itemIter.next();				
//				itemStr.add("<"+new ImedDateFormat().format(item.getKey())+"> ");
				itemStr.add(item.getValue().toString());
				itemStr.add("-1 ");
			}
			itemStr.add("-2");
			csvlist.put(sequence.getKey(), itemStr);
		}
		
		return csvlist;
	}
	
	@Override
	public void go() {
		// STAGE 4. Do VMSP SeqPtn Mining
		
		String inputfolder = this.folderpath+this.cdsc.getVMSPinputFolder();
		String outputfolder = this.folderpath+this.cdsc.getVMSPoutputFolder();
		
		File directory = new File(inputfolder);
		File[] fList = directory.listFiles();
		AlgoVMSP algo = new AlgoVMSP();
		try{
			for (File file : fList){			
				if (file.isFile()){
				
					String filename = file.getName();
					String input = inputfolder+OSValidator.getPathSep()+file.getName();
					String output ="";
					if(!filename.contains("_withId")){
						
						for(Double threshold:this.cdsc.getVMSPthreshold()){
							if(this.cdsc.getVMSPMaxLen().size()>0){
								for(Integer maxlen:this.cdsc.getVMSPMaxLen()){
									
									algo.setMaximumPatternLength(maxlen);
									output = outputfolder+OSValidator.getPathSep()+file.getName().substring(0, filename.indexOf("."))+"_VMSP_"+threshold+"_"+maxlen+".txt";
									logger.info("SPMing "+output);
									
										algo.runAlgorithm(input, output,threshold );
									
								}
							}else{
								output = outputfolder+OSValidator.getPathSep()+file.getName().substring(0, filename.indexOf("."))+"_VMSP_"+threshold+".txt";
								logger.info("SPMing "+output);
								algo.runAlgorithm(input, output,threshold );
							}
							
							algo.printStatistics();
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("fail to complete SPM"+ writeException.toString(e));
		}
	}
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		 URL url = SequenceDataSetWorker.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
	@Override
	public void done() {
		// STAGE 6. Find discriminate pattern
		ArrayList<MMRFSConfig> mfscg=genMMRFSConfigs();
		//for(MMRFSConfig mf:mfscg)System.out.println(mf);
		for(MMRFSConfig mcfg: mfscg){
			MMRFSworker mrfw = new MMRFSworker( mcfg);
			logger.info("mrfw.prepare");
			mrfw.prepare();
			logger.info("mrfw.ready");
			mrfw.ready();
			logger.info("mrfw.go");
			mrfw.go();
			logger.info("mrfw.DONE");
			mrfw.done();
		}
	}
	public ArrayList<MMRFSConfig> genMMRFSConfigs(){
		ArrayList<MMRFSConfig> cfgList = new ArrayList<MMRFSConfig>();
		String basicItemsetsFolder = this.folderpath+this.cdsc.getMMRFSbasicItemsetsFolder();
		String discrimItemsetsFolder = this.folderpath+this.cdsc.getMMRFSdiscrimItemsetsFolder();
		String outlierSourceFolder = this.folderpath+this.cdsc.getMMRFSoutlierSourceFolder();
		String featureItemsetsFolder = this.folderpath+this.cdsc.getMMRFSfeatureItemsetFolder();
		
		File directory = new File(basicItemsetsFolder);
		File[] fList = directory.listFiles();
		
			for (File file : fList){			
				if (file.isFile()){
				
					String filename = file.getName();
					if(filename.contains("_withId")){
					  
					   int beginIndex = filename.indexOf("_")+1;
					   int endIndex = filename.lastIndexOf("_preseq");
					   String keyname = filename.substring(beginIndex, endIndex);
					  
					   String basicItemsetsFileName = basicItemsetsFolder+OSValidator.getPathSep()+file.getName();
					   String oulierSourceFileName = outlierSourceFolder+OSValidator.getPathSep()+keyname+"_prol.csv";
					   	File freqSeqdir = new File(discrimItemsetsFolder);
						File[] sList = freqSeqdir.listFiles();
						for (File sfile : sList){			
							if (sfile.isFile() && sfile.getName().contains(keyname)){
								String discrimItemsetsFileName = discrimItemsetsFolder+OSValidator.getPathSep()+sfile.getName();
								for(Double thrd:this.cdsc.getMMRFSlabelDefineThreshold()){
									for(Double cvg:this.cdsc.getMMRFScoverage()){
										String outFilename = sfile.getName().substring(0, sfile.getName().lastIndexOf("."))+"_dsmc_"+thrd+"_"+cvg+".csv";
										String featureItemsesFilename = featureItemsetsFolder+OSValidator.getPathSep()+outFilename;
										MMRFSConfig mfcg = new MMRFSConfig();
										mfcg.setBasicItemsetsFileName(basicItemsetsFileName);
										mfcg.setOutlierSource(oulierSourceFileName);
										mfcg.setDiscrimItemsetsFileName(discrimItemsetsFileName);
										mfcg.setFeatureItemsetFileName(featureItemsesFilename);
										mfcg.setLabelDefineThreshold(thrd);
										mfcg.setCoverageRate(cvg);
										cfgList.add(mfcg);
										
									}
								}
							}
						}
					}
				}
			}
		
		return cfgList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	SequenceDataSetWorker sdsw = new SequenceDataSetWorker("data\\IMEDS\\DiabeteComorbidDS\\DSConfig.xml");
	//	sdsw.prepare();
	//	sdsw.ready();
	}
	

}
