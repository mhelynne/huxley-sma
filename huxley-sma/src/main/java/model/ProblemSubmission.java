package model;

public class ProblemSubmission {

	public static byte SUBMISSION_CORRECT = 0;
	
	private long submissionId;
//	private long userId;
	private boolean solved;
	private long problemId;
	private double problemNd;

	public long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(long submissionId) {
		this.submissionId = submissionId;
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
