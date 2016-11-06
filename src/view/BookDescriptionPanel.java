package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BookDescriptionPanel extends JPanel {

	private JComponent titleLabel, authorLabel, isbnLabel, genreLabel;
	private JTextField titleField, authorField, isbnField;
	private JComboBox<Object> genresBox;
	private JButton addBtn, clearBtn;
	private JSeparator separator;

	private static final String titleString = "Title";
	private static final String authorString = "Author";
	private static final String isbnString = "ISBN";
	private static final String genreString = "Genre";

	private JPanel panel;

	private BookListener bookListener;

	public BookDescriptionPanel() {
		add(createGui(), BorderLayout.LINE_START);

		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = titleField.getText();
				String author = authorField.getText();
				String isbn = isbnField.getText();
				String genre = genresBox.getSelectedItem().toString();
				
				if (checkIfEmpty(title, titleString) != false && checkIfEmpty(author, authorString) != false
						&& checkIfEmpty(isbn, isbnString) != false && checkIfEmpty(genre, genreString) != false) {
					bookListener.formEventHandler(title, author, Integer.parseInt(isbn), genre);
				}

			}


			private Boolean checkIfEmpty(String temp, String name) {
				if (temp.isEmpty() || temp.equals("") || temp == "") {
					JOptionPane.showMessageDialog(new JFrame(), "Please provide " + name, "Warning!",
							JOptionPane.WARNING_MESSAGE);
				} else {

					return true;
				}
				return false;

			}

		});

		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				titleField.setText("");
				authorField.setText("");
				isbnField.setText("");
			}
		});
	}

	private JPanel createGui() {
		panel = new JPanel();
		setBorder(BorderFactory.createTitledBorder("Add book"));

		titleLabel = labelCreation(this, titleString + ": ");
		authorLabel = labelCreation(this, authorString + ": ");
		isbnLabel = labelCreation(this, isbnString + ": ");
		genreLabel = labelCreation(this, genreString + ": ");

		titleField = new JTextField(15);
		authorField = new JTextField(15);
		isbnField = new JTextField(15);
		isbnField.setToolTipText("Must be 10 or 13 digits");
		
		separator = new JSeparator(SwingConstants.HORIZONTAL);

		titleField.setText(titleString);
		authorField.setText(authorString);
		isbnField.setText("1");
		
		addBtn = new JButton("Add");
		clearBtn = new JButton("Clear");
		
		//Setting up genres from enum WITHOUT !ALL!
		//for JComboBox
		ArrayList<String> temp = new ArrayList<>();
		for(Genres value : Genres.values()) {
			temp.add(value.toString().toLowerCase());
		}
		temp.remove(0);
		
		genresBox = new JComboBox<>(temp.toArray());
		//end of setting up JComboBox 
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		
		panel.setLayout(gbl);
		gc.gridx = 0;
		gc.gridy = 0;

		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = gc.FIRST_LINE_START;

		// first row
		gc.insets = new Insets(10, 3, 0, 3);
		gbl.setConstraints(titleLabel, gc);
		panel.add(titleLabel);

		gc.gridx++;
		gbl.setConstraints(titleField, gc);
		panel.add(titleField);
		// new row
		gc.gridy++;
		gc.gridx = 0;
		gbl.setConstraints(authorLabel, gc);
		panel.add(authorLabel);

		gc.gridx++;
		gbl.setConstraints(authorField, gc);
		panel.add(authorField);

		// new row
		gc.gridy++;
		gc.gridx = 0;
		gbl.setConstraints(isbnLabel, gc);
		panel.add(isbnLabel);

		gc.gridx++;
		gbl.setConstraints(isbnField, gc);
		panel.add(isbnField);

		// new row
		gc.gridy++;
		gc.gridx = 0;
		gbl.setConstraints(genreLabel, gc);
		panel.add(genreLabel);

		gc.gridx++;
		gbl.setConstraints(genresBox, gc);
		panel.add(genresBox);

		// new row
		gc.fill = gc.HORIZONTAL;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.gridwidth = 2;
		gc.gridx = 0;
		gc.gridy++;
		gbl.setConstraints(separator, gc);
		panel.add(separator);
		
		// Button addition
		gc.fill = gc.NONE;
		gc.gridwidth = 0;;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.gridy++;
		gc.gridx = 0;
		gbl.setConstraints(addBtn, gc);
		panel.add(addBtn);

		gc.gridx++;
		gbl.setConstraints(clearBtn, gc);
		panel.add(clearBtn);
		
		gc.fill = gc.HORIZONTAL;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.gridwidth = 2;
		gc.gridx = 0;
		gc.gridy++;
		panel.add(new JSeparator(), gc);
		

		return panel;

	}

	private JComponent labelCreation(JComponent component, String string) {
		component = new JLabel(string);
		return component;
	}

	public void setBookActionListener(BookListener bookListener) {
		this.bookListener = bookListener;
	}
}
