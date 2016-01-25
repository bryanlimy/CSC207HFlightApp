package cs.g0365.csc207project.backend;

/**
 * A representation of an Administrator.
 */
public class Administrator extends User {

	/** Serial version ID */
	private static final long serialVersionUID = 1791870419786040190L;

	/**
	 * Creates a new administrator.
	 * @param firstName First name of this administrator
	 * @param lastName Last name of this administrator 
	 * @param email Email of this administrator
	 */
	public Administrator(String firstName, String lastName, String email) {
		super(firstName, lastName, email);
	}
	
}
