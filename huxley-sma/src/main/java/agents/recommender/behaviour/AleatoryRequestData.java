package agents.recommender.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.List;

import model.ProblemSubmission;
import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AleatoryRequestData extends RequestData {
	
	private static final long serialVersionUID = 2113L;

	static Logger logger = LoggerFactory.getLogger(AleatoryRequestData.class);
	
	private int step = 0;
	
	// O agente de dados a ser consultado, o username para ser enviado ao agente de dados e a mensagem que precisa ser respondida
	public AleatoryRequestData(AID dataAgent, Request request, ACLMessage msgFromStudent) {
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
					analiseNotWantedProblems(problemSubmissionList);
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

			requestAleatoryProblem();
			step = 3;
			break;

		case 3:

			ACLMessage reply2 = myAgent.receive(mt); // Recebe resposta vinda
													 // do agente de dados,
													 // pode ser INFORM ou REFUSE

			if (reply2 != null) {

				// INFORM traz um problema
				if (reply2.getPerformative() == ACLMessage.INFORM) {

					logger.info("Recebida " + reply2.getOntology());
					
					jsonProblem = reply2.getContent();
					logger.debug("O agente recomendador escolheu um problema " + jsonProblem);
					
					step = 4; // Responde propose

				} else if (reply2.getPerformative() == ACLMessage.REFUSE) {
					
					logger.info("Solicitação negada! Não foi encontrado problema");
					
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

	private void analiseNotWantedProblems(List<ProblemSubmission> problemSubmissionList) {
		
		List<Long> notWantedProblemsId = request.getNotWantedProblemsId();
		
		for (ProblemSubmission sub : problemSubmissionList) {
			
			if (sub.isSolved()) {
				// Colocando os problemas já resolvidos na lista de problemas que não serão recomendados 
				// Específico desse recomendador.
				if( !notWantedProblemsId.contains(sub.getProblemId()) ) { 
					notWantedProblemsId.add(sub.getProblemId());
				}
			}
			
		}
		
		// Atualizando a lista de problemas que não deseja ( problemas já resolvidos )
		request.setNotWantedProblemsId(notWantedProblemsId);
		
	}
	
	private void requestAleatoryProblem() {
		// TODO Auto-generated method stub
		
	}


}
