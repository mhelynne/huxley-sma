package gui.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CancelAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	Window window;

	public CancelAction(Window parent) {
		super("Fechar");
		this.window = parent;	                       
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (window != null) {
			window.dispose();
		}
		
	}
}
