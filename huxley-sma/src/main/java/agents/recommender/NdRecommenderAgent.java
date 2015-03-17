package agents.recommender;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.recommender.behaviour.NdRequestData;

public class NdRecommenderAgent extends RecommenderAgent {

	private static final long serialVersionUID = 211L;

	static Logger logger = LoggerFactory.getLogger(NdRecommenderAgent.class);

	@Override
	protected void setup() {

		this.agentName = "level-recommender";
		super.setup();
		
	}

	@Override
	public Behaviour recommenderToDataBehaviour(AID dataAgent, String username, ACLMessage msgFromStudent) {
		
		return new NdRequestData(dataAgent, username, msgFromStudent);
		
	}
	
}
