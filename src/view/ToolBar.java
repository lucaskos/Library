package view;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import listeners.ToolBarListener;

public class ToolBar extends JToolBar {
	private JButton updateBtn, refreshBtn, saveBtn;
	private ToolBarListener listener;
	private IconsNames[] names = IconsNames.values();
	private ArrayList<String> buttonNameList;

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
		buttonNameList = new ArrayList<>();
		
		for(IconsNames icons : IconsNames.values()) {
			buttonNameList.add(icons.name());
		}
		
		for(int i = 0 ; i < names.length; i++) {
			JButton btn = new JButton(buttonNameList.get(i));
			btn.setName(buttonNameList.get(i));
			String path = "/icons/" + buttonNameList.get(i) + "16.gif";
			btn.setIcon(createIcon(path));
			if(btn.getName().equalsIgnoreCase(buttonNameList.get(i))) {
				String actionName = buttonNameList.get(i);
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						listener.getAction(actionName);
					}
				});
			}
			add(btn);
		}
		saveBtn = new JButton();
		refreshBtn = new JButton();
		updateBtn = new JButton();
		updateBtn.setToolTipText("Update to database");
		refreshBtn.setToolTipText("Refresh");
		
		
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.updateHandler();
			}
		});

		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.refreshHandler();
			}
		});

	}

	public void setToolBarListener(ToolBarListener toolBarListener) {
		this.listener = toolBarListener;
	}
	
	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if (url == null) {
			System.err.println("Unable to load image: " + path);
		}
		return new ImageIcon(url);

	}
}
