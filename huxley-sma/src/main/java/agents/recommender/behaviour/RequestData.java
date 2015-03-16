package agents.recommender.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ProblemSubmission;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.JsonReader;

public class RequestData extends Behaviour {

	private static final long serialVersionUID = 22L;

	static Logger logger = LoggerFactory.getLogger(RequestData.class);

	private AID dataAgent;
	private String username;
	private ACLMessage msgFromStudent;
	
	private MessageTemplate mt; // The template to receive replies
	private int step = 0;	
	private boolean finished = false;
	
	private double recommendedNd;
	private String jsonProblem;
	private String refuseMsg;

	// O agente de dados a ser consultado, o username para ser enviado ao agente de dados e a mensagem que precisa ser respondida
	public RequestData(AID dataAgent, String username, ACLMessage msgFromStudent) {
		this.dataAgent = dataAgent;
		this.username = username;
		this.msgFromStudent = msgFromStudent;
	}

	@Override
	public void action() {

		switch (step) {

		case 0:

			// Esse username será enviado para o agente de dados
			JSONObject usernameJson = new JSONObject();
			usernameJson.put("username", username);

			// Criando mensagem para enviar ao Agente Dados,
			// solicitando as informações do usuário

			ACLMessage msgToData = new ACLMessage(ACLMessage.REQUEST);
			msgToData.addReceiver(dataAgent);
			msgToData.setOntology("request-from-recommender-by-level");
			msgToData.setContent(usernameJson.toString());
			msgToData.setConversationId("rqs");
			msgToData.setReplyWith("rqs" + System.currentTimeMillis()); // Unique value

			logger.info("Enviando " + msgToData.getOntology() + " : " + usernameJson);

			myAgent.send(msgToData);

			// Prepare the template to get answer
			mt = MessageTemplate.and(
					MessageTemplate.MatchConversationId("rqs"),
					MessageTemplate.MatchInReplyTo(msgToData.getReplyWith()));
			
			step = 1;

			break;

		case 1:

			ACLMessage reply = myAgent.receive(mt); // Recebe resposta vinda
													// do agente de dados,
													// pode ser INFORM ou REFUSE

			if (reply != null) {

				// INFORM traz os dados do usuário, a mensagem será lida
				if (reply.getPerformative() == ACLMessage.INFORM) {

					logger.info("Recebida " + reply.getOntology()+ ", com username " + username);
					recommendedNd = readMessage(reply.getContent());
					step = 2;

				} else if (reply.getPerformative() == ACLMessage.REFUSE) {
					
					logger.info("Solicitação negada! Não foram encontrados dados do usuário "+ username);
					refuseMsg = "Não foram encontrados dados do usuário " + username;
					
					step = 5; // Responde refuse
					
				}

			} else {
				block();
			}
			break;

		case 2:

			// Esse username será enviado para o agente de dados
			JSONObject ndJson = new JSONObject();
			ndJson.put("nd", recommendedNd);

			// Criando mensagem para enviar ao Agente Dados,
			// solicitando as informações do usuário

			ACLMessage msg2ToData = new ACLMessage(ACLMessage.REQUEST);
			msg2ToData.addReceiver(dataAgent);
			msg2ToData.setOntology("request-problem-by-nd");
			msg2ToData.setContent(ndJson.toString());
			msg2ToData.setConversationId("rqs");
			msg2ToData.setReplyWith("rqs" + System.currentTimeMillis()); // Unique value

			logger.info("Enviando " + msg2ToData.getOntology() + " : " + recommendedNd);

			myAgent.send(msg2ToData);

			// Prepare the template to get answer
			mt = MessageTemplate.and(
					MessageTemplate.MatchConversationId("rqs"),
					MessageTemplate.MatchInReplyTo(msg2ToData.getReplyWith()));

			step = 3;
			break;

		case 3:

			ACLMessage reply2 = myAgent.receive(mt); // Recebe resposta vinda
													 // do agente de dados,
													 // pode ser INFORM ou REFUSE

			if (reply2 != null) {

				// INFORM traz um problema
				if (reply2.getPerformative() == ACLMessage.INFORM) {

					logger.info("Recebida " + reply2.getOntology() + ", com nd " + recommendedNd);
					
					jsonProblem = reply2.getContent();
					logger.debug("O agente recomendador escolheu um problema " + jsonProblem);
					
					step = 4; // Responde propose

				} else if (reply2.getPerformative() == ACLMessage.REFUSE) {
					
					logger.info("Solicitação negada! Não foi encontrado problema com nd " + recommendedNd);
					
					refuseMsg = "Nenhum problema recomendado";
					
					step = 5; // Responde refuse
					
				}

			} else {
				block();
			}
			break;

		case 4:  // Este passo responde ao agente estudante com um problema recomendado
			
			// Aqui utilizamos ACLMessage msgFromStudent	
			ACLMessage replyToStudent = msgFromStudent.createReply();
			replyToStudent.setReplyWith(msgFromStudent.getReplyWith());

			// Preparando o resultado a ser enviado (problema + mensagem)
			JSONArray jsonResult = new JSONArray();
			jsonResult.put(new JSONObject(jsonProblem) );
			jsonResult.put("Tente resolver o problema ");
			
			// Compondo e enviando a mensagem
			replyToStudent.setPerformative(ACLMessage.PROPOSE);
			replyToStudent.setOntology("problem-text");
			replyToStudent.setContent(jsonResult.toString());
			
			logger.info("Enviando "+ replyToStudent.getOntology() + ": "+ jsonResult);
			myAgent.send(replyToStudent);
			
			finished = true;
			break;		
		
		case 5: // Esse passo responde ao agente estudante, caso não encontre recomendação
			
			// Aqui utilizamos ACLMessage msgFromStudent	
			ACLMessage replyToStudentError = msgFromStudent.createReply();
			replyToStudentError.setReplyWith(msgFromStudent.getReplyWith());
			
			// Compondo e enviando a mensagem
			replyToStudentError.setPerformative(ACLMessage.REFUSE);
			replyToStudentError.setOntology("refuse");
			replyToStudentError.setContent(refuseMsg);
			
			logger.info("Enviando "+ replyToStudentError.getOntology());
			myAgent.send(replyToStudentError);
			
			finished = true;
			break;
		}
		
	}

