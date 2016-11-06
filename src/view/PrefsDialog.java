package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class PrefsDialog extends JDialog {
	private JLabel userLabel, passLabel, portLabel;
	private JSpinner spinner;
	private SpinnerNumberModel spinnerModel;
	private JTextField userField;
	private JPasswordField passField;
	private JPanel panel;
	private JButton okBtn, cancelBtn;
	private PrefsListener prefsListener;

	PrefsDialog() {
		setVisible(false);
		setLayout();
		setSize(300, 300);

	}

	private void setLayout() {
		userLabel = new JLabel("Username: ");
		passLabel = new JLabel("Password: ");
		portLabel = new JLabel("Port: ");

		userField = new JTextField(10);
		passField = new JPasswordField(10);

		okBtn = new JButton("Confirm");
		cancelBtn = new JButton("Cancel");

		spinnerModel = new SpinnerNumberModel(3306, 1, 9999, 1);
		spinner = new JSpinner(spinnerModel);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();

		panel = new JPanel();
		panel.setLayout(gbl);

		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(10, 20, 0, 0);
		// first line

		gc.gridy++;
		gc.gridx = 0;
		panel.add(userLabel, gc);

		gc.gridx = 1;
		panel.add(userField, gc);

		// next line
		gc.gridy++;
		gc.gridx = 0;
		panel.add(passLabel, gc);

		gc.gridx = 1;
		panel.add(passField, gc);

		// next line
		gc.gridy++;
		gc.gridx = 0;
		gbl.setConstraints(portLabel, gc);
		panel.add(portLabel);

		gc.gridx = 1;
		gbl.setConstraints(spinner, gc);
		panel.add(spinner);

		// next line
		gc.insets = new Insets(10, 40, 0, 0);
		gc.gridy++;
		gc.gridx = 0;

		gbl.setConstraints(okBtn, gc);
		panel.add(okBtn);

		gc.gridx = 1;
		gbl.setConstraints(cancelBtn, gc);
		panel.add(cancelBtn);

		// next line

		this.add(panel);

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer port = (Integer) spinner.getValue();
				char[] password = passField.getPassword();
				String user = userField.getText();
				if (prefsListener != null) {
					prefsListener.setPreferences(user, new String(password), port);
				}
				setVisible(false);
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;
	}

	public void setDefaults(String username, String password, Integer port) {
		userField.setText(username);
		passField.setText(password);
		spinner.setValue(port);
	}

}
