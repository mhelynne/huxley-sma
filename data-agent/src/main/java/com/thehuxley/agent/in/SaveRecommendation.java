package com.thehuxley.agent.in;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.agent.DataStudentCommunication;
import com.thehuxley.data.dao.ProblemRecommendationDao;
import com.thehuxley.data.dao.mysql.ProblemRecommendationDaoMySQL;
import com.thehuxley.data.model.ProblemRecommendation;

public class SaveRecommendation implements DataStudentCommunication {
	
	static Logger logger = LoggerFactory.getLogger(SaveRecommendation.class);
	
	@Override
	public boolean saveRecommendation(String jsonRecommendedProblem, String jsonUserId) {
		
		ObjectReader jsonMapper = new ObjectMapper().reader(HashMap.class);
		Map<String, Object> map;
		ProblemRecommendation recommendation = new ProblemRecommendation();
		ProblemRecommendationDao recommendationDao = new ProblemRecommendationDaoMySQL();
					
			try {
				map = jsonMapper.readValue(jsonRecommendedProblem);
				recommendation.setProblemId( Long.parseLong(map.get("id").toString() ) );
				
				map = jsonMapper.readValue(jsonUserId);
				recommendation.setUserId(Long.parseLong(map.get("userId").toString() ) );
			
				recommendationDao.createRecommendation(recommendation);				
				return true;
				
			} catch (JsonGenerationException e) {
				logger.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		
			return false;
	}

}
