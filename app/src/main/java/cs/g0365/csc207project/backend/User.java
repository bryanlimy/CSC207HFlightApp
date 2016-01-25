package cs.g0365.csc207project.backend;

import java.io.Serializable;

/**
 * A representation of a User.
 */
public abstract class User implements Serializable {

	/** Serial version ID */
	private static final long serialVersionUID = -4744771617777185890L;
	
	/*PERSONAL INFORMATION*/
	
	/** First name of this user */
	private String firstName;
	
	/** Last name of this user */
	private String lastName;
	
	/** Email of this user */
	private String email;

	/**
	 * Creates a new User.
	 * @param firstName First name of this user
	 * @param lastName Last name of this user 
	 * @param email Email of this user
	 */
	public User(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	/**
	 * Returns the first name of this user.
	 * @return the first name of this user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of this user.
	 * @param firstName the first name of this user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of this user.
	 * @return the last name of this user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name of this user.
	 * @param lastName the last name of this user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the email of this user.
	 * @return the email of this user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of this user.
	 * @param email the email of this user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the String representation of this user in the format:
	 * LastName,FirstNames,Email
	 * @return the String representation of this user
	 */
	@Override
	public String toString() {
		return lastName + "," + firstName + "," + email;
	}
	
}