package agents.recommender.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

import model.ProblemSubmission;
import model.Request;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.JsonReader;

public class RequestData extends Behaviour {

	private static final long serialVersionUID = 211L;

	static Logger logger = LoggerFactory.getLogger(RequestData.class);

	protected AID dataAgent;
	protected Request request;
	protected String username;
	protected List<Long> notWantedProblemsId;
	
	protected MessageTemplate mt; // The template to receive replies
	protected long recommendedId;
	protected String jsonProblem;
	protected String refuseMsg;
	
	private ACLMessage msgFromStudent;
	private boolean finished = false;
	
	// O agente de dados a ser consultado, o username para ser enviado ao agente de dados e a mensagem que precisa ser respondida
	public RequestData(AID dataAgent, Request request, ACLMessage msgFromStudent) {
		this.dataAgent = dataAgent;
		this.request = request;
		this.username = request.getUsername();
		this.msgFromStudent = msgFromStudent;
	}
	
	protected void requestProblemSubmissionList() {
		
		// Solicita uma lista das submissões do usuário e dos problemas associados a essa submissão
		
		// Esse username será enviado para o agente de dados
		JSONObject usernameJson = new JSONObject();
		usernameJson.put("username", username);

		// Criando mensagem para enviar ao Agente Dados,
		// solicitando as informações do usuário

		ACLMessage msgToData = new ACLMessage(ACLMessage.REQUEST);
		msgToData.addReceiver(dataAgent);
		msgToData.setOntology("request-problem-submission-list");
		msgToData.setContent(usernameJson.toString());
		msgToData.setConversationId("rqs");
		msgToData.setReplyWith("rqs" + System.currentTimeMillis()); // Unique value

		logger.info("Enviando " + msgToData.getOntology() + " : " + usernameJson);

		myAgent.send(msgToData);

		// Prepare the template to get answer
		mt = MessageTemplate.and(
				MessageTemplate.MatchConversationId("rqs"),
				MessageTemplate.MatchInReplyTo(msgToData.getReplyWith()));
			
	}
	
	protected void requestProblemById() {
	
		// Solicita ao agente de dados, os dados do problema que possui esse id
		
		JSONObject idJson = new JSONObject();
		idJson.put("id", recommendedId);

		// Criando mensagem para enviar ao Agente Dados,
		// solicitando os dados do problema com o id recomendado

		ACLMessage msg2ToData = new ACLMessage(ACLMessage.REQUEST);
		msg2ToData.addReceiver(dataAgent);
		msg2ToData.setOntology("request-problem-by-id");
		msg2ToData.setContent(idJson.toString());
		msg2ToData.setConversationId("rqs");
		msg2ToData.setReplyWith("rqs" + System.currentTimeMillis()); // Unique value

		logger.info("Enviando " + msg2ToData.getOntology() + " : " + recommendedId);

		myAgent.send(msg2ToData);

		// Prepare the template to get answer
		mt = MessageTemplate.and(
				MessageTemplate.MatchConversationId("rqs"),
				MessageTemplate.MatchInReplyTo(msg2ToData.getReplyWith()));
		
	}
	
	protected void sendResponseToStudent(String msg) {
		
		// Responde ao agente estudante com um problema recomendado
		
		// Aqui utilizamos ACLMessage msgFromStudent	
		ACLMessage replyToStudent = msgFromStudent.createReply();
		replyToStudent.setReplyWith(msgFromStudent.getReplyWith());

		// Preparando o resultado a ser enviado (problema + mensagem)
		JSONArray jsonResult = new JSONArray();
		
		jsonResult.put(new JSONObject(jsonProblem) );
		jsonResult.put(msg); // Uma mensagem que acompanhará o nome do problema na caixa de diálogo,
							 // por exemplo, "Tente resolver o problema"
		
		// Compondo e enviando a mensagem
		replyToStudent.setPerformative(ACLMessage.PROPOSE);
		replyToStudent.setOntology("problem-text");
		replyToStudent.setContent(jsonResult.toString());
		
		logger.info("Enviando "+ replyToStudent.getOntology() + ": "+ jsonResult);
		myAgent.send(replyToStudent);
		
		finished = true;
		
	}

	protected void sendRefuseToStudent() {
		
		// Responde ao agente estudante, caso não encontre recomendação
		
		// Aqui utilizamos ACLMessage msgFromStudent	
		ACLMessage replyToStudentError = msgFromStudent.createReply();
		replyToStudentError.setReplyWith(msgFromStudent.getReplyWith());
		
		// Compondo e enviando a mensagem
		replyToStudentError.setPerformative(ACLMessage.REFUSE);
		replyToStudentError.setOntology("refuse");
		replyToStudentError.setContent(refuseMsg);
		
		logger.info("Enviando "+ replyToStudentError.getOntology());
		myAgent.send(replyToStudentError);
		
		finished = true;
		
	}
	
	protected List<ProblemSubmission> readProblemSubmissionListMessage(String content) {

		List<ProblemSubmission> problemSubmissionList;
		ProblemSubmission problemSubmission;
		JSONArray jsonArray;
		JSONObject jsonObject;

		jsonArray = new JSONArray(content);

		problemSubmissionList = new ArrayList<ProblemSubmission>();
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			problemSubmission = (ProblemSubmission) JsonReader.readValueAsObject(jsonObject.toString(),
							     ProblemSubmission.class);
			if (problemSubmission != null) {
				problemSubmissionList.add(problemSubmission);
			}
		}

		return problemSubmissionList;
		
	}

	@Override
	public boolean done() {		
		return finished;
	}

	@Override
	public void action() {
		
	}

}
