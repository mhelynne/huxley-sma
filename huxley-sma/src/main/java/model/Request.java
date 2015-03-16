package model;

import java.util.List;

public class Request {
	
	String username;
	List<Long> notWantedProblemId;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<Long> getNotWantedProblemId() {
		return notWantedProblemId;
	}
	
	public void setNotWantedProblemId(List<Long> notWantedProblemId) {
		this.notWantedProblemId = notWantedProblemId;
	}

}
