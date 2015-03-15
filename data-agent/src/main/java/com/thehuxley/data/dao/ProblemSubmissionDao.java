package com.thehuxley.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.data.ResourcesUtil;
import com.thehuxley.data.DataConnector;
import com.thehuxley.data.DeadLockHandler;
import com.thehuxley.data.model.ProblemSubmission;

public class ProblemSubmissionDao {

	static Logger logger = LoggerFactory.getLogger(ProblemSubmissionDao.class);

	protected String CREATE_PROBLEM_SUBMSSION;
	protected String GET_PROBLEM_SUBMSSION_BY_USER_ID;

	public boolean createProblemSubmission(ProblemSubmission problemSubmission) {

		PreparedStatement statement = null;
		Connection connection = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Salvando submissão "	+ problemSubmission.getSubmissionId() + " ...");
		}

		try {
			
			connection = DataConnector.getConnection();
			
			statement = connection.prepareStatement(CREATE_PROBLEM_SUBMSSION);
			// user_id, submission_id, solved, problem_id, problem_nd
			
			statement.setLong(1, problemSubmission.getUserId());
			statement.setLong(2, problemSubmission.getSubmissionId());
			statement.setBoolean(3, problemSubmission.isSolved());
			statement.setLong(4, problemSubmission.getProblemId());
			statement.setDouble(5, problemSubmission.getProblemNd());

			DeadLockHandler.executeUpdate(statement);

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(null, statement, connection);
		}

		return true;
	}

	public List<ProblemSubmission> selectProblemSubmissionByUserId(long userId) {

		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		List<ProblemSubmission> problemSubmissionList = null;
		ProblemSubmission problemSubmission = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Obtendo submissões dos usuários ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(GET_PROBLEM_SUBMSSION_BY_USER_ID);
			}
			connection = DataConnector.getConnection();
			statement = connection
					.prepareStatement(GET_PROBLEM_SUBMSSION_BY_USER_ID);
			statement.setLong(1, userId);
			rs = statement.executeQuery();

			problemSubmissionList = new ArrayList<ProblemSubmission>();

			while (rs.next()) {
				
				problemSubmission = new ProblemSubmission();
				problemSubmission.setUserId(userId);
				problemSubmission.setSubmissionId(rs.getLong("submission_id"));
				problemSubmission.setSolved(rs.getBoolean("solved"));
				problemSubmission.setProblemId(rs.getLong("problem_id"));
				problemSubmission.setProblemNd(rs.getDouble("problem_nd"));
				problemSubmissionList.add(problemSubmission);
				
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}
		
		return problemSubmissionList;
	}


}
