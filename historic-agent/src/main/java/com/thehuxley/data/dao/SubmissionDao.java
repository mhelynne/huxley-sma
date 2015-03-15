package com.thehuxley.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.data.HuxleyConnector;
import com.thehuxley.data.ResourcesUtil;
import com.thehuxley.data.model.Problem;
import com.thehuxley.data.model.Submission;
import com.thehuxley.data.model.Topic;
import com.thehuxley.data.model.User;

public class SubmissionDao {
	
	static Logger logger = LoggerFactory.getLogger(SubmissionDao.class);

	protected String SELECT_FROM_STUDENTS;
	
	public List<Submission> selectFromStudents() {
		
		ResultSet rs = null;
		PreparedStatement statement = null;
		Connection connection = null;
		
		long lastId = 0;
		long subId = 0;
		
		Submission submission = null;
		Problem problem = null;
		Topic topic = null;

		List<Submission> submissionList = null;
		List<Topic> topicsList = null;

		if (logger.isDebugEnabled()) {
			System.out.println("Obtendo submissions de usuários estudantes ...");
		}

		try {
			if (logger.isDebugEnabled()) {
				System.out.println(SELECT_FROM_STUDENTS);
				logger.debug(SELECT_FROM_STUDENTS);
			}
			connection = HuxleyConnector.getConnection();
			statement = connection.prepareStatement(SELECT_FROM_STUDENTS);
			statement.setLong(1, User.STUDENT);
			rs = statement.executeQuery();

			submissionList = new ArrayList<Submission>();
			
			while (rs.next()) {
				subId = rs.getLong("submission_id");

				if (lastId != subId) { // Uma nova submissão

					// Add a submissão anterior a lista
					if (lastId != 0) // Menos na primeira iteração (pois não há submissão anterior ainda)
					submissionList.add(submission);

					// Agora, criando a nova submissão,
					submission = new Submission();
					submission.setSubmissionId(subId);
					submission.setUserId(rs.getLong("user_id"));
					
					if(rs.getByte("evaluation") == Submission.CORRECT){
						submission.setSolved(true);
					} else {
						submission.setSolved(false);
					}

					// o problema da submissão,
					problem = new Problem();
					problem.setProblemId(rs.getLong("problem_id"));
					problem.setProblemNd(rs.getLong("problem_nd"));					
					topicsList = new ArrayList<>();
					problem.setTopics(topicsList);
					submission.setProblem(problem);
					
					lastId = subId;

				}
				
				// o tópico do problema (pode haver mais de um, não vai cair no
				// if na próxima iteração, vai apenas add o novo tópico)
				topic = new Topic();
				topic.setTopicId(rs.getLong("topic_id"));
				topic.setTopicName(rs.getString("topic_name"));
				submission.getProblem().getTopics().add(topic);

			}
			submissionList.add(submission); // A última submissão

		} catch (SQLException e) {
			System.out.println(e);
			logger.error(e.getMessage(), e);
		} finally {
			ResourcesUtil.release(rs, statement, connection);
		}

		return submissionList;
	}
	
}
