package isbnCheck;

public class OldIsbn implements IsbnStrategy {
	private int sum, d, stringLength;

	public boolean check(String isbnString) {
		stringLength = isbnString.length();
		for (int i = 0; i < stringLength; i++) {
			d = Integer.parseInt(isbnString.substring(i, i + 1));
			sum += ((10 - i) * d);
		}
		if ((sum % 11) != 0) {
			return false;
		} else {
			return true;
		}
	}

}
