package com.thehuxley.agent.in;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.agent.HistoricCommunication;
import com.thehuxley.data.dao.SubmissionDao;
import com.thehuxley.data.dao.mysql.SubmissionDaoMySQL;
import com.thehuxley.data.model.Submission;

public class FindHistoric implements HistoricCommunication {

	static Logger logger = LoggerFactory.getLogger(FindHistoric.class);

	public String consultHistoric() {

		SubmissionDao submissionDao = new SubmissionDaoMySQL();
		List<Submission> submissionList;
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonSubmissionList = "";
		
		submissionList = submissionDao.selectFromStudents();
		
		try {

			jsonSubmissionList = mapper.writeValueAsString(submissionList);
			
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return jsonSubmissionList;
	}

}
