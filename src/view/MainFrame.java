package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import listeners.BookListener;
import listeners.BookTableListener;
import listeners.MenuListener;
import listeners.PrefsListener;
import listeners.ToolBarListener;

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
	private ArrayList<String> removedRows;
	private JSplitPane splitPane;

	public MainFrame() {
		setTitle("Book Catalogue");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane();
		/*
		 * dispose() closes window and releases all the resorces uses by it.
		 * System.gc() runs garbage collector.
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.gc();
			}

		});
		FileFilter defaultFilter = new FileFilter() {
			public String getDescription() {
				return ".txt files";
			}

			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					String filename = file.getName().toLowerCase();
					return filename.endsWith(".txt") || filename.endsWith(".TXT");
				}
			}
		};
		controller = new Controller();
		prefsDialog = new PrefsDialog();
		toolBar = new ToolBar();
		bookListPanel = new BookListPanel(controller);
		descriptionPanel = new BookDescriptionPanel();
		menuBar = new MenuBar();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descriptionPanel, bookListPanel);

		/*
		 * Setting preferences for database (default in mysql). That includes
		 * username, password and port number.
		 */
		myPrefs = Preferences.userRoot().node("library");

		String username = myPrefs.get("username", "");
		String password = myPrefs.get("password", "");
		Integer port = myPrefs.getInt("port", 3306);
		prefsDialog.setDefaults(username, password, port);

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());
		tempPanel.add(menuBar, BorderLayout.NORTH);
		tempPanel.add(toolBar, BorderLayout.CENTER);
		add(tempPanel, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
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
					if (removedRows != null) {
						int temp;
						// controller.deleteRowsFromDb(removedRows);
						for (String rows : removedRows) {
							temp = Integer.parseInt(rows);
							if (controller.removeBookFromDatabase(temp) == true) {
								System.out.println("failed to delete records");
							}
						}

					}
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

			public void saveHandler() {
				save(defaultFilter);
			}

			public void getAction(String action) {
			action.toLowerCase();
				if(action.equalsIgnoreCase(IconsNames.Save.toString())) {
					System.out.println("saving");
				}
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

			@Override
			public void deletedRows(ArrayList<String> removedItems) {
				removedRows = removedItems;
			}
		});
		/*
		 * Getting values from bookListPanel (form). Creating new Book object
		 * and adding it to the table.
		 */
		descriptionPanel.setBookActionListener(new BookListener() {
			public void formEventHandler(String title, String author, long isbn, String genre) {
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
		fileChooser.setAcceptAllFileFilterUsed(false); // removes the accept-all
		menuBar.setMenuListener(new MenuListener() {
			public void takeJemenu(JMenuItem item) {

				String command = item.getActionCommand();
				switch (command) {
				case "New":
					controller.newFile();
					break;
				case "Open":
					fileChooser.setFileFilter(defaultFilter);
					if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
						controller.openFile(fileChooser.getSelectedFile());
					}

					break;
				case "Save":
					save(defaultFilter);
					break;
				case "Export":
					fileChooser.resetChoosableFileFilters();
					fileChooser.setFileFilter(new FileFilter() {
						public String getDescription() {
							return ".JSON";
						}

						public boolean accept(File f) {
							if (f.isDirectory()) {
								return true;
							} else {
								return f.getName().endsWith(".json") || f.getName().endsWith(".JSON");
							}
						}
					});
					// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
						controller.exportFile(new File(fileChooser.getSelectedFile() + ".json"));
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

	private void save(FileFilter filter) {
		fileChooser.setFileFilter(filter);
		if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
			controller.saveToFile(new File(fileChooser.getSelectedFile() + ".txt"));
		}
	}
}