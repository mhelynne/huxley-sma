package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Problem;
import model.ProblemSubmission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import conf.Connector;
import conf.ResourcesUtil;

public class ProblemDao {

	static Logger logger = LoggerFactory.getLogger(ProblemDao.class);
	
	protected String GET_PROBLEM_LIST;
	protected String GET_PROBLEM_BY_ID;
	protected String GET_COUNT_CORRECT_BY_ND;
	protected String GET_COUNT_BY_ND;
		
	public Map<Long, Integer> countCorrectSubmissionsByProblemNd(Double nd) {
		
		Map<Long, Integer> countCorrectProblemNdMap = null;
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Contanto submissões corretas para problemas com "+ nd +" ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(GET_COUNT_CORRECT_BY_ND);
			}
			connection = Connector.getConnection();
			statement = connection.prepareStatement(GET_COUNT_CORRECT_BY_ND);
			statement.setByte(1, ProblemSubmission.SUBMISSION_CORRECT);
			statement.setDouble(2, nd);
			rs = statement.executeQuery();
			
			countCorrectProblemNdMap = new HashMap<>();
			while (rs.next()) {				
				countCorrectProblemNdMap.put(rs.getLong("problem_id"), rs.getInt("count"));				
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}
		
		if(countCorrectProblemNdMap.isEmpty()){
			countCorrectProblemNdMap = null;
		}
		return countCorrectProblemNdMap;
		
	}
	
	public Map<Long, Integer> countSubmissionsByProblemNd(Double nd) {
		
		Map<Long, Integer> countProblemNdMap = null;
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Contanto submissões para problemas com "+ nd +" ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(GET_COUNT_BY_ND);
			}
			connection = Connector.getConnection();
			statement = connection.prepareStatement(GET_COUNT_BY_ND);
			statement.setDouble(1, nd);
			rs = statement.executeQuery();

			countProblemNdMap = new HashMap<>();			
			while (rs.next()) {
				countProblemNdMap.put(rs.getLong("problem_id"), rs.getInt("count"));				
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}
		
		if(countProblemNdMap.isEmpty()){
			countProblemNdMap = null;
		}
		return countProblemNdMap;
		
	}

	public List<Problem> selectAllProblems(){
		
		List<Problem> problemList = null;
		Problem problem = null;
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Selecionando todos os problemas ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(GET_PROBLEM_LIST);
			}
			connection = Connector.getConnection();
			statement = connection.prepareStatement(GET_PROBLEM_LIST);
			rs = statement.executeQuery();

			problemList = new ArrayList<>();			
			while (rs.next()) {
				
				problem = new Problem();
				problem.setId(rs.getLong("id"));
				problem.setNd(rs.getDouble("nd"));
				problem.setName(rs.getString("name"));
				problem.setDescription(rs.getString("description"));
				problemList.add(problem);
				
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}
		
		return problemList;
		
	}
	
	public Problem selectProblemById(long id) {

		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		Problem problem = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Obtendo problema com id "+ id +" ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(GET_PROBLEM_BY_ID);
			}
			connection = Connector.getConnection();
			statement = connection.prepareStatement(GET_PROBLEM_BY_ID);
			statement.setLong(1, id);
			rs = statement.executeQuery();

			if (rs.next()) {
				
				problem = new Problem();
				problem.setId(id);
				problem.setNd(rs.getDouble("nd"));
				problem.setName(rs.getString("name"));
				problem.setDescription(rs.getString("description"));
				
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}
		
		return problem;
		
	}
	
//	public List<Problem> selectProblemByNd(Double nd) {
//
//		ResultSet rs = null;
//		PreparedStatement statement = null;
//		Connection connection = null;
//		List<Problem> problemList = null;
//		Problem problem = null;
//		
//		if (logger.isDebugEnabled()) {
//			logger.debug("Obtendo problemas com nd "+ nd +" ...");
//		}
//
//		try {
//			if (logger.isDebugEnabled()) {
//				logger.debug(GET_PROBLEMS_BY_ND);
//			}
//			connection = HuxleyConnector.getConnection();
//			statement = connection.prepareStatement(GET_PROBLEMS_BY_ND);
//			statement.setDouble(1, nd);
//			rs = statement.executeQuery();
//
//			problemList = new ArrayList<Problem>();
//
//			while (rs.next()) {
//				
//				problem = new Problem();
//				problem.setNd(nd);
//				problem.setId(rs.getLong("id"));
//				problem.setName(rs.getString("name"));
//				
//				problemList.add(problem);
//				
//			}
//
//		} catch (SQLException e) {
//			logger.error(e.getMessage(), e);
//		} finally {
//			ResourcesUtil.release(rs, statement, connection);
//		}
//		
//		return problemList;
//		
//	}
	
}
