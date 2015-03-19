package gui;

import gui.actions.RequestRecommendationAction;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class RequestRecommendationPane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel usernameLabel;
	private JTextField username;
	private JButton requestRecommendationButton;
	
	public RequestRecommendationPane(StudentGUI myGui) {
		usernameLabel = new JLabel("Username no Huxley: ");
		username = new JTextField();
		username.setColumns(15);
		requestRecommendationButton = new JButton(new RequestRecommendationAction(myGui, this));
		
		setLayout(new MigLayout("", "grow", "grow"));
		initComponents();
	}

	private void initComponents() {
		add(createUsernamePane(), "center, wrap");
		add(requestRecommendationButton, "center");
	}
	

	private Component createUsernamePane() {
		JPanel usernamePane = new JPanel(new MigLayout());
		
		usernamePane.add(usernameLabel, "split 2");
		usernamePane.add(username, "center");
		
		return usernamePane;
	}

	public String getUsername() {
		return username.getText().trim();
	}

	public JButton getRequestRecommendationButton() {
		return requestRecommendationButton;
	}
}
