package com.worm.run;

import java.util.List;

public class MovieInfo {

	private String directors; // 导演
	private double rate; // 评分
	private String title; // 电影名称
	private String url; // 地址
	private List<String> casts; // 主演
	
	public MovieInfo() {
		// TODO Auto-generated constructor stub
		 
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getCasts() {
		return casts;
	}
	public void setCasts(List<String> casts) {
		this.casts = casts;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((casts == null) ? 0 : casts.hashCode());
		result = prime * result + ((directors == null) ? 0 : directors.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieInfo other = (MovieInfo) obj;
		if (casts == null) {
			if (other.casts != null)
				return false;
		} else if (!casts.equals(other.casts))
			return false;
		if (directors == null) {
			if (other.directors != null)
				return false;
		} else if (!directors.equals(other.directors))
			return false;
		if (Double.doubleToLongBits(rate) != Double.doubleToLongBits(other.rate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "MovieInfo [directors=" + directors + ", rate=" + rate + ", title=" + title + ", url=" + url + ", casts="
				+ casts + "]";
	}

	
	
}
