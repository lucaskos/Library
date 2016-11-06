import model.Book;
import model.Database;

public class TestDB {
	public static void main(String[] args) throws Exception {
		System.out.println("Running db");
		Database db = new Database();
		
		
		try {
			db.connect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		db.addBook(new Book("Harry Potter" , "J.K.Rowing", 21342, "fiction"));
		db.addBook(new Book("Brave New World" , "A. Huxley", 11111, "nonfiction"));
		
		try {
			db.save();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		db.load();
		
		db.disconnect();
	}
}
