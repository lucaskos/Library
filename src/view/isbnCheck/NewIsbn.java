package view.isbnCheck;

public class NewIsbn implements IsbnStrategy {
	private int sum, checksum;

	@Override
	public boolean check(String isbn) {
		sum = 0;
		for (int i = 0; i < isbn.length() - 1; i++) {
			int even = 0, odd = 0;
			if ((i == 0) || (i % 2 == 0)) {
				even = Integer.parseInt(isbn.substring(i, i + 1));
				sum += even;
			} else {
				odd = Integer.parseInt(isbn.substring(i, i + 1)) * 3;
				sum += odd;
			}
		}
		checksum = 10 - (sum % 10);
		if (checksum == 10) {
			checksum = 0;
		}
		if (checksum == Integer.parseInt(isbn.substring(12))) {
			System.out.println(checksum);
			return true;
		} else {
			return false;
		}

	}

}
