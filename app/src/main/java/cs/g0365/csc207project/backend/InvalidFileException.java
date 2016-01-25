package cs.g0365.csc207project.backend;

/** Exception thrown when information in a file is not formatted correctly. */
public class InvalidFileException extends Exception {

	/** Serial version ID */
	private static final long serialVersionUID = 3470943724240450698L;

	public InvalidFileException() {
		
	}
	
	public InvalidFileException(String message) {
        super(message);
    }
}
