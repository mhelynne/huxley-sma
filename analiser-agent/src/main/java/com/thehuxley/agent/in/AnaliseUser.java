package com.thehuxley.agent.in;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.agent.AnaliserCommunication;
import com.thehuxley.agent.DataHistoricCommunication;
import com.thehuxley.model.AnalysisSubmission;

public class AnaliseUser implements AnaliserCommunication {
	
	static Logger logger = LoggerFactory.getLogger(AnaliseUser.class);

	public String analiseUser(String jsonUserId) {
		
		long userId;
		Map<String, Object> mapUserId;

		ObjectReader reader = new ObjectMapper().reader(HashMap.class);
		
		DataHistoricCommunication dataComunnication = new FindUserData();
		String jsonProblemSubmissionList;
		List<AnalysisSubmission> analysisSubmissionList;
		
		double recommendedNd;
		JSONObject jsonNd = null;
		
		try {
			
			mapUserId = reader.readValue(jsonUserId.toString());
			userId = Long.parseLong(mapUserId.get("userId").toString());
			
			jsonProblemSubmissionList = dataComunnication.consultUserData(jsonUserId);
			
			analysisSubmissionList = decode(jsonProblemSubmissionList);
			
			recommendedNd = analiseLevel(analysisSubmissionList);
			
			jsonNd = new JSONObject();
			jsonNd.put("nd", recommendedNd);
			
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return jsonNd.toString();
		
	}


	private List<AnalysisSubmission> decode(String jsonProblemSubmissionList) {
		
		AnalysisSubmission analysisSubmission;
		List<AnalysisSubmission> analysisSubmissionList = new ArrayList<>();
		//ProblemTopic problemTopic;
		//Map<Long, ProblemTopic> problemTopicMap = new HashMap<>();
		
		JSONArray rootArray;
		JSONObject jsonObject;

		Map<String, Object> jsonMapSubmissions = new HashMap<>();
		ObjectReader reader = new ObjectMapper().reader(HashMap.class);

		long submissionId;
		long userId;
		boolean solved;
		long problemId;
		double problemNd;
//		Long topicId;
//		String topicName;
		
		rootArray = new JSONArray(jsonProblemSubmissionList);
		
		for (int i = 0; i < rootArray.length(); i++) {

			jsonObject = rootArray.getJSONObject(i);
				
			try {
				jsonMapSubmissions = reader.readValue(jsonObject.toString());

				submissionId = Long.parseLong(jsonMapSubmissions.get("submissionId").toString());
				userId = Long.parseLong(jsonMapSubmissions.get("userId").toString());
				solved = Boolean.parseBoolean(jsonMapSubmissions.get("solved").toString());
				problemId = Long.parseLong(jsonMapSubmissions.get("problemId").toString());
				problemNd = Double.parseDouble(jsonMapSubmissions.get("problemNd").toString());
				
				analysisSubmission = new AnalysisSubmission();
				analysisSubmission.setProblemId(problemId);
				analysisSubmission.setProblemNd(problemNd);
				analysisSubmission.setUserId(userId);
				analysisSubmission.setSubmissionId(submissionId);
				analysisSubmission.setSolved(solved);
				
				analysisSubmissionList.add(analysisSubmission);

			} catch (JsonGenerationException e) {
				logger.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

		}

		return analysisSubmissionList;
		
	}
	
	private double analiseLevel(List<AnalysisSubmission> analysisSubmissionList) {
		
		Map<Double, Integer> mapNdCorrectSubmission = new HashMap<>();
		Map<Double, Integer> mapNdTotalSubmission = new HashMap<>();
		Integer contCorrect;
		Integer contTotal;
		Double ndKey;
		
		double acc=0;
		double weight;
		double totalWeight=0;
		double recommendedNd;
		
		for (AnalysisSubmission sub : analysisSubmissionList) {
		
			ndKey = sub.getProblemNd();
			
			contTotal = mapNdTotalSubmission.get(ndKey);
			
			if(contTotal==null){
				contTotal = 1;
			} else {
				contTotal++;
			}
			
			mapNdTotalSubmission.put(ndKey, contTotal);
			
			if(sub.isSolved()){
				contCorrect = mapNdCorrectSubmission.get(ndKey);			
				if(contCorrect==null){
					contCorrect = 1;
				} else {
					contCorrect++;
				}
				mapNdCorrectSubmission.put(ndKey, contCorrect);
			}
			
		}
		
		for(Map.Entry<Double, Integer> entry : mapNdTotalSubmission.entrySet()) {
			ndKey = entry.getKey();
			contTotal = entry.getValue();
			contCorrect = mapNdCorrectSubmission.get(ndKey);
			
			weight = (double) contCorrect / contTotal;
			
			acc += weight * ndKey;
			totalWeight += weight;			
		}
		
		recommendedNd = Math.ceil(acc/totalWeight);
		
		return recommendedNd;
		
	}

}
