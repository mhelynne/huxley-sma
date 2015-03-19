package model;

import java.util.ArrayList;
import java.util.List;

public class Request {
	
	String username;
	List<Long> notWantedProblemsId = new ArrayList<>();
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<Long> getNotWantedProblemsId() {
		return notWantedProblemsId;
	}
	
	public void setNotWantedProblemsId(List<Long> notWantedProblemsId) {
		this.notWantedProblemsId = notWantedProblemsId;
	}

}
