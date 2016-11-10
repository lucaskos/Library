package model;

import java.io.Serializable;

public class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static int count = 1;
	
	private int id;
	private String title, author, genre;
	private int isbn; 

	public Book(String title, String author, int isbn, String genre) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.isbn = isbn;
		
		this.id = count;
		count++;
	}

	public Book(int id, String title, String author, int isbn, String genre) {
		this(title, author, isbn, genre);
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	
	public String toString() {
		return "ID: " + id + " Author: " + author + " Title: " + title + " Genre: " + genre + "\n";
		
	}
}
