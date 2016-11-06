package model;

import java.util.List;

import view.MainFrame;

public class TestDB {
	
	
	
	public static void main(String[] args) {
		Book book1 = new Book("pierwszy test", "test", 3, "test1");
		Book book2 = new Book("drugi test", "test", 3, "test");
		Book book3 = new Book("trzeci test", "test", 3, "test");
		Book book4 = new Book("czwarty test" , "test autora" , 4 , "test3");
		Database db = new Database();
		db.addBook(book1);
		db.addBook(book2);
		db.addBook(book3);
		db.addBook(book4);
		//System.out.println(db.getAllBooks());
		
		
		//db.getBook("test");
		//db.getBook("test");
		//System.out.println(db.getBookByGenre(1));
	}
}
