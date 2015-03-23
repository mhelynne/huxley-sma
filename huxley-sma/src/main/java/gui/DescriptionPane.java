package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class DescriptionPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JLabel description;
	DescriptionScrollPane scrollPane;

	public DescriptionPane(String problemTitle, String problemDescription) {
		title = new JLabel();
		description = new JLabel();
		
		scrollPane = new DescriptionScrollPane();
		scrollPane.setLayout(new BorderLayout());
	
		setLayout(new MigLayout("", "grow", "[][grow]"));
		initComponents();
	}

	private void initComponents() {
		add(title, "gap 20 20 20 20, center, wrap");
		scrollPane.add(description);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(scrollPane);
		
		add(scroll, "grow");
	}

	public void setTitle(String problemTitle) {
		this.title.setText("<html><b>" + problemTitle + "</b></html>");
	}

	public void setDescription(String problemDescription) {

		problemDescription = problemDescription.trim();

		if (problemDescription.startsWith("<")) {
			this.description.setText("<html>" + problemDescription + "</html>");
		} else {
			this.description.setText("<html>Descrição do problema não encontrada</html>");
		}

	}
	
	public DescriptionScrollPane getScrollPane() {
		return scrollPane;
	}

}
