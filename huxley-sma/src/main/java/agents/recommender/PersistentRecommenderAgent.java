package agents.recommender;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import model.Request;
import agents.recommender.behaviour.PersistentRequestData;

public class PersistentRecommenderAgent extends RecommenderAgent {	

	private static final long serialVersionUID = 22L;
	
	@Override
	protected void setup() {
		this.agentName = "persistent-recommender";
		super.setup();
	}

	@Override
	public Behaviour recommenderToDataBehaviour(AID dataAgent, Request request,
												ACLMessage msgFromStudent) {
		
		return new PersistentRequestData(dataAgent, request, msgFromStudent);
		
	}

}
