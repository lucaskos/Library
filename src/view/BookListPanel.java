package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import controller.Controller;

public class BookListPanel extends JPanel {
	private BookTableListener bookTableListener;
	private JPopupMenu popup;
	private TableModel tableModel;
	int row;
	int column;
	String value = "";

	public BookListPanel(Controller controller) {
		tableModel = new TableModel(controller);
		popup = new JPopupMenu();
		JMenuItem removeItem = new JMenuItem("Remove row");
		JMenuItem editItem = new JMenuItem("Edit");
		popup.add(removeItem);
		popup.add(editItem);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Book list"));
		JTable table = new JTable();
		table.setModel(tableModel);
		table.setEnabled(true);
		table.setModel(tableModel);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		setVisible(true);

		/*
		 * Using mouseadapter because MouseListener must! implements all 4
		 * methods whereas MouseAdapter can use all or any no. of those.
		 */

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				row = table.rowAtPoint(e.getPoint());
				column = table.columnAtPoint(e.getPoint());
				System.out.println(row + " : column : " + column);
				table.getSelectionModel().setSelectionInterval(row, row);
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (column == 0) {
						editItem.setEnabled(false);
					} else {
						editItem.setEnabled(true);
					}
					popup.show(table, e.getX(), e.getY());

				}
			}
		});

		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (bookTableListener != null) {
					bookTableListener.rowDeleted(row);
					tableModel.fireTableRowsDeleted(row, row);
				}
			}
		});

		/*
		 * Implement editable single cells inside the table. Call for table.fire
		 * function to update the changes inside the table.
		 * 
		 */
		editItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Check if column is isbn because it should take only numbers.
				 */
				if (column == 3) {
					Document doc = new PlainDocument() {
						@Override
						public void insertString(int offs, String str, AttributeSet attr) throws BadLocationException {
							String newstr = str.replaceAll(" ", ""); // could
																		// use
																		// "\\s"
																		// instead
																		// of "
																		// "
							super.insertString(offs, newstr, attr);
						}

						@Override
						public void replace(int offs, int len, String str, AttributeSet attr)
								throws BadLocationException {
							String newstr = str.replaceAll(" ", ""); // could
																		// use
																		// "\\s"
																		// instead
																		// of "
																		// "
							super.replace(offs, len, newstr, attr);
						}
					};
					JTextField isbnField = new JTextField();
					isbnField.setDocument(doc);
					isbnField.setToolTipText("Value no longer than 13 characters");
					((AbstractDocument) isbnField.getDocument()).setDocumentFilter(new DocumentFilter() {
						Pattern regEx = Pattern.compile("\\d+");

						@Override
						public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
								throws BadLocationException {
							Matcher matcher = regEx.matcher(text);
							if (!matcher.matches()) {
								return;
							}
							super.replace(fb, offset, length, text, attrs);
						}
					});
					JComponent[] inputs = new JComponent[] { new JLabel("ISBN number :"), isbnField };
					int showConfirmDialog = JOptionPane.showConfirmDialog(null, inputs, "ISBN number",
							JOptionPane.OK_CANCEL_OPTION);
					String value = isbnField.getText();
					
					/*
					 * Implement checking whether the ISBN value is already in
					 * table. If so prompt user to enter correct value with
					 * information about duplicates.
					 */
					if (showConfirmDialog == 0) {
						if (!value.isEmpty()) {
							tableModel.setValueAt(Integer.parseInt(value), row, column);
						} 
					}
					/*
					 * Taking each column except the isbn columns
					 */
				} else {
					value = JOptionPane.showInputDialog(null, "Enter " + tableModel.getColumnName(column));
					if (value != null) {
						tableModel.setValueAt(value, row, column);
					}
				}

				tableModel.fireTableCellUpdated(row, column);
			}
		});
	}

	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	public void setBookTableListener(BookTableListener listener) {
		this.bookTableListener = listener;
	}
}
