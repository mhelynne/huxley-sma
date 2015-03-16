package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ProblemSubmission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import conf.Connector;
import conf.ResourcesUtil;

public class ProblemSubmissionDao {
	static Logger logger = LoggerFactory.getLogger(ProblemSubmissionDao.class);

	protected String GET_PROBLEM_SUBMSSION_BY_USERNAME;
	
	public List<ProblemSubmission> selectProblemSubmissionByUsername(String username) {

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
				logger.debug(GET_PROBLEM_SUBMSSION_BY_USERNAME);
			}
			connection = Connector.getConnection();
			statement = connection.prepareStatement(GET_PROBLEM_SUBMSSION_BY_USERNAME);
			statement.setString(1, username);
			rs = statement.executeQuery();

			problemSubmissionList = new ArrayList<ProblemSubmission>();

			while (rs.next()) {

				problemSubmission = new ProblemSubmission();
				//problemSubmission.setUserId(rs.getLong("user_id"));
				problemSubmission.setSubmissionId(rs.getLong("submission_id"));
				problemSubmission.setProblemId(rs.getLong("problem_id"));
				problemSubmission.setProblemNd(rs.getDouble("problem_nd"));
				if(rs.getByte("evaluation") == ProblemSubmission.SUBMISSION_CORRECT) {
					problemSubmission.setSolved(true);
				} else {
					problemSubmission.setSolved(false);
				}
				
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
