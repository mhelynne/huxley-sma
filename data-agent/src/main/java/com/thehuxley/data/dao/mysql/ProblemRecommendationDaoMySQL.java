package com.thehuxley.data.dao.mysql;

import com.thehuxley.data.dao.ProblemRecommendationDao;

public class ProblemRecommendationDaoMySQL extends ProblemRecommendationDao {
	
	public ProblemRecommendationDaoMySQL() {

		CREATE_PROBLEM_RECOMMENDATION = "INSERT INTO recommendation (problem_id, user_id, recommendation_date) values (?,?,?)";
		
	}

}
