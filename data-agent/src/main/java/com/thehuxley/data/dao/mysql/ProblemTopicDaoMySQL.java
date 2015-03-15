package com.thehuxley.data.dao.mysql;

import com.thehuxley.data.dao.ProblemTopicDao;

public class ProblemTopicDaoMySQL extends ProblemTopicDao {
	
	public ProblemTopicDaoMySQL() {
		
		CREATE_PROBLEM_TOPIC = "INSERT INTO problem_topic ( topic_id, topic_name, problem_id ) values (?,?,?)";
		
	}
	

}
