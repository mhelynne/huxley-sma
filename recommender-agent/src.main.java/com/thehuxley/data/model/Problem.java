package com.thehuxley.data.model;

public class Problem {

	private long id;
	private double nd;
	private String name;
	
	public static byte SUBMISSION_CORRECT = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getNd() {
		return nd;
	}

	public void setNd(double nd) {
		this.nd = nd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
