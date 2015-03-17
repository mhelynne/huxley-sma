package agents.recommender.behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.recommender.RecommenderAgent;
import util.JsonReader;

public class WaitForStudentRequest extends CyclicBehaviour {

	private static final long serialVersionUID = 21L;

	static Logger logger = LoggerFactory.getLogger(WaitForStudentRequest.class);

	String username;
	ACLMessage msgFromStudent;
	AID dataAgent = null;
	
	RecommenderAgent recommenderAgent;
	
	public WaitForStudentRequest(RecommenderAgent recommederAgent) {
		this.recommenderAgent = recommederAgent;
	}
	
	@Override
	public void action() {

		// Recebendo um request
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		msgFromStudent = myAgent.receive(mt);
		
		if (msgFromStudent != null) {

			// Lendo o username que solicita recomendação
			Request request;
			String jsonString = msgFromStudent.getContent();

			request = (Request) JsonReader.readValueAsObject(jsonString, Request.class);

			username = request.getUsername();

			logger.info("Recebida " + msgFromStudent.getOntology() + ", com username " + username);

			// Buscando agente de dados
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("data");
			template.addServices(sd);

			try {
				dataAgent = DFService.search(myAgent, template)[0].getName();
			} catch (FIPAException fe) {
				logger.error(fe.getMessage(), fe);
			} catch (ArrayIndexOutOfBoundsException ae) {
				logger.error("O agente de dados não foi encontrado!");
			}

			if (dataAgent != null) {

				// Comportamento que faz comunicação com o agente de dados
				// É diferente para cada agente recomendador
				myAgent.addBehaviour( recommenderAgent.recommenderToDataBehaviour(dataAgent, username, msgFromStudent) );

			} else {
				logger.info("Solicitação não enviada! Agente de dados não encontrado");
				
				// Aqui utilizamos ACLMessage msgFromStudent	
				ACLMessage replyToStudentError = msgFromStudent.createReply();
				
				// Compondo e enviando a mensagem
				replyToStudentError.setPerformative(ACLMessage.REFUSE);
				replyToStudentError.setOntology("refuse");
				replyToStudentError.setContent("Agente de dados não encontrado");
				
				myAgent.send(replyToStudentError);				
			}

		}
		// This puts the behaviour on hold until the next message is received
		// block doesn't stop execution; it just schedules the next execution
		// if you don't call block(), your behaviour will stay active and cause
		// a LOOP.
		block();

	}
	
}
