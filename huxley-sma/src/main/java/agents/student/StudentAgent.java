package agents.student;

import gui.StudentGui;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.student.behaviour.AskRecommendation;

public class StudentAgent extends Agent {

	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger(StudentAgent.class);

	private String username;
	private AID[] recommenderAgents;
	private StudentGui myGui;
	private MessageTemplate mt;

	// Put agent initializations here
	@Override
	protected void setup() {

		// Printout a welcome message
		logger.info("O agente " + this.getAID().getName() + " está pronto.");

		myGui = new StudentGui(this);
		myGui.showGui();

	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Adeus " + getAID().getName());
	}

	// Isso é chamado pela GUI quando o usuário insere seu username no Huxley
	public void informUsername(String huxleyUsername) {
		
		username = huxleyUsername;
		
		// Buscando lista de recomendadores
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("recommender");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this,template);
			// System.out.println("Encontrando os agentes recomendadores");
			recommenderAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				recommenderAgents[i] = result[i].getName();
				// System.out.println(recommenderAgents[i].getName());
			}
		} catch (FIPAException fe) {
			logger.error(fe.getMessage(), fe);
		}
		
		addBehaviour(new AskRecommendation(this));
	}

	public String getUsername() {
		return username;
	}

	public AID[] getRecommenderAgents() {
		return recommenderAgents;
	}
	
	public StudentGui getStudentGui() {
		return myGui;
	}
	
	public MessageTemplate getMt() {
		return mt;
	}
	
	public void setMt(MessageTemplate mt) {
		this.mt = mt;
	}

}
