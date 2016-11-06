package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.MenuEvent;

import controller.Controller;
import listeners.MenuListener;

public class MenuBar extends JMenuBar {
	private JMenu fileMenu, optionMenu, aboutMenu;
	private JMenuItem newItem, openItem, saveItem, exitItem, aboutItem, prefsItem;
	JFileChooser fileChooser;
	private MenuListener listener;
	
	public MenuBar() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newItem = new JMenuItem("New");
		newItem.setMnemonic(KeyEvent.VK_N);
		fileMenu.add(newItem);

		saveItem = new JMenuItem("Save");
		saveItem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveItem);
		
		openItem = new JMenuItem("Open");
		openItem.setMnemonic(KeyEvent.VK_O);
		fileMenu.add(openItem);

		fileMenu.addSeparator();

		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		fileMenu.add(exitItem);

		this.add(fileMenu);
		
		optionMenu = new JMenu("Options");
		prefsItem = new JMenuItem("Preferences...");
		optionMenu.add(prefsItem);
		
		this.add(optionMenu);
		
		
		aboutMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		aboutMenu.add(aboutItem);
		
		this.add(aboutMenu);
		//fileChooser = new JFileChooser();

		// listeners
		addActionListeners(newItem, saveItem, openItem, exitItem, aboutItem, prefsItem);
	}

	public void setMenuListener(MenuListener listener) {
		this.listener = listener;

	}
	
	//public function to add listeners to elements of this MenuBar, take all elements of Class Jmenuitem
	//pointless to add multiple action listeners if more elements are about to be added
	//main frame deals will this actions !
	public void addActionListeners(JMenuItem ... item) {
		for (int i = 0; i < item.length; i++) {
			item[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					listener.takeJemenu((JMenuItem) e.getSource());
				}
			});
		}
		
	}
}
