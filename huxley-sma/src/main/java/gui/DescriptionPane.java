package gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class DescriptionPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JLabel description;

	public DescriptionPane(String problemTitle, String problemDescription) {
		title = new JLabel();
		description = new JLabel();

		setLayout(new MigLayout("", "grow", "[][grow]"));
		initComponents();
	}

	private void initComponents() {
		add(title, "gap 20 20 20 20, center, wrap");
		add(createdDescriptionPane(), "grow");
	}

	private Component createdDescriptionPane() {
		JPanel descriptionPane = new JPanel(new MigLayout("", "[grow]",	"[grow]"));
		descriptionPane.add(description, "gaptop 20, align center top");

		JScrollPane scroll = new JScrollPane(descriptionPane);

		return scroll;
	}

	public void setTitle(String problemTitle) {
		this.title.setText("<html><b>" + problemTitle + "</b></html>");
	}

	public void setDescription(String problemDescription) {

		problemDescription = problemDescription.trim();

		if (problemDescription.startsWith("<")) {
			this.description.setText("<html>" + problemDescription + "</html>");
		} else {
			this.description
					.setText("<html>Descrição do problema não encontrada</html>");
		}

	}

}
