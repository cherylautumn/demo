package org.imeds.data;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class SurvivalTime {
	private Long id;
	private Date obs_start_date;
	private Date obs_end_date;
	private Date death_date;
	private Date censored_date;
	private Date dis_index_date;
	private Integer survival_length;
	private Integer survival_start;
	private Integer survival_end;
	private boolean censored;
	
	private ArrayList<Double> features=new ArrayList<Double>();
	public ArrayList<Double> getFeatures() {
		return features;
	}
	public void setFeatures(ArrayList<Double> features) {
		this.features = features;
	}
	public void setFeatures(String features) {
		//System.out.println(features);
		String[] fe= features.split(",");
		for(String s:fe){
		  
		  this.features.add(Double.parseDouble(s.trim()));
		}
	}
	public SurvivalTime(){
		
	}
	public SurvivalTime(Long id, Integer survival_length, boolean censored) {
		super();
		this.id = id;
		this.survival_length = survival_length;
		this.censored = censored;
	}

	public SurvivalTime(Long id2, Date obs_startDate, Date obs_endDate,
			Date death_Date) {
		// TODO Auto-generated constructor stub
		this.obs_start_date=obs_startDate;
		this.obs_end_date=obs_endDate;
		this.death_date=death_Date;

	}
	public Long getId() {
		return id;
	}
	public void setId(Long l) {
		this.id = l;
	}
	public Date getObs_start_date() {
		return obs_start_date;
	}
	public void setObs_start_date(Date obs_start_date) {
		this.obs_start_date = obs_start_date;
	}
	public Date getObs_end_date() {
		return obs_end_date;
	}
	public void setObs_end_date(Date obs_end_date) {
		this.obs_end_date = obs_end_date;
	}
	public Date getDeath_date() {
		return death_date;
	}
	public void setDeath_date(Date death_date) {
		this.death_date = death_date;
	}
	public Date getCensored_date() {
		return censored_date;
	}
	public void setCensored_date(Date censored_date) {
		this.censored_date = censored_date;
		int svvlen=0;
		if(this.death_date!=null){
			if(!this.death_date.after(this.censored_date)){
				setCensored(true);
				svvlen=Days.daysBetween(new DateTime(this.death_date),new DateTime(this.obs_start_date)).getDays();
				
			}else{
				setCensored(false);
				svvlen=Days.daysBetween(new DateTime(this.censored_date),new DateTime(this.obs_start_date)).getDays();
			}
		}else {
			setCensored(false);
			if(!this.obs_end_date.after(this.censored_date)){
				svvlen=Days.daysBetween(new DateTime(this.obs_end_date),new DateTime(this.obs_start_date)).getDays();
			}else{
				svvlen=Days.daysBetween(new DateTime(this.censored_date),new DateTime(this.obs_start_date)).getDays();
			}
		}
		svvlen = Math.abs(svvlen);
		setSurvival_length(svvlen);
		
		setSurvival_start(0);
		setSurvival_end(this.survival_length);
		
	}
	public Date getDis_index_date() {
		return dis_index_date;
	}
	public void setDis_index_date(Date dis_index_date) {
		this.dis_index_date = dis_index_date;
	}
	public Integer getSurvival_length() {
		return survival_length;
	}
	public void setSurvival_length(Integer survival_length) {
		this.survival_length = survival_length;
	}
	public Integer getSurvival_start() {
		return survival_start;
	}
	public void setSurvival_start(Integer survival_start) {
		this.survival_start = survival_start;
	}
	public Integer getSurvival_end() {
		return survival_end;
	}
	public void setSurvival_end(Integer survival_end) {
		this.survival_end = survival_end;
	}
	public boolean isCensored() {
		return censored;
	}
	public double getCensored() {
		if(censored)return 1;
		else return 0;		
		
	}
	public void setCensored(boolean censored) {
		this.censored = censored;
	}

	@Override
	public String toString() {
		return id + "," + obs_start_date
				  + "," + obs_end_date 
				  + "," + death_date 
				  + "," + censored_date
			  	  + "," + survival_length 
			  	  + "," + survival_start
				  + "," + survival_end
				  + "," + censored;
	}
	

	
}
