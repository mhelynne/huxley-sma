package com.thehuxley.data.dao.mysql;

import com.thehuxley.data.dao.SubmissionDao;

public class SubmissionDaoMySQL extends SubmissionDao {
	
	public SubmissionDaoMySQL() {
		
		SELECT_FROM_STUDENTS = "SELECT s.id as submission_id, s.user_id as user_id, s.evaluation as evaluation,"
				+ " s.problem_id as problem_id, p.nd   as problem_nd,"
				+ " tp.topic_id  as topic_id  , t.name as topic_name"
				+ " from submission s"
				+ " join problem p on p.id = s.problem_id"
				+ " join topic_problems tp on tp.problem_id = p.id"
				+ " join topic t on t.id = tp.topic_id"
				+ " WHERE s.user_id  in ("
				+ " 	SELECT distinct u.id from cluster_permissions cp"
				+ "		JOIN shiro_user u on u.id = cp.user_id "
				+ "		WHERE cp.permission = ? AND user_id=500 order by u.id )";
		
	}
	
}
