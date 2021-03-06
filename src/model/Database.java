package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
	List<Book> bookList = new LinkedList<Book>();

	private Connection con;

	public Database() {
	}

	public void addBook(Book book) {
		bookList.add(book);
	}

	public void removeBook(int index) {
		bookList.remove(index);
	}

	public List<Book> getBooks() {
		/*
		 * If we copy list as a unmodifiableList (or Collection) we can change
		 * the original list and in consequence it will affect also the
		 * unmodifiable reference. The unmodifiable reference (or copy) cannot
		 * be altered through set or add. Unmodifiable method prevents other
		 * classes to change the unmodifiable list.
		 */
		return Collections.unmodifiableList(bookList);
	}

	public int getListSize() {
		return bookList.size();
	}

	public Object getBook(int index) {
		return bookList.get(index);
	}

	// saving files
	public void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(bookList);

		oos.flush();
		oos.close();
	}

	public void openFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			@SuppressWarnings("unchecked")
			List<Book> books = (List<Book>) ois.readObject();
			bookList.clear();
			bookList.addAll(books);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
		ois.close();
	}

	public void newFile() {
		bookList.clear();
	}

	public void connect() throws Exception {
		// safe to connect multiple times
		if (con != null)
			return;
		// due to those 2 lines
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Class not found");
		}

		String url = "jdbc:mysql://localhost:3306/library?useSSL=false";
		con = DriverManager.getConnection(url, "root", "lucas7");

		// debuggin connection
		// System.out.println("Connected: " + con);
	}

	public void disconnect() {
		if (con != null) {
			try {
				con.close();
				if (con.isClosed())
					System.out.println("Connection closed");
			} catch (SQLException e) {
				System.out.println("Can't close the connection");
			}
		}
	}

	
	public void remove() throws SQLException {
		String removeSql = "DELETE from book where id=?";
		
		PreparedStatement  removeStatement = con.prepareStatement(removeSql);
	}
	public void save() throws SQLException {

		String checkSql = "SELECT count(*) as count from books where id=?";

		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		String insertSql = "insert into books (id, title, author, isbn, genre) values (?, ?, ?, ?, ?)";
		PreparedStatement insertStatement = con.prepareStatement(insertSql);

		String updateSql = "update books set title=?, author=?, isbn=?, genre=? where id=?";
		PreparedStatement updateStatement = con.prepareStatement(updateSql);

		for (Book book : bookList) {
			int id = book.getId();
			String title = book.getTitle();
			String author = book.getAuthor();
			String genre = book.getGenre();
			int isbn = book.getIsbn();

			checkStmt.setInt(1, id);

			ResultSet checkResult = checkStmt.executeQuery();

			checkResult.next();

			// checking if the book object is in the database
			// 0 means it is not in db
			int count = checkResult.getInt(1);

			//System.out.println("Count for book with ID: " + id + " is " + count);

			if (count == 0) {
				System.out.println("Inserting book with ID: " + id);

				int col = 1; // columns from insertSql
				// must be in the same order as we created insertSql String
				insertStatement.setInt(col++, id);
				insertStatement.setString(col++, title);
				insertStatement.setString(col++, author);
				insertStatement.setString(col++, String.valueOf(isbn));
				insertStatement.setString(col++, genre);
				insertStatement.executeUpdate();
			} else {

				int col = 1;
				updateStatement.setString(col++, title);
				updateStatement.setString(col++, author);
				updateStatement.setString(col++, String.valueOf(isbn));
				updateStatement.setString(col++, genre);
				updateStatement.setInt(col++, id);
				System.out.println("*** Updating book with values " + id + " : " + title + " : " + author + " : " + isbn + " : " + genre);

				updateStatement.executeUpdate();
				//System.out.println("Updating book with ID: " + id);
			}

		}
		updateStatement.close();
		insertStatement.close();
		checkStmt.close();
	}

	public void load() throws SQLException {
		bookList.clear();
		try {
			connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "select id, title, author, isbn, genre from books order by id";
		Statement selectStatement = con.createStatement();

		ResultSet results = selectStatement.executeQuery(sql);

		while (results.next()) {
			int id = results.getInt("id");
			String title = results.getString("title");
			String author = results.getString("author");
			String genre = results.getString("genre");
			int isbn = results.getInt("isbn");

			bookList.add(new Book(id, title, author, isbn, genre));

		}

		results.close();
		selectStatement.close();

	}

	public void refresh() {

	}

}
