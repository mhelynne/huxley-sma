package agents.recommender;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import model.Request;
import agents.recommender.behaviour.AleatoryRequestData;

public class AleatoryRecommenderAgent extends RecommenderAgent {

	private static final long serialVersionUID = 23L;

	@Override
	protected void setup() {

		this.agentName = "aleatory-recommender";
		super.setup();
		
	}

	@Override
	public Behaviour recommenderToDataBehaviour(AID dataAgent, Request request, ACLMessage msgFromStudent) {
		
		return new AleatoryRequestData(dataAgent, request, msgFromStudent);
		
	}

}
