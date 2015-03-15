package com.thehuxley.agent.out;

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

import com.thehuxley.agent.HistoricCommunication;
import com.thehuxley.agent.in.FindHistoric;
import com.thehuxley.data.dao.ProblemSubmissionDao;
import com.thehuxley.data.dao.ProblemTopicDao;
import com.thehuxley.data.dao.mysql.ProblemSubmissionDaoMySQL;
import com.thehuxley.data.dao.mysql.ProblemTopicDaoMySQL;
import com.thehuxley.data.model.Problem;
import com.thehuxley.data.model.ProblemSubmission;
import com.thehuxley.data.model.ProblemTopic;

public class HistoricContact {

	static Logger logger = LoggerFactory.getLogger(HistoricContact.class);

	public static void main(String[] args) {

		HistoricCommunication historicCommunication = new FindHistoric();

		String jsonProblemSubmissionList = historicCommunication.consultHistoric();

		ProblemSubmission problemSubmission;
		List<ProblemSubmission> problemSubmissionList = new ArrayList<>();
		ProblemTopic problemTopic;
		Map<Long, ProblemTopic> problemTopicMap = new HashMap<>();

		JSONArray rootArray;
		JSONObject jsonObject;

		Map<String, Object> jsonMapSubmissions = new HashMap<>();
		Map<String, Object> jsonMapProblem = new HashMap<>();
		Map<String, Object> jsonMapTopics = new HashMap<>();
		ObjectReader reader = new ObjectMapper().reader(HashMap.class);

		JSONObject jsonObjectProblem;
		JSONArray jsonArrayTopic;

		long submissionId;
		long userId;
		boolean solved;
		long problemId;
		double problemNd;
		Long topicId;
		String topicName;

		rootArray = new JSONArray(jsonProblemSubmissionList);
		for (int i = 0; i < rootArray.length(); i++) {

			jsonObject = rootArray.getJSONObject(i);

			try {
				jsonMapSubmissions = reader.readValue(jsonObject.toString());

				submissionId = Long.parseLong(jsonMapSubmissions.get("submissionId").toString());
				userId = Long.parseLong(jsonMapSubmissions.get("userId").toString());
				solved = Boolean.parseBoolean(jsonMapSubmissions.get("solved").toString());

				jsonObjectProblem = jsonObject.getJSONObject("problem");
				jsonMapProblem = reader.readValue(jsonObjectProblem.toString());

				problemId = Long.parseLong(jsonMapProblem.get("problemId").toString());
				problemNd = Double.parseDouble(jsonMapProblem.get("problemNd").toString());

				problemSubmission = new ProblemSubmission();
				problemSubmission.setProblemId(problemId);
				problemSubmission.setProblemNd(problemNd);
				problemSubmission.setUserId(userId);
				problemSubmission.setSubmissionId(submissionId);
				problemSubmission.setSolved(solved);

				problemSubmissionList.add(problemSubmission);

				jsonArrayTopic = jsonObjectProblem.getJSONArray("topics");

				for (int j = 0; j < jsonArrayTopic.length(); j++) {

					jsonObject = jsonArrayTopic.getJSONObject(j);
					jsonMapTopics = reader.readValue(jsonObject.toString());

					topicId = Long.parseLong(jsonMapTopics.get("topicId").toString());
					topicName = jsonMapTopics.get("topicName").toString();

					if (problemTopicMap.get(topicId) == null) {
						problemTopic = new ProblemTopic();
						problemTopic.setProblemId(problemId);
						problemTopic.setTopicId(topicId);
						problemTopic.setTopicName(topicName);

						problemTopicMap.put(topicId, problemTopic);
					}

				}

			} catch (JsonGenerationException e) {
				logger.error(e.getMessage(), e);
			} catch (JsonMappingException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

		}
		
		ProblemSubmissionDao problemSubmissionDao = new ProblemSubmissionDaoMySQL();
		ProblemTopicDao problemTopicDao = new ProblemTopicDaoMySQL();
		
		//Salvando ProblemSubmission's
		for (ProblemSubmission ps : problemSubmissionList) {
			problemSubmissionDao.createProblemSubmission(ps);
		}
		
		//Salvando Topic's
		for (Map.Entry<Long, ProblemTopic> entry : problemTopicMap.entrySet()) {
		    ProblemTopic pt = entry.getValue();
		    problemTopicDao.createProblemTopic(pt);
		}

	}
	
}
