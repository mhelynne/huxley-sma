package com.thehuxley.agent.out;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thehuxley.agent.DataStudentCommunication;
import com.thehuxley.agent.RecommenderCommunication;
import com.thehuxley.agent.in.FindRecommendation;
import com.thehuxley.agent.in.SaveRecommendation;
import com.thehuxley.data.dao.ProblemSubmissionDao;

public class AnaliserContact {
	
	static Logger logger = LoggerFactory.getLogger(AnaliserContact.class);
	
	private static long ID_EXAMPLE = new Long(500);
	
	public static void main(String[] args) {
		
		RecommenderCommunication recommenderCommunication = new FindRecommendation();
		
		long userId = ID_EXAMPLE;
		
		String jsonUserId = new JSONObject().put("userId", ID_EXAMPLE).toString();
		
		String jsonProblem;
	
		jsonProblem = recommenderCommunication.findRecommendation(jsonUserId); // TODO Ligar ao Huxley
			
		DataStudentCommunication dataCommunication = new SaveRecommendation();
		if(dataCommunication.saveRecommendation(jsonProblem, jsonUserId)){
			System.out.println("Recomendação salva com sucesso!" + jsonProblem + " " + jsonUserId);
			logger.info("Recomendação salva com sucesso! " + jsonProblem + " " + jsonUserId);
		}
		
	}
	

}
