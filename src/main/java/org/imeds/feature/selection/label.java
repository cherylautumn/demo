package org.imeds.feature.selection;

import java.util.ArrayList;

public class label {
	private Integer Class_id;
	private Double feature_v;
	private Long data_id;
	public label() {
		// TODO Auto-generated constructor stub
	}
	public label(Integer class_id, Double feature_v, Long data_id) {
		super();
		this.Class_id = class_id;
		this.feature_v = feature_v;
		this.data_id = data_id;
	}

	public Integer getClass_id() {
		return Class_id;
	}

	public void setClass_id(Integer class_id) {
		Class_id = class_id;
	}

	public Double getFeature_v() {
		return feature_v;
	}

	public void setFeature_v(Double feature_v) {
		this.feature_v = feature_v;
	}


	public Long getData_id() {
		return data_id;
	}

	public void setData_id(Long data_id) {
		this.data_id = data_id;
	}
	@Override
	public String toString() {
		return "label [Class_id=" + Class_id + ", feature_v=" + feature_v
				+ ", data_id=" + data_id + "]";
	}



}
