package agents.student.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private String recommendProblemJson;
	private List<String> recommendProblemJsonList;
	private Problem problem;
	private String refusedMsg;
	private int repliesCnt;
	private int repliesCntMax;
	private int step = 0;
	private boolean finished = false;

	static Logger logger = LoggerFactory.getLogger(NdRequestData.class);

	public WaitForRecommenderResponse(StudentAgent studentAgent) {
		this.studentAgent = studentAgent;
		repliesCntMax = studentAgent.getRecommenderAgents().length;
		recommendProblemJsonList = new ArrayList<>();
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
					recommendProblemJsonList.add(reply.getContent());

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

			Random random = new Random();

			if (!recommendProblemJsonList.isEmpty()) {

				// Escolhe um dos problemas recomendados aleatoriamente
				recommendProblemJson = recommendProblemJsonList.get(random.nextInt(recommendProblemJsonList.size()));

				JSONArray jsonArray = new JSONArray(recommendProblemJson);
				String text;

				problem = (Problem) JsonReader.readValueAsObject(jsonArray.get(0).toString(), Problem.class);
				text = jsonArray.get(1).toString();
				
				studentAgent.getStudentGui().createDialog( 
						studentAgent.getUsername() + ", " + text + " " + problem.getName()); // Mostra a recomendação

			} else {
				// Motivo pelo qual não se deu recomendação
				studentAgent.getStudentGui().createDialog(refusedMsg); 
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
