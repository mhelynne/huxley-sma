package agents.student.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

import model.Problem;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.JsonReader;
import agents.recommender.behaviour.NdRequestData;
import agents.student.StudentAgent;

public class WaitForRecommenderResponse extends Behaviour {

	private static final long serialVersionUID = 12L;

	private StudentAgent studentAgent;
	private MessageTemplate expectedMT;
	private List<String> recommendedProblemJsonList;
	private String refusedMsg;
	private int repliesCnt;
	private int repliesCntMax;
	private int step = 0;
	private boolean finished = false;

	static Logger logger = LoggerFactory.getLogger(NdRequestData.class);

	public WaitForRecommenderResponse(StudentAgent studentAgent) {
		
		this.studentAgent = studentAgent;
		repliesCntMax = studentAgent.getRecommenderAgents().length;
		recommendedProblemJsonList = new ArrayList<>();
		expectedMT = studentAgent.getMt();
		
	}

	@Override
	public void action() {

		ACLMessage reply = myAgent.receive(expectedMT); // Recebe resposta vinda
														// do agente de dados,
														// pode ser PROPOSE ou REFUSE

		switch (step) {
		case 0:
			if (reply != null) {

				// PROPOSE traz o problema recomendado
				if (reply.getPerformative() == ACLMessage.PROPOSE) {

					logger.info("Recebida " + reply.getOntology());
					recommendedProblemJsonList.add(reply.getContent());

				} else if (reply.getPerformative() == ACLMessage.REFUSE) { 
					
					logger.info("Recebida " + reply.getOntology());
					refusedMsg = reply.getContent();

				}
				
				repliesCnt++;
				if (repliesCnt >= repliesCntMax) {
					// We received all replies
					step = 1;				
				}
			} else {
				block();
			}
			break;

		case 1:
			
			List<Problem> recommendedProblems;
			List<String> recommendedTexts;
			JSONArray jsonArray;
			Problem recommendedProblem;
			String text;
			
			// Lendo os problemas recomendados que os agentes de recomendação enviaram
			recommendedProblems = new ArrayList<>();
			recommendedTexts = new ArrayList<>();
			if (!recommendedProblemJsonList.isEmpty()) {
				
				for (String rpJson : recommendedProblemJsonList) {
					jsonArray = new JSONArray(rpJson);
					recommendedProblem = (Problem) JsonReader.readValueAsObject(jsonArray.get(0).toString(), Problem.class);
					text = jsonArray.get(1).toString();
					
					recommendedProblems.add(recommendedProblem);
					recommendedTexts.add(text);
				}
								
				// Guardamos a lista dos problemas que foram recomendados pelos agentes de recomendação
				// Em caso de mais uma recomendação ser feita, primeiro consultamos essa lista
				studentAgent.setRecommendedProblems(recommendedProblems);
				studentAgent.setRecommendedTexts(recommendedTexts);
				studentAgent.chooseProblem();

			} else {
				// Motivo pelo qual não se deu recomendação
				studentAgent.showRefusedMsg(refusedMsg); 
			}

			finished = true;
			break;

		}
	}

	@Override
	public boolean done() {
		return finished;
	}

}
