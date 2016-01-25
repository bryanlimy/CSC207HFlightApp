package cs.g0365.csc207project.backend;

import java.text.SimpleDateFormat;
import java.util.Locale;

/** The constants used by the DataController. */
public class DataConstants {

	/** Client CSV file */
	public static final String CLIENTS_PATH = "assets/clients.csv";

	/** Flight CSV file */
	public static final String FLIGHTS_PATH = "assets/flights.csv";
	
	/** Serialized data files */
	public static final String CLIENTS_SERIALIZED = "assets/clients.ser";
	public static final String ADMINS_SERIALIZED = "assets/admins.ser";
	public static final String FLIGHTS_SERIALIZED = "assets/flights.ser";
	public static final String ITINERARYS_SERIALIZED 
	= "assets/itineraries.ser";

	/** Token character */
	public static final String TOKEN = ",";
	
	/** User field indices */
	public static final int USER_LAST_NAME = 0;
	public static final int USER_FIRST_NAME = 1;
	public static final int USER_EMAIL = 2;
	public static final int USER_ADDRESS = 3;
	public static final int USER_CREDIT_CARD = 4;
	public static final int USER_EXPIRE_DATE = 5;

	/** Flight field indices */
	public static final int FLIGHT_NUMBER = 0;
	public static final int FLIGHT_DEPARTURE_DATE = 1;
	public static final int FLIGHT_ARRIVAL_DATE = 2;
	public static final int FLIGHT_AIRLINE = 3;
	public static final int FLIGHT_ORIGIN = 4;
	public static final int FLIGHT_DESTINATION = 5;
	public static final int FLIGHT_PRICE = 6;
	public static final int FLIGHT_NUM_SEATS = 7;
    
    /** Max length of stopover between flights, in milliseconds */
    public static final int MAX_STOPOVER = 21600000;
    
    /** Max length of stopover between flights, in minutes */
    public static final int MAX_DELTA_MIN = 6*60;
    
    /** Format for dates */
    public static final String DATEPATTERN = "yyyy-MM-dd";
    public static final String TIMEPATTERN = "yyyy-MM-dd HH:mm";
    public static final SimpleDateFormat DATEPARSE = 
    		new SimpleDateFormat(DATEPATTERN, Locale.US);
	
}