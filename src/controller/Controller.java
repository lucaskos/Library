package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Book;
import model.Database;

public class Controller {
	Database db = new Database();

	public List<Book> getBooks() {
		return db.getBooks();
	}

	public void addBooks(String title, String author, int isbn, String genre) {
		Book book = new Book(title, author, isbn, genre);
		db.addBook(book);

	}

	public int getDbSize() {
		return db.getListSize();
	}

	public Book getBook(int index) {
		return (Book) db.getBook(index);
	}

	public void saveToFile(File file) {
		try {
			db.saveToFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newFile() {
		db.newFile();
	}

	public void openFile(File file) {
		try {
			System.out.println("opening file " + file.getAbsolutePath());
			db.openFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean checkIfExists(String title, int isbn) {
		List<Book> tempList = getBooks();
		String tempTitle = "";
		int tempIsbn;
		for(int i = 0; i < tempList.size() ; i++) {
			tempTitle = tempList.get(i).getTitle().toLowerCase();
			tempIsbn = tempList.get(i).getIsbn();
			if((tempTitle.equals(title.toLowerCase()) && tempIsbn == isbn) || tempIsbn == isbn) {
				return false;
			}
		}
		return true;
		
	}
	
	public void save() throws SQLException {
		db.save();
	}
	public void connect() throws Exception {
		db.connect();
	}
	public void disconnect() {
		db.disconnect();
	}
	public void load() throws SQLException {
		db.load();
	}

	public void refresh() {
		db.refresh();
	}

	public void removeBook(int index) {
		db.removeBook(index);
	}

	public void deleteRowsFromDb(ArrayList<String> removedRows) throws SQLException {
		db.deleteRowsFromDb(removedRows);
	}
	
	public void removeBookFromDatabase(int ids) throws SQLException{
		db.deleteCellsFromDb(ids);
	}

}
