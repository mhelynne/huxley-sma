package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class DescriptionPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	//private JLabel description;

	public DescriptionPane(String problemTitle) {
		title = new JLabel();
		//description = new JLabel("<html>" + problemDescription + "</html>");

		setLayout(new MigLayout("", "grow", "[][grow]"));
		initComponents();
	}

	private void initComponents() {
		add(title, "center, wrap");
		//add(createdDescriptionPane(), "center, wrap, grow");
	}

//	private Component createdDescriptionPane() {
//		JPanel descriptionPane = new JPanel();
//		descriptionPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//
//		descriptionPane.add(description);
//
//		return descriptionPane;
//	}
	
	public void setTitle(String problemTitle) {
		this.title.setText("<html><b>" + problemTitle + "</b></html>");
	}
	
//	public void setDescription(String problemDescription) {
//		this.description.setText("<html>" + problemDescription + "</html>");
//	}
	
}
