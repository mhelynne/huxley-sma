package agents.recommender.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.ProblemSubmission;
import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentRequestData extends RequestData {

	private static final long serialVersionUID = 2112L;

	static Logger logger = LoggerFactory.getLogger(PersistentRequestData.class);

	private int step = 0;	
	
	// O agente de dados a ser consultado, o username para ser enviado ao agente de dados e a mensagem que precisa ser respondida
	public PersistentRequestData(AID dataAgent, Request request, ACLMessage msgFromStudent) {
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
					recommendedId = analisePersistence(problemSubmissionList);
					if(recommendedId == 0) {
						// Não encontrou um problema para recomendar
						// porque todos os problemas tentados já foram resolvidos
						
						refuseMsg = "Nenhum problema recomendado";
						step = 5; // Responde refuse
					}
					
					step = 2; // Encontrou um id, vai para o próximo passo

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

			requestProblemById();
			step = 3;
			break;

		case 3:

			ACLMessage reply2 = myAgent.receive(mt); // Recebe resposta vinda
													 // do agente de dados,
													 // pode ser INFORM ou REFUSE

			if (reply2 != null) {

				// INFORM traz um problema
				if (reply2.getPerformative() == ACLMessage.INFORM) {

					logger.info("Recebida " + reply2.getOntology() + ", com id " + recommendedId);
					
					jsonProblem = reply2.getContent();
					logger.debug("O agente recomendador escolheu um problema " + jsonProblem);
					
					step = 4; // Responde propose

				} else if (reply2.getPerformative() == ACLMessage.REFUSE) {
					
					logger.info("Solicitação negada! Não foi encontrado problema com id " + recommendedId);
					
					refuseMsg = "Nenhum problema recomendado";
					
					step = 5; // Responde refuse
					
				}

			} else {
				block();
			}
			break;

		case 4:  // Este passo responde ao agente estudante com um problema recomendado
			
			sendResponseToStudent("continue tentando o problema");
			break;		
		
		case 5: // Esse passo responde ao agente estudante, caso não encontre recomendação
			
			sendRefuseToStudent();
			break;
		}
		
	}

	private long analisePersistence(List<ProblemSubmission> problemSubmissionList) {
		
		long problemId =0;
		List<Long> notWantedProblems;
		List<Long> correctProblems;
		List<Long> unsolvedProblems;
		Map<Long, Integer> unsolvedProblemTriesMap = new HashMap<>();
		Integer tries;
		
		notWantedProblems = request.getNotWantedProblemsId();
		correctProblems = new ArrayList<>();
		
		for (ProblemSubmission ps : problemSubmissionList) {
			
			problemId = ps.getProblemId();
			if( notWantedProblems.contains(problemId) || ps.isSolved()){
				
				correctProblems.add(problemId);// Se já foi resolvido retira do map, ou se deve ser desconsiderado
				unsolvedProblemTriesMap.remove(problemId);
				
			} else { // Se não, incrementa o numero de tentativas
				
				tries = unsolvedProblemTriesMap.get(problemId);
				if(tries == null) {
					tries=0; // Não foi para o map ainda
				}
				tries++;
				unsolvedProblemTriesMap.put(problemId, tries);
				
			}
			
		}

		problemId =0;
		if(! unsolvedProblemTriesMap.isEmpty()) {
			
			// Sorteia um problema entre os problemas ainda não resolvidos para incentivar a persistencia			
			Random random = new Random();
			unsolvedProblems = new ArrayList<>(unsolvedProblemTriesMap.keySet());
			problemId = unsolvedProblems.get(random.nextInt( unsolvedProblems.size() ));

		}
		return problemId;
		
	}


}
