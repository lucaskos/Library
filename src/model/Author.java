package model;

public class Author {
	private int authorID;
	private String firstName;
	private String lastName;
	private String pseudonym;

	public Author(String first_name, String last_name) {
		this.firstName = first_name;
		this.lastName = last_name;
	}

	public Author(String first_name, String last_name, String pseudonym) {
		this.firstName = first_name;
		this.lastName = last_name;
		this.pseudonym = pseudonym;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}

}
