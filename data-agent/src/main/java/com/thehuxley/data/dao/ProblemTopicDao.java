package com.thehuxley.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.data.DataConnector;
import com.thehuxley.data.DeadLockHandler;
import com.thehuxley.data.ResourcesUtil;
import com.thehuxley.data.model.ProblemTopic;

public class ProblemTopicDao {

	static Logger logger = LoggerFactory.getLogger(ProblemTopicDao.class);

	protected String CREATE_PROBLEM_TOPIC;
	
	public boolean createProblemTopic(ProblemTopic problemTopic) {

		PreparedStatement statement = null;
		Connection connection = null;

		if (logger.isDebugEnabled()) {
			logger.debug("Salvando tópico " + problemTopic.getTopicId()	+ " ...");
		}

		try {

			connection = DataConnector.getConnection();

			statement = connection.prepareStatement(CREATE_PROBLEM_TOPIC);
			// topic_id, topic_name, problem_id

			statement.setLong(1, problemTopic.getTopicId());
			statement.setString(2, problemTopic.getTopicName());
			statement.setLong(3, problemTopic.getProblemId());

			DeadLockHandler.executeUpdate(statement);

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(null, statement, connection);
		}

		return true;
	}

}
