package gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class LogoPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private ImageIcon image;
	private JLabel imageLabel;
	private JLabel titleLabel;

	public LogoPane() {
		titleLabel = new JLabel(
				"<html><center><h2>"
				+ " Sistema Multiagentes"
				+ " para recomendação de"
				+ " problemas de programação do Huxley"
				+ " </h2></center></html>");
			
		image = new ImageIcon("src/resources/huxley-logo.png");
		imageLabel = new JLabel("", image, JLabel.CENTER);

		setLayout(new MigLayout("", "grow", "grow"));
		setBackground(Color.WHITE);
		initComponents();
	}

	private void initComponents() {
		add(imageLabel);
		add(titleLabel);
		
	}
}
