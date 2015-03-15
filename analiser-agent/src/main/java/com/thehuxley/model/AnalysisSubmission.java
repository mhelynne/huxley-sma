package com.thehuxley.model;

public class AnalysisSubmission {

	// Essa entidade é similar a Submission do Huxley,
	// no entanto foi modificada para manter Problema, Tópicos, Ajuda,
	// entre outras adaptações para o Agente Analisador
	
	private long submissionId;
	private long userId;
	private boolean solved;
	
	private long problemId;
	private double problemNd;
	
//	private Map<Long, String> topicsMap;
//	
//	private int tipNumber;

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

//	public Map<Long, String> getTopicsMap() {
//		return topicsMap;
//	}
//
//	public void setTopicsMap(Map<Long, String> topicsMap) {
//		this.topicsMap = topicsMap;
//	}
//
//	public int getTipNumber() {
//		return tipNumber;
//	}
//
//	public void setTipNumber(int tipNumber) {
//		this.tipNumber = tipNumber;
//	}
	
}
