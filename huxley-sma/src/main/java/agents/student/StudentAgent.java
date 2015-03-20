package agents.student;

import gui.StudentGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Problem;
import model.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import agents.student.behaviour.AskRecommendation;

public class StudentAgent extends Agent {

	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger(StudentAgent.class);

	private String username;
	private Request request ;
	private List<Problem> recommendedProblems;
	private List<String> recommendedTexts;
	private AID[] recommenderAgents;
	private StudentGUI myGui;
	private MessageTemplate mt;
	
	public StudentAgent() {
		request = new Request();
		recommendedProblems = new ArrayList<Problem>();
		username = "";
	}

	// Put agent initializations here
	@Override
	protected void setup() {

		// Printout a welcome message
		logger.info("O agente " + this.getAID().getName() + " está pronto.");
		
		myGui = new StudentGUI(this);

	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Adeus " + getAID().getName());
	}

	// Isso é chamado pela GUI quando o usuário insere seu username no Huxley
	// E clica em Solicitar recomendação
	public void informUsername(String huxleyUsername) {
		
		myGui.getRequestButton().setEnabled(false);
		
		if(!huxleyUsername.equals("")){
			// Se o usuário acabou de pedir recomendações
			if(username.toLowerCase().equals(huxleyUsername.toLowerCase())){
				// Apenas sorteamos um problema dessa lista, 
				// Caso ela esteja vazia, será feita uma nova solicitação
				chooseProblem();
			} else {
				
				// Caso contrário, se for outro usuário ou se a lista não contém mais problemas para recomendar
				// Enviamos solicitações para os agentes de recomendação
				username = huxleyUsername;
				request.setUsername(username);
				request.setNotWantedProblemsId(new ArrayList<Long>());
				sendNewRequest();
			}
									
		}
		
	}
	
	private void sendNewRequest() {
		
		// Buscando lista de recomendadores
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("recommender");
		template.addServices(sd);
		try {
			
			DFAgentDescription[] result = DFService.search(this,template);
			recommenderAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				recommenderAgents[i] = result[i].getName();
			}
			
		} catch (FIPAException fe) {
			logger.error(fe.getMessage(), fe);
		}
		
		addBehaviour(new AskRecommendation(this));
		
	}
	
	public void chooseProblem() {
		
		Random random = new Random();
		int randomIndex;
		Problem problem;
		String text;
		String title;
		
		if(recommendedProblems.isEmpty()) {
			sendNewRequest();
			
		} else {
			
			// Escolhendo um dos problemas recomendados aleatoriamente
			// recommendedProblems contém recomendações que foram recebidas dos agentes de recomendação
			// Essa lista é setada em WaitForRecommenderResponse quando as respostas chegam
			randomIndex = random.nextInt( recommendedProblems.size() ); 
			// O problema é removido da lista porque não será recomendado de novo, caso o usuário peça outra recomendação
			problem = recommendedProblems.remove(randomIndex);
			text = recommendedTexts.remove(randomIndex);
			
			// Guardamos o id do problema escolhido na lista de problemas que o estudante não deseja, que está em request,
			// para não o considerarmos mais em caso de outra recomendação ser solicitada aos agentes
			if(request.getNotWantedProblemsId().contains(problem.getId())){
				chooseProblem();
			}
			
			request.getNotWantedProblemsId().add(problem.getId());
			
			// Mostrando a recomendação
			title = username + ", " + text + " " + problem.getName();
			myGui.showProblem(title, problem.getDescription());
			myGui.getRequestButton().setEnabled(true);
			
		}
		
	}
	
	public void showRefusedMsg(String refusedMsg) {
		myGui.showProblem(refusedMsg,""); 		
	}
	
	public String getUsername() {
		return username;
	}

	public Request getRequest() {
		return request;
	}
	
	public AID[] getRecommenderAgents() {
		return recommenderAgents;
	}
	
	public StudentGUI getStudentGui() {
		return myGui;
	}
	
	public MessageTemplate getMt() {
		return mt;
	}
	
	public void setMt(MessageTemplate mt) {
		this.mt = mt;
	}
	
	public void setRecommendedProblems(List<Problem> recommendedProblems) {
		this.recommendedProblems = recommendedProblems;
	}

	public void setRecommendedTexts(List<String> recommendedTexts) {
		this.recommendedTexts = recommendedTexts;
	}

	
}
