package com.thehuxley.data.model;

import java.sql.Date;

public class ProblemRecommendation {
	
	long id;
	Date recommendationDate;
	long problemId;
	long userId;

	public Date getRecommendationDate() {
		return recommendationDate;
	}
	
	public void setRecommendationDate(Date recommendationDate) {
		this.recommendationDate = recommendationDate;
	}
	
	public long getProblemId() {
		return problemId;
	}
	
	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
