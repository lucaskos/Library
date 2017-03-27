package view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar {
	private JButton updateBtn, refreshBtn;
	private ToolBarListener listener;

	public ToolBar() {
		/*
		 * For the drag behavior to work correctly, the tool bar must be in a
		 * container that uses the BorderLayout layout manager. The component
		 * that the tool bar affects is generally in the center of the
		 * container. The tool bar must be the only other component in the
		 * container, and it must not be in the center.
		 * 
		 * Check BoxLayout and how it affects dragging.
		 */
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setFloatable(false);
		updateBtn = new JButton("Update");
		refreshBtn = new JButton("Refresh");
		updateBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				listener.updateHandler();
			}
		});
		
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.refreshHandler();
			}
		});
		
		add(updateBtn);
		add(refreshBtn);
	}
	public void setToolBarListener(ToolBarListener toolBarListener) {
		this.listener = toolBarListener;
	}
}