	private double readMessage(String content) {

		List<ProblemSubmission> problemSubmissionList;
		ProblemSubmission problemSubmission;
		JSONArray jsonArray;
		JSONObject jsonObject;

		jsonArray = new JSONArray(content);

		problemSubmissionList = new ArrayList<ProblemSubmission>();
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			problemSubmission = (ProblemSubmission) JsonReader
					.readValueAsObject(jsonObject.toString(),
							ProblemSubmission.class);
			if (problemSubmission != null) {
				problemSubmissionList.add(problemSubmission);
			}
		}

		return analiseLevel(problemSubmissionList);

	}

	private double analiseLevel(List<ProblemSubmission> problemSubmissionList) {

		Map<Double, Integer> mapNdCorrectSubmission = new HashMap<>();
		Map<Double, Integer> mapNdTotalSubmission = new HashMap<>();
		Integer contCorrect;
		Integer contTotal;
		Double ndKey;

		double acc = 0;
		double weight;
		double totalWeight = 0;
		double recommendedNd;

		for (ProblemSubmission sub : problemSubmissionList) {

			ndKey = sub.getProblemNd();

			contTotal = mapNdTotalSubmission.get(ndKey);

			if (contTotal == null) {
				contTotal = 1;
			} else {
				contTotal++;
			}

			mapNdTotalSubmission.put(ndKey, contTotal);

			if (sub.isSolved()) {
				contCorrect = mapNdCorrectSubmission.get(ndKey);
				if (contCorrect == null) {
					contCorrect = 1;
				} else {
					contCorrect++;
				}
				mapNdCorrectSubmission.put(ndKey, contCorrect);
			}

		}

		for (Map.Entry<Double, Integer> entry : mapNdTotalSubmission.entrySet()) {
			ndKey = entry.getKey();
			contTotal = entry.getValue();
			contCorrect = mapNdCorrectSubmission.get(ndKey);

			weight = (double) contCorrect / contTotal;

			acc += weight * ndKey;
			totalWeight += weight;
		}
		
		recommendedNd = Math.ceil(acc / totalWeight); //+1?
		return recommendedNd;

	}

	@Override
	public boolean done() {		
		return finished;
	}

}
