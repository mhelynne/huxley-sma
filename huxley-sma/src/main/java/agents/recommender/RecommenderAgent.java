package agents.recommender;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.recommender.behaviour.WaitForStudentRequest;

public class RecommenderAgent extends Agent {

	private static final long serialVersionUID = 2L;

	static Logger logger = LoggerFactory.getLogger(RecommenderAgent.class);

	@Override
	protected void setup() {

		// Registra o serviço agente recomendador nas páginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("recommender");
		sd.setName("recommender-by-level");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		logger.info("O agente " + this.getAID().getName() + " está pronto.");

		// Comportamentos do agente
		addBehaviour(new WaitForStudentRequest());

	}

	protected void takeDown() {
		System.out.println("Adeus " + this.getAID().getName());
	}
	
}
