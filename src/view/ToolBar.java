package view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import listeners.ToolBarListener;

public class ToolBar extends JToolBar {
	private JButton updateBtn, refreshBtn;
	private ToolBarListener listener;

	public ToolBar() {
		setBorder(new EtchedBorder());
		/*
		 * For the drag behavior to work correctly, the tool bar must be in a
		 * container that uses the BorderLayout layout manager. The component
		 * that the tool bar affects is generally in the center of the
		 * container. The tool bar must be the only other component in the
		 * container, and it must not be in the center.
		 * 
		 * Check BoxLayout and how it affects dragging.
		 */
		setFloatable(false);
		updateBtn = new JButton("Update");
		refreshBtn = new JButton("Refresh");
		updateBtn.setIcon(createIcon("/icons/Export16.gif"));
		refreshBtn.setIcon(createIcon("/icons/Refresh16.gif"));
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
	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if(url == null) {
			System.err.println("Unable to load image: " + path);
		}
		return new ImageIcon(url);
		
	}
}
