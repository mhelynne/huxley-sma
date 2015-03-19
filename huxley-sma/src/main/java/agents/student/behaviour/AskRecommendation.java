package agents.student.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.JsonMapper;
import agents.student.StudentAgent;

public class AskRecommendation extends Behaviour {

	private static final long serialVersionUID = 21L;

	static Logger logger = LoggerFactory.getLogger(AskRecommendation.class);

	private StudentAgent studentAgent;
	private String username;
	private AID[] recommenderAgents;
	private MessageTemplate mt;

	private boolean finished = false;

	private Request request;
	private String jsonRequest;
	
	public AskRecommendation(StudentAgent studentAgent) {
		
		this.studentAgent = studentAgent;
		username = studentAgent.getUsername();
		request = studentAgent.getRequest();
		recommenderAgents = studentAgent.getRecommenderAgents();
		
	}

	@Override
	public void action() {

			// Envia mensagem de solicatação de recomendação
			logger.info("Seja bem vindo " + username);

			// Criando a solicitação
			jsonRequest = JsonMapper.writeValueAsString(request);

			// Criando mensagem para enviar aos agentes recomendadores,
			// solicitando uma recomendação de problema
			// Envia cfp para todos os recomendadores
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			for (int i = 0; i < recommenderAgents.length; ++i) {
				msg.addReceiver(recommenderAgents[i]);
			}
			msg.setContent(jsonRequest);
			msg.setOntology("student-asks");
			msg.setConversationId("cfp");
			msg.setReplyWith("cfp" + System.currentTimeMillis());

			logger.info("Enviando " + msg.getOntology() + ": " + jsonRequest);
			myAgent.send(msg);

			// Prepare the template to get answer
			mt = MessageTemplate.and(
					MessageTemplate.MatchConversationId("cfp"),
					MessageTemplate.MatchInReplyTo(msg.getReplyWith()));

			studentAgent.setMt(mt);
			finished = true;

			myAgent.addBehaviour(new WaitForRecommenderResponse(this.studentAgent));
		
		}
		

	@Override
	public boolean done() {
		return finished;
	}

}
