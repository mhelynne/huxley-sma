package model.dao.mysql;

import model.dao.ProblemSubmissionDao;

public class ProblemSubmissionDaoMySQL extends ProblemSubmissionDao {
	
	public ProblemSubmissionDaoMySQL() {
		
		GET_PROBLEM_SUBMSSION_BY_USERNAME = "SELECT s.id as submission_id, s.evaluation as evaluation,"
				+ " s.problem_id as problem_id, p.nd   as problem_nd"
				//+ " tp.topic_id  as topic_id  , t.name as topic_name"
				+ " from submission s"
				+ " join shiro_user u on u.id = s.user_id"
				+ " join problem p on p.id = s.problem_id"
				//+ " join topic_problems tp on tp.problem_id = p.id"
				//+ " join topic t on t.id = tp.topic_id"
				+ " WHERE u.username = ?";
		
	}

}
