package com.thehuxley.data.dao.mysql;

import com.thehuxley.data.dao.ProblemSubmissionDao;

public class ProblemSubmissionDaoMySQL extends ProblemSubmissionDao {
	
	public ProblemSubmissionDaoMySQL() {
		
		CREATE_PROBLEM_SUBMSSION = "INSERT INTO problem_submission ( user_id, submission_id, solved, problem_id, problem_nd ) values (?,?,?,?,?)";
		
		GET_PROBLEM_SUBMSSION_BY_USER_ID = "SELECT * FROM problem_submission where user_id=?";
		
	}

}
