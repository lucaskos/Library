package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Controller;
import listeners.MenuListener;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookListPanel bookListPanel;
	private BookDescriptionPanel descriptionPanel;
	private Controller controller;
	private MenuBar menuBar;
	private ToolBar toolBar;
	private JFileChooser fileChooser;
	private PrefsDialog prefsDialog;
	private String booksNumber;
	int row, column;
	private Preferences myPrefs;

	public MainFrame() {
		setTitle("Book Catalogue");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Window closing");
				dispose();
				System.gc();
			}

		});
		controller = new Controller();
		prefsDialog = new PrefsDialog();
		toolBar = new ToolBar();

		/*
		 * Setting preferences for database (default in mysql). That includes username, password and port number.
		 */
		myPrefs = Preferences.userRoot().node("library");

		String username = myPrefs.get("username", "");
		String password = myPrefs.get("password", "");
		Integer port = myPrefs.getInt("port", 3306);
		prefsDialog.setDefaults(username, password, port);
		bookListPanel = new BookListPanel(controller);
		descriptionPanel = new BookDescriptionPanel();
		menuBar = new MenuBar();
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());
		tempPanel.add(menuBar, BorderLayout.PAGE_START);
		tempPanel.add(toolBar, BorderLayout.AFTER_LAST_LINE);
		add(toolBar, BorderLayout.NORTH);
		add(bookListPanel, BorderLayout.WEST);
		add(descriptionPanel, BorderLayout.CENTER);
		add(new JLabel(booksNumber), BorderLayout.SOUTH);
		/*
		 * Setting up toolbarlistener and updating functionalaties of buttons
		 * inside the ToolBarPanel. updateHandler() connect to database, update
		 * the records from the left side table to db if connection
		 * possible(password, username, and port given and correct). If values
		 * to connect are not given show the prefsDialog() to enter them and try
		 * again.
		 */
		toolBar.setToolBarListener(new ToolBarListener() {
			public void updateHandler() {
				connect();
				try {
					controller.save();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Cannot update the database.",
							"Database connection error.", JOptionPane.ERROR_MESSAGE);
				}
				System.out.println("Updating...\n" + myPrefs.get("username", "") + " : " + myPrefs.get("password", ""));
				if (myPrefs.get("username", "").isEmpty() || myPrefs.get("password", "").isEmpty()) {
					JOptionPane.showMessageDialog(MainFrame.this,
							"Both username and password value must be entered.\nPlease provide valid values.",
							"Empty Preferences", JOptionPane.ERROR_MESSAGE);
					setPreferences();
				}

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see view.ToolBarListener#refreshHandler() Getting values from
			 * database and retrieving those to table.
			 */
			public void refreshHandler() {
				System.out.println("refreshing...");
				connect();
				try {
					controller.load();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(MainFrame.this, "Unable to load from database.",
							"Database connection error.", JOptionPane.ERROR_MESSAGE);
				}
				bookListPanel.refresh();
			}
		});
		/*
		 * Removing row from a table causes a removal of record from List<Book>.
		 */
		bookListPanel.setBookTableListener(new BookTableListener() {
			public void rowDeleted(int row) {
				controller.removeBook(row);
				// System.out.println(controller.getBook(row));

			}
		});
		/*
		 * Getting values from right side of the program. Creating new Book
		 * object and adding it to the table.
		 */
		descriptionPanel.setBookActionListener(new BookListener() {
			public void formEventHandler(String title, String author, int isbn, String genre) {
				Boolean temp = controller.checkIfExists(title, isbn);
				if (temp == true) {
					controller.addBooks(title, author, isbn, genre);
					bookListPanel.refresh();
				} else {
					JOptionPane.showMessageDialog(MainFrame.this, "Cannot be added. Duplicate", "Duplicate error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		fileChooser = new JFileChooser();
		menuBar.setMenuListener(new MenuListener() {
			public void takeJemenu(JMenuItem item) {
				String command = item.getActionCommand();
				switch (command) {
				case "New":
					controller.newFile();
					break;
				case "Open":
					if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
						controller.openFile(fileChooser.getSelectedFile());
					}

					break;
				case "Save":
					if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
						controller.saveToFile(fileChooser.getSelectedFile());
					}
					break;
				case "Exit":
					System.exit(1);
					break;
				case "About":
					System.out.println("About");
					break;
				case "Preferences...":
					setPreferences();
					break;
				default:
					break;
				}
				bookListPanel.refresh();

			}
		});

		setVisible(true);
	}

	// private so it cannot be accessed from different class.
	private void connect() {
		try {
			controller.connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainFrame.this, "Cannot connect to database.", "Database connection error.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setPreferences() {
		prefsDialog.setLocationRelativeTo(MainFrame.this);
		prefsDialog.setVisible(true);
		prefsDialog.setPrefsListener(new PrefsListener() {
			public void setPreferences(String user, String password, int port) {
				try {
					myPrefs.flush();
					myPrefs.put("username", user);
					myPrefs.put("password", password);
					myPrefs.putInt("port", port);
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}

			}
		});

	}

}