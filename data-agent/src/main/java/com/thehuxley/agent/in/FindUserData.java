package com.thehuxley.agent.in;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.agent.DataHistoricCommunication;
import com.thehuxley.data.dao.ProblemSubmissionDao;
import com.thehuxley.data.dao.mysql.ProblemSubmissionDaoMySQL;
import com.thehuxley.data.model.ProblemSubmission;

public class FindUserData implements DataHistoricCommunication {

	static Logger logger = LoggerFactory.getLogger(FindUserData.class);
	
	@Override
	public String consultUserData(String jsonUserId) {
		
		long userId;
		Map<String, Object> mapUserId;

		ObjectReader reader = new ObjectMapper().reader(HashMap.class);
		
		ProblemSubmissionDao problemSubmissionDao = new ProblemSubmissionDaoMySQL();
		List<ProblemSubmission> problemSubmissionList;

		ObjectMapper mapper = new ObjectMapper();
		String jsonProblemSubmissionList = "";
		
		try {
			
			mapUserId = reader.readValue(jsonUserId.toString());
			userId = Long.parseLong(mapUserId.get("userId").toString());
			
			problemSubmissionList = problemSubmissionDao.selectProblemSubmissionByUserId(userId);

			jsonProblemSubmissionList = mapper.writeValueAsString(problemSubmissionList);
			
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return jsonProblemSubmissionList;
		
	}

}
