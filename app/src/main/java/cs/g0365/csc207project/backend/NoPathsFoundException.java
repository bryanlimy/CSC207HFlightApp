package cs.g0365.csc207project.backend;

/**
 * Exception thrown when no paths are found using the FlightGraph.
 */
public class NoPathsFoundException extends Exception {

	/** Serial version ID */
	private static final long serialVersionUID = -6566161335233189919L;

	public NoPathsFoundException() {
    }

    public NoPathsFoundException(String message) {
        super(message);
    }
	
}