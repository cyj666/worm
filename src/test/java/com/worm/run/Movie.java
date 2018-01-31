package com.worm.run;

import java.util.ArrayList;
import java.util.List;

public class Movie {

	private String movieName;
	private String year;
	private String directors; // 导演
	private double rate; // 评分
	private long popleNum; // 评价人数
	private List<String> casts = new ArrayList<>(); // 主演
	private List<String> type= new ArrayList<>();    //类型	   
	private String movieTime;
	
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public long getPopleNum() {
		return popleNum;
	}
	public void setPopleNum(long popleNum) {
		this.popleNum = popleNum;
	}
	public List<String> getCasts() {
		return casts;
	}
	public void setCasts(String casts) {		
		this.casts.add(casts);
	}
	
	public List<String> getType() {
		return type;
	}
	public void setType(String type) {
		this.type.add(type);
	}
	public String getMovieTime() {
		return movieTime;
	}
	public void setMovieTime(String movieTime) {
		this.movieTime = movieTime;
	}
	
	
	public Movie() {		
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Movie [movieName=" + movieName + ", year=" + year + ", directors=" + directors + ", rate=" + rate
				+ ", popleNum=" + popleNum + ", casts=" + casts + ", type=" + type  
				+ ", movieTime=" + movieTime + "]";
	}
	
	
	
}
