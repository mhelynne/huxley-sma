package gui.actions;

import gui.RequestRecommendationPane;
import gui.StudentGUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class RequestRecommendationAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private StudentGUI myGui;
	private RequestRecommendationPane myPane;
	
	public RequestRecommendationAction(StudentGUI myGui, RequestRecommendationPane myPane) {
		super("Solicitar recomendação");
		this.myGui = myGui;
		this.myPane = myPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		myGui.getMyAgent().informUsername( myPane.getUsername() );
		
	}

	
}
