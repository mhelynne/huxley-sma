package com.thehuxley.data.model;

import java.util.List;

public class Problem {

	private long problemId;
	private double problemNd;
	private List<Topic> topics;

	public long getProblemId() {
		return problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public double getProblemNd() {
		return problemNd;
	}

	public void setProblemNd(double problemNd) {
		this.problemNd = problemNd;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
