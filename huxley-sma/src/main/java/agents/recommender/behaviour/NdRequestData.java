package agents.recommender.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ProblemSubmission;
import model.Request;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.JsonMapper;


public class NdRequestData extends RequestData {

	private static final long serialVersionUID = 2111L;

	static Logger logger = LoggerFactory.getLogger(NdRequestData.class);
	
	private int step = 0;
	private double recommendedNd;
	
	// O agente de dados a ser consultado, o username para ser enviado ao agente de dados e a mensagem que precisa ser respondida
	public NdRequestData(AID dataAgent, Request request, ACLMessage msgFromStudent) {
		super(dataAgent, request, msgFromStudent);
	}

	@Override
	public void action() {

		switch (step) {

		case 0:

			requestProblemSubmissionList();			
			step = 1;

			break;

		case 1:

			List<ProblemSubmission> problemSubmissionList;
			ACLMessage reply = myAgent.receive(mt); // Recebe resposta vinda
													// do agente de dados,
													// pode ser INFORM ou REFUSE

			if (reply != null) {

				// INFORM traz os dados do usuário, a mensagem será lida
				if (reply.getPerformative() == ACLMessage.INFORM) {

					logger.info("Recebida " + reply.getOntology()+ ", com username " + username);
					problemSubmissionList = readProblemSubmissionListMessage(reply.getContent());
					recommendedNd = analiseLevel(problemSubmissionList);
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

			requestLeastSolvedProblemByNd();
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
			
			sendResponseToStudent("tente resolver o problema");
			break;		
		
		case 5: // Esse passo responde ao agente estudante, caso não encontre recomendação
			
			sendRefuseToStudent();
			break;
			
		}
		
	}

	private double analiseLevel(List<ProblemSubmission> problemSubmissionList) {

		Map<Double, Integer> mapNdCorrectSubmission = new HashMap<>();
		Map<Double, Integer> mapNdTotalSubmission = new HashMap<>();
		List<Long> notWantedProblemsId;
		Integer contCorrect;
		Integer contTotal;
		Double ndKey;

		double acc = 0;
		double weight;
		double totalWeight = 0;
		double recommendedNd;

		notWantedProblemsId = request.getNotWantedProblemsId();
		
		for (ProblemSubmission sub : problemSubmissionList) {

			ndKey = sub.getProblemNd();

			contTotal = mapNdTotalSubmission.get(ndKey);

			if (contTotal == null) { // Contando todas as submissões
				contTotal = 1;
			} else {
				contTotal++;
			}

			mapNdTotalSubmission.put(ndKey, contTotal);

			if (sub.isSolved()) { // Contando as submissões corretas
				contCorrect = mapNdCorrectSubmission.get(ndKey);
				if (contCorrect == null) {
					contCorrect = 1;
				} else {
					contCorrect++;
				}
				mapNdCorrectSubmission.put(ndKey, contCorrect);
				
				// Colocando os problemas já resolvidos na lista de problemas que não serão recomendados 
				// Específico desse recomendador.
				if( !notWantedProblemsId.contains(sub.getProblemId()) ) { 
					notWantedProblemsId.add(sub.getProblemId());
				}
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
		
		// Atualizando a lista de problemas que não deseja ( problemas já resolvidos )
		request.setNotWantedProblemsId(notWantedProblemsId);
	
		// Calculando nd recomendado
		recommendedNd = Math.ceil(acc / totalWeight); //+1?
		return recommendedNd;

	}
	
	private void requestLeastSolvedProblemByNd() {
		
		// Será enviado o nd ...
		JSONObject ndJson = new JSONObject();
		ndJson.put("nd", recommendedNd);

		// E os problemas que devem ser desconsiderados
		ndJson.put( "notWantedProblemsId", JsonMapper.writeValueAsString(request.getNotWantedProblemsId()) );
		
		// Criando mensagem para enviar ao Agente Dados,
		// solicitando um problema por nd, nesse caso, o menos respondido

		ACLMessage msg2ToData = new ACLMessage(ACLMessage.REQUEST);
		msg2ToData.addReceiver(dataAgent);
		msg2ToData.setOntology("request-least-solved-problem-by-nd");
		msg2ToData.setContent(ndJson.toString());
		msg2ToData.setConversationId("rqs");
		msg2ToData.setReplyWith("rqs" + System.currentTimeMillis()); // Unique value

		logger.info("Enviando " + msg2ToData.getOntology() + " : " + recommendedNd);

		myAgent.send(msg2ToData);

		// Prepare the template to get answer
		mt = MessageTemplate.and(
				MessageTemplate.MatchConversationId("rqs"),
				MessageTemplate.MatchInReplyTo(msg2ToData.getReplyWith()));
		
	}

}
