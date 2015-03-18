package agents.recommender;

import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.recommender.behaviour.WaitForStudentRequest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public abstract class RecommenderAgent extends Agent {

	static Logger logger = LoggerFactory.getLogger(RecommenderAgent.class);
	
	private static final long serialVersionUID = 2L;
	
	String agentName;
	
	@Override
	protected void setup() {
	
		// Registra o serviço agente recomendador nas páginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("recommender");
		sd.setName(agentName);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		logger.info("O agente " + this.getAID().getName() + " está pronto.");
		
		// Comportamentos do agente
		addBehaviour(new WaitForStudentRequest(this));
		
	}
	
	public abstract Behaviour recommenderToDataBehaviour(AID dataAgent, Request request,
														 ACLMessage msgFromStudent);
	
	protected void takeDown() {
		System.out.println("Adeus " + this.getAID().getName());
	}

}
