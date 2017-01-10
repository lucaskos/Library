package view.isbnCheck;

public class Isbn {
	private IsbnStrategy strategy;
	
	public Isbn(IsbnStrategy strategy) {
		this.strategy = strategy;
	}
	public boolean executeStrategy(String isbn) {
		return strategy.check(isbn);
	 }

}
