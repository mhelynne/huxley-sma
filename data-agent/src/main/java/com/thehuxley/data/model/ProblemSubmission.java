package com.thehuxley.data.model;

public class ProblemSubmission {

	private long id;
	private long submissionId;
	private long userId;
	private boolean solved;
	private long problemId;
	private double problemNd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(long submissionId) {
		this.submissionId = submissionId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

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

}
