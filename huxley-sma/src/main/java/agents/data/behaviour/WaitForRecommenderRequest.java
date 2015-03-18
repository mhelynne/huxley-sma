package agents.data.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.data.task.FindData;

public class WaitForRecommenderRequest extends CyclicBehaviour {

	private static final long serialVersionUID = 31L;

	static Logger logger = LoggerFactory.getLogger(WaitForRecommenderRequest.class);

	@Override
	public void action() {

		// Recebendo um request
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt);

		if (msg == null) {
			block();
		} else if (msg.getOntology().equals("request-problem-submission-list")) {

			answerRequestFromNdRecommender(msg);

		} else if (msg.getOntology().equals("request-problem-by-id")) {
			
			answerRequestProblemById(msg);
		
		} else if (msg.getOntology().equals("request-least-solved-problem-by-nd")) {
			
			answerRequestLeastSolvedProblemsByNd(msg);
						
		} else {
			
			ACLMessage unknown = msg.createReply();
			unknown.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			myAgent.send(unknown);
			block();
			// TODO receber esse tipo de mensagem no agente recomendador
		}

	}

	private void answerRequestFromNdRecommender(ACLMessage msg) {
		
		// Lendo o username que solicita recomendação
		String jsonString = msg.getContent();
		String username;

		username = new JSONObject(jsonString).getString("username");

		logger.info("Recebida " + msg.getOntology() + ", com username "	+ username);

		// Bucando os dados de submissão do usuário
		FindData findData = new FindData();
		String problemSubmissionsJson;
		problemSubmissionsJson = findData.findProblemSubmissionsByUsername(username);

		// Preparando resposta
		ACLMessage reply = msg.createReply();
		
		if (problemSubmissionsJson != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setOntology("user-data");
			reply.setContent(problemSubmissionsJson);

			logger.info("Respondendo com dados de " + username);
		} else {
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setOntology("nada-encontrado");
			reply.setContent("");

			logger.info("Dados não encontrados para o username " + username);
		}
		myAgent.send(reply);

	}
	
	private void answerRequestProblemById(ACLMessage msg) {
		// Lendo o username que solicita recomendação
		String jsonString = msg.getContent();
		long id;

		id = new JSONObject(jsonString).getLong("id");

		logger.info("Recebida " + msg.getOntology() + ", com id "	+ id);

		// Bucando os dados de submissão do usuário
		FindData findData = new FindData();
		String problemsJson;
		problemsJson = findData.findProblemById(id);

		// Preparando resposta
		ACLMessage reply = msg.createReply();

		if (problemsJson != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setOntology("problem-data");
			reply.setContent(problemsJson);

			logger.info("Respondendo com problema de nd " + id);
		} else {
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setOntology("nada-encontrado");
			reply.setContent("");

			logger.info("Problemas não encontrados para o nd " + id);
		}
		myAgent.send(reply);
		
	}


	private void answerRequestLeastSolvedProblemsByNd(ACLMessage msg) {
		// Lendo o username que solicita recomendação
		String jsonString = msg.getContent();
		double nd;

		nd = new JSONObject(jsonString).getDouble("nd");

		logger.info("Recebida " + msg.getOntology() + ", com nd "	+ nd);

		// Bucando os dados de submissão do usuário
		FindData findData = new FindData();
		String problemsJson;
		problemsJson = findData.findLeastSolvedProblemByNd(nd);

		// Preparando resposta
		ACLMessage reply = msg.createReply();

		if (problemsJson != null) {
			reply.setPerformative(ACLMessage.INFORM);
			reply.setOntology("problem-data");
			reply.setContent(problemsJson);

			logger.info("Respondendo com problema de nd " + nd);
		} else {
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setOntology("nada-encontrado");
			reply.setContent("");

			logger.info("Problemas não encontrados para o nd " + nd);
		}
		myAgent.send(reply);
		
	}

}
