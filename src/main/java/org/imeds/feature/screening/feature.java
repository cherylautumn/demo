package org.imeds.feature.screening;

import org.imeds.feature.selection.discrimItemsets;

public class feature implements Comparable<feature>{
//	private Integer id;
	private Long id;
	private Double score;
	private String Description;
//	public feature(Integer id, double score) {
//		// TODO Auto-generated constructor stub
//		this.id=id;
//		this.score=score;
//	}
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public feature(Long id, double score) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.score=score;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int compareTo(feature o) {
	
//		if(this.score < o.score){
//            return 1;
//        }else{
//        	return -1;
//        }
		if (this.score < o.score)
	        return 1;           // Neither val is NaN, thisVal is smaller
        if (this.score > o.score)
            return -1;            // Neither val is NaN, thisVal is larger

        // Cannot use doubleToRawLongBits because of possibility of NaNs.
        long thisBits    = Double.doubleToLongBits(this.score);
        long anotherBits = Double.doubleToLongBits( o.score);

        return (thisBits == anotherBits ?  0 : // Values are equal
                (thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                 1));      
	}

	@Override
	public String toString() {
		return "feature [id=" + id + ", score=" + score + "]";
	}


}
