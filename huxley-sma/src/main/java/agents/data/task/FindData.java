package agents.data.task;

import java.util.List;
import java.util.Map;

import util.JsonMapper;
import model.Problem;
import model.ProblemSubmission;
import model.dao.ProblemDao;
import model.dao.ProblemSubmissionDao;
import model.dao.mysql.ProblemDaoMySQL;
import model.dao.mysql.ProblemSubmissionDaoMySQL;

public class FindData {
	
	// Retorna um string em formato json com as submissões do usuário,
	// se estavam corretas, problem da submissão e nd do problema
	public String findProblemSubmissionsByUsername(String username) {
		
		ProblemSubmissionDao problemSubmissionDao = new ProblemSubmissionDaoMySQL();
		List<ProblemSubmission> problemSubmissionList;
		String problemSubmissionsJson = null;
		
		problemSubmissionList = problemSubmissionDao.selectProblemSubmissionByUsername(username);

		if(!problemSubmissionList.isEmpty()) {
			problemSubmissionsJson = JsonMapper.writeValueAsString(problemSubmissionList);
		}
		
		return problemSubmissionsJson;
		
	}

	public String findLeastSolvedProblemByNd(double nd) {
		
		// TODO excluir problemas que já foram respondidos pelo usuário
		ProblemDao problemDao = new ProblemDaoMySQL();
		Problem problem;
		String problemJson = null;
		
		Map<Long, Integer> countCorrectProblemNdMap;
		Map<Long, Integer> countProblemNdMap;
		
		Long problemId;
		Integer correctCount;
		Integer totalCount;
		double rate;
		double lower = Double.MAX_VALUE;
		long recommendedProblemId = 0;
				
		countCorrectProblemNdMap = problemDao.countCorrectSubmissionsByProblemNd(nd);
		countProblemNdMap = problemDao.countSubmissionsByProblemNd(nd);

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
		
		problemJson = JsonMapper.writeValueAsString(problem);
		
		return problemJson;
		
	}

}
