package cs.g0365.csc207project.backend;

/** Exception thrown when flight information file is not formatted correctly. */
public class InvalidFlightInfoException extends Exception {
	
	/** Serial version ID */
	private static final long serialVersionUID = 8608112670147370206L;

	public InvalidFlightInfoException() {
		
	}
	
	public InvalidFlightInfoException(String message) {
        super(message);
    }

}
