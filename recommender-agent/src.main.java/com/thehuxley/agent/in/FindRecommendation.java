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

import com.thehuxley.agent.AnaliserCommunication;
import com.thehuxley.agent.RecommenderCommunication;
import com.thehuxley.data.dao.ProblemDao;
import com.thehuxley.data.dao.mysql.ProblemDaoMySQL;
import com.thehuxley.data.model.Problem;

public class FindRecommendation implements RecommenderCommunication {
	
	static Logger logger = LoggerFactory.getLogger(FindRecommendation.class);

	@Override
	public String findRecommendation(String jsonUserId) {

		long userId;
		double problemNd;
		Map<String, Object> map;

		ObjectReader reader = new ObjectMapper().reader(HashMap.class);
		ObjectMapper mapper = new ObjectMapper();
		
		AnaliserCommunication analiserCommunication = new AnaliseUser();
		String jsonNd = "";
		
		Problem recommendedProblem;
		String jsonProblem = "";

		try {
			
			map = reader.readValue(jsonUserId.toString());
			userId = Long.parseLong(map.get("userId").toString());
			
			jsonNd = analiserCommunication.analiseUser(jsonUserId);
			map = reader.readValue(jsonNd.toString());
			problemNd = Double.parseDouble(map.get("nd").toString());
			
			recommendedProblem = findRecommendedProblemByNd(problemNd);
			jsonProblem = mapper.writeValueAsString(recommendedProblem);
			
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return jsonProblem;
		
	}
	

	//Escolhe o problema menos tentado, ele representa mais pontos no TopCoder
	private Problem findRecommendedProblemByNd(Double nd) {
		
		ProblemDao problemDao = new ProblemDaoMySQL();
		//List<Problem> problemList;
		Problem problem;
		
		Map<Long, Integer> countCorrectProblemNdMap;
		Map<Long, Integer> countProblemNdMap;		
		
		//problemList = problemDao.selectProblemByNd(nd);
		
		countCorrectProblemNdMap = problemDao.countCorrectSubmissionsByProblemNd(nd);
		countProblemNdMap = problemDao.countSubmissionsByProblemNd(nd);
		
		Long problemId;
		Integer correctCount;
		Integer totalCount;
		double rate;
		double lower = Double.MAX_VALUE;
		long recommendedProblemId = 0;
		
		for (Map.Entry<Long, Integer> entry : countProblemNdMap.entrySet()) {
			problemId = entry.getKey();
			totalCount = entry.getValue();
			correctCount = countCorrectProblemNdMap.get(problemId);
			
			if(correctCount!=null){
				rate = (double)correctCount/totalCount;
				
				if(lower > rate) {
					lower = rate;
					recommendedProblemId = problemId;
				}
			}
		}
			
		problem = problemDao.selectProblemById(recommendedProblemId);
		
		return problem;		
		
	}
}
