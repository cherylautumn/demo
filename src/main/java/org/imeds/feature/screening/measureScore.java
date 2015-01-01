package org.imeds.feature.screening;

public class measureScore {
	private Integer id;
	private Double score;
	private String comment;
	public measureScore() {
	}
	public measureScore(Integer id, Double score, String comment) {
		super();
		this.id = id;
		this.score = score;
		this.comment = comment;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
