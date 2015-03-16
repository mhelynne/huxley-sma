package agents.data;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.data.behaviour.WaitForRecommenderRequest;

public class DataAgent extends Agent {

	private static final long serialVersionUID = 3L;

	static Logger logger = LoggerFactory.getLogger(DataAgent.class);

	@Override
	protected void setup() {
		// Registra o serviço agente recomendador nas páginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("data");
		sd.setName("data");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		logger.info("O agente " + this.getAID().getName() + " está pronto.");

		// Comportamentos do agente
		addBehaviour(new WaitForRecommenderRequest());

	}

	protected void takeDown() {
		System.out.println("Adeus " + this.getAID().getName());
	}

}
