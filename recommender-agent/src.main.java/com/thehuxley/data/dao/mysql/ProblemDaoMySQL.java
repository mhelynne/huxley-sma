package com.thehuxley.data.dao.mysql;

import com.thehuxley.data.dao.ProblemDao;

public class ProblemDaoMySQL extends ProblemDao {
	
	public ProblemDaoMySQL() {
		
		GET_PROBLEM_BY_ID = "select nd, name from problem where id = ?";
		
		//GET_PROBLEMS_BY_ND = "select id, name from problem where nd = ?";
		
		GET_COUNT_CORRECT_BY_ND = "select problem_id, count(*) as count from submission s"
				+ " where evaluation = ? and problem_id in"
				+ " (select id from problem p where nd = ?) group by problem_id";
		
		GET_COUNT_BY_ND = "select problem_id, count(*) as count from submission s"
				+ " where problem_id in"
				+ " (select id from problem p where nd = ?) group by problem_id";
		
	}

}
