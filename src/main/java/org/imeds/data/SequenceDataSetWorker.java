package org.imeds.data;

import java.io.File;
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

import org.imeds.util.CCIcsvTool;
import org.imeds.util.ComorbidDSxmlTool;
import org.imeds.util.ImedDateFormat;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Item;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.ItemFactory;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.items.Itemset;


public class SequenceDataSetWorker extends DataSetWorker {
	
	private SequenceDataSetConfig cdsc = new SequenceDataSetConfig();
	private ComorbidDSxmlTool cfgparser = new ComorbidDSxmlTool();
	private String configFile="";
	public SequenceDataSetWorker() {
		// TODO Auto-generated constructor stub
	}
	public SequenceDataSetWorker(String configFile) {
		this.configFile = configFile;
	}

	@Override
	public void prepare() {
		
		//Initialize config file
		this.cfgparser.parserDoc(this.configFile,this.cdsc);

	}

	@Override
	public void ready() {
		File directory = new File(this.cdsc.getInputFolder());
		File[] fList = directory.listFiles();
		for (File file : fList){		
			if (file.isFile()){
				ArrayList<ArrayList<String>> DataPointList = new ArrayList<ArrayList<String>>();
				CCIcsvTool.preSequenceDataParserDoc(this.cdsc.getInputFolder()+"\\"+file.getName(),DataPointList);
				HashMap<Long, HashMap<Date, Itemset>> sequences = processSequence(DataPointList);
				ArrayList<ArrayList<String>> seqTocsv = transSequencesToList(sequences);
				CCIcsvTool.SequenceDataSetCreateDoc(this.cdsc.getOutputFolder()+"\\seq_"+file.getName(), seqTocsv); 
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

	public ArrayList<ArrayList<String>> transSequencesToList(HashMap<Long, HashMap<Date, Itemset>> sequences ){
		ArrayList<ArrayList<String>> csvlist = new ArrayList<ArrayList<String>>();
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
			csvlist.add(itemStr);
		}
		
		return csvlist;
	}
	@Override
	public void go() {
		// TODO Auto-generated method stub

	}

	@Override
	public void done() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SequenceDataSetWorker sdsw = new SequenceDataSetWorker("data\\IMEDS\\DiabeteComorbidDS\\DSConfig.xml");
		sdsw.prepare();
		sdsw.ready();
	}

}
