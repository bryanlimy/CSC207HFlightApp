package cs.g0365.csc207project.backend;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A class which authenticates the login of users.
 * This class
 * 1. reads login information from a file
 * 2. compares login information and returns the correct user 
 */
public class LoginAuthenticator {

    /** A mapping of emails to passwords. */
	private Map<String, String> pwd;

    /** The default password for newly uploaded users. */
	private static final String DEFAULT_PWD = "password";
	
	/**
	 * Reads the information from the password file and populates the password
	 * map.
	 * @param inpStream the path to the password file
	 * @throws InvalidFileException when there is an error in the formatting
	 */
	public LoginAuthenticator(InputStream inpStream) 
			throws InvalidFileException {
		super();
		pwd = new HashMap<String, String>();
		Scanner fileReader;
		fileReader = new Scanner(inpStream);
		
		while(fileReader.hasNextLine()) {
			
			String current = fileReader.nextLine();
			String[] data = current.split(DataConstants.TOKEN);
			
			if (data.length != 2) {
				throw new InvalidFileException("Error in data format.");
			} else {
				pwd.put(data[0], data[1]);
			}
		}
	}
	
	/**
	 * Returns true if the user's email matches their corresponding password,
	 * returns false otherwise.
	 * @param email the user's email
	 * @param pass the user's password
	 * @return true if the user's email matches their corresponding password,
	 * returns false otherwise.
	 */
	public boolean auth(String email, String pass){
		User usr = DataController.getInstance().getUser(email);
		if(usr != null){
			if(pwd.containsKey(email)){
				if(pwd.get(email).equals(pass)){
					DataController.getInstance().setCurrentUser(usr);
					return true;
				}
			}
			else{
				if(pass.equals(DEFAULT_PWD)){
					DataController.getInstance().setCurrentUser(usr);
					return true;
				}
			}
		}
		return false;
	}
	
}
