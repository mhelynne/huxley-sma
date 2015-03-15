package com.thehuxley.data.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.data.DataConnector;
import com.thehuxley.data.DeadLockHandler;
import com.thehuxley.data.ResourcesUtil;
import com.thehuxley.data.model.ProblemRecommendation;

public class ProblemRecommendationDao {
	
	static Logger logger = LoggerFactory.getLogger(ProblemSubmissionDao.class);

	protected String CREATE_PROBLEM_RECOMMENDATION;

	public void createRecommendation(ProblemRecommendation recommendation) {
		
		PreparedStatement statement = null;
		Connection connection = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Salvando recomendação do problema " + recommendation.getProblemId() +
					" para o usuário " + recommendation.getUserId() + " ...");
		}

		try {
			
			connection = DataConnector.getConnection();
			
			statement = connection.prepareStatement(CREATE_PROBLEM_RECOMMENDATION);
			// problem_id, user_id, recommendation_date
			
			statement.setLong(1, recommendation.getProblemId());
			statement.setLong(2, recommendation.getUserId());
			statement.setDate(3, new Date(System.currentTimeMillis()));
			DeadLockHandler.executeUpdate(statement);

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(null, statement, connection);
		}
		
	}

}
