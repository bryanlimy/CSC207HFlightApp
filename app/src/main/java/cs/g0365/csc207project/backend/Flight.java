package cs.g0365.csc207project.backend;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

/**
 * A representation of a Flight.
 */
public class Flight implements Serializable {
	
	/** Serial version ID */
	private static final long serialVersionUID = 6808601612740715944L;

	/** SimpleDateFormat instance which formats and parses Date*/
    private SimpleDateFormat format;
    
    /** flight number of this flight*/
    private String flightNumber;
    
    /** departure date and time of this flight */
    private Date departureDateTime;
    
    /** arrival date and time of this flight */
    private Date arrivalDateTime;
    
    /** airline of this flight */
    private String airline;
    
    /** origin of this flight */
    private String origin;
    
    /** destination of this flight */
    private String destination;
    
    /** cost of this flight */
    private double price;
    
    /** number of seats available of this flight */
    private int numSeats;
    
    /**
     * Creates a new Flight.
     * @param number flight number of this flight
     * @param departureDateTime departure date and time of this flight
     * @param arrivalDateTime arrival date and time of this flight
     * @param airline airline of this flight
     * @param origin origin of this flight
     * @param destination destination of this flight
     * @param price cost of this flight
     * @throws ParseException thrown when the price is not valid
     * @throws InvalidFlightInfoException when the information contains an 
     * error
     */
    public Flight(String number, String departureDateTime,
                  String arrivalDateTime, String airline, String origin,
                  String destination, String price, String numSeats) throws
                  InvalidFlightInfoException, ParseException {
        this.flightNumber = number;
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        setNumSeats(numSeats);
        if (origin.equals(destination)) {
        	throw new InvalidFlightInfoException("Origin cannot be the same "
        			+ "as destination.");
        }
        setPrice(price);
        setDepartureDateTime(departureDateTime);
        setArrivalDateTime(arrivalDateTime);
    }
    
    /**
     * Returns this flight's flight number.
     * @return flight number of this flight
     */
    public String getFlightNumber() {
        return flightNumber;
    }
    
    /**
     * Sets the flight number of this flight
     * @param flightNumber flight number of this flight
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    /**
     * Returns the departure date and time of this flight in Date.
     * @return the departure date and time of this flight in Date
     */
    public Date getDepartureDateTimeinDate() {
        return this.departureDateTime;
    }
    
    /**
     * Returns the departure date and time of this flight in YYYY-MM-DD HH:MM
     * format in a String.
     * @return the departure date and time of this flight
     */
    public String getDepartureDateTimeinString() {
        return this.format.format(this.departureDateTime);
    }
    
    /**
     * Returns the departure date of this flight in YYYY-MM-DD format in a
     * String.
     * @return the departure date of this flight
     */
    public String getDepartureDateinString() {
        return getDepartureDateTimeinString().substring(0, 10);
    }
    
    /**
     * Returns the departure time of this flight in HH:MM format in a String.
     * @return the departure time of this flight
     */
    public String getDepartureTimeinString() {
        return getDepartureDateTimeinString().substring(11, 16);
    }
    
    /**
     * Sets the departure date and time of this flight.
     * departureDateTime formatted in YYYY-MM-DD HH:MM.
     * @param departureDateTime the departure date and time of this flight
     * @throws InvalidFlightInfoException when this info of this flight is 
     * invalid
     */
    public void setDepartureDateTime(String departureDateTime)
    throws InvalidFlightInfoException {
        try {
        	if (this.arrivalDateTime != null) {
        		if (checkInvalidDate(this.arrivalDateTime, 
        				format.parse(departureDateTime))) {
            		throw new InvalidFlightInfoException("Departure date and "
            				+ "time cannot be after arrival date and time ");
            	}
        	}
            this.departureDateTime = format.parse(departureDateTime);
        } catch (ParseException e) {
            throw new InvalidFlightInfoException("Invalid Date.");
        }
        
    }
    
    /**
     * Returns the arrival date and time of this flight in Date.
     * @return the arrival date and time of this flight in Date
     */
    public Date getArrivalDateTimeinDate() {
        return this.arrivalDateTime;
    }
    
    /**
     * Returns the arrival date and time of this flight in YYYY-MM-DD HH:MM
     * format in a String.
     * @return the arrival date and time of this flight
     */
    public String getArrivalDateTimeinString() {
        return this.format.format(this.arrivalDateTime);
    }
    
    /**
     * Returns the arrival date of this flight in YYYY-MM-DD format in a 
     * String.
     * @return the arrival date of this flight
     */
    public String getArrivalDateinString() {
        return getArrivalDateTimeinString().substring(0, 10);
    }
    
    /**
     * Returns the arrival time of this flight in HH:MM format in a String.
     * @return the arrival time of this flight
     */
    public String getArrivalTimeinString() {
        return getArrivalDateTimeinString().substring(11, 16);
    }
    
    /**
     * Sets the arrival date and time of this flight.
     * arrivalDateTime formatted in YYYY-MM-DD HH:MM.
     * @param arrivalDateTime the arrival date and time of this flight
     * @throws InvalidFlightInfoException when info of this flight is invalid
     */
    public void setArrivalDateTime(String arrivalDateTime)
    throws InvalidFlightInfoException {
        try {
        	if (this.departureDateTime != null) {
        		if (checkInvalidDate(format.parse(arrivalDateTime), 
        				this.departureDateTime)) {
            		throw new InvalidFlightInfoException("Arrival date and "
            				+ "time cannot be after before date and time ");
            	}
        	}
            this.arrivalDateTime = format.parse(arrivalDateTime);
        } catch (ParseException e) {
            throw new InvalidFlightInfoException("Invalid Date");
        }
        
    }
    
    /**
     * Returns the airline of this flight.
     * @return the airline of this flight
     */
    public String getAirline() {
        return airline;
    }
    
    /**
     * Sets the airline of this flight.
     * @param airline the airline of this flight.
     */
    public void setAirline(String airline) {
        this.airline = airline;
    }
    
    /**
     * Returns the origin of this flight.
     * @return the origin of this flight
     */
    public String getOrigin() {
        return origin;
    }
    
    /**
     * Sets the origin of this flight.
     * @param origin the origin of this flight
     * @throws InvalidFlightInfoException thrown when this origin is the same 
     * 			as the destination
     */
    public void setOrigin(String origin) throws InvalidFlightInfoException {
    	if (this.destination.equals(origin)) {
    		throw new InvalidFlightInfoException("Origin cannot be the same "
    				+ "as destination.");
    	} else {
    		this.origin = origin;
    	}
    }
    
    /**
     * Returns the destination of this flight.
     * @return the destination of this flight
     */
    public String getDestination() {
        return destination;
    }
    
    /**
     * Sets the destination of this flight.
     * @param destination the destination of this flight
     * @throws InvalidFlightInfoException 
     */
    public void setDestination(String destination) 
    		throws InvalidFlightInfoException {
    	if (this.destination.equals(origin)) {
    		throw new InvalidFlightInfoException("Destination cannot be the "
    				+ "same as origin.");
    	} else {
    		this.destination = destination;
    	} 
    }
    
    /**
     * Returns the price of this flight in 2 decimals.
     * If price has more than 2 significant digits, it will be rounded up.
     * @return the price of this flight
     */
    public String getPrice() {
        DecimalFormat decimalformat = new DecimalFormat("0.00");
        return decimalformat.format(this.price);
    }
    
    /**
     * Sets the price of this flight.
     * @param price the price of this flight
     * @throws InvalidFlightInfoException thrown if this given price is 
     * 			invalid
     */
    public void setPrice(String price) throws InvalidFlightInfoException {
    	try {
    		this.price = Double.parseDouble(price);
    	} catch (NumberFormatException e) {
    		throw new InvalidFlightInfoException("Invalid price");
    	}
    }
    
    /**
     * Sets the number of seats of this flight.
     * @param seats Seat the number of seats
     * @throws InvalidFlightInfoException thrown if the number of seats is less
     * 			than zero
     */
    public void setNumSeats(String seats) throws InvalidFlightInfoException {
    	try {
    		int availableSeats = Integer.parseInt(seats);
    		if (availableSeats >= 0) {
    			this.numSeats = availableSeats;
    		} else {
    			throw new InvalidFlightInfoException("Number of seats cannot " 
    		+ "be less than zero.");
    		}
    	} catch (NumberFormatException e) {
    		throw new InvalidFlightInfoException("Invalid seats.");
    	}
    }
    
    /**
     * Returns the number of seats available on this flight.
     * @return the number of seats available on this flight
     */
    public String getNumSeats() {
    	return Integer.toString(this.numSeats);
    }
    
    /**
     * Decreases the available seats of this flight by one when someone books
     * this flight.
     * @throws InvalidFlightInfoException when trying to book a full flight
     */
    public void bookFlight() throws InvalidFlightInfoException {
    	if (this.numSeats > 0) {
    		this.numSeats -= 1;
    	} else {
    		throw new InvalidFlightInfoException("This flight is full.");
    	}
    }
    
    /**
     * Returns the String representation of this Flight in the format:
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,
     * Price
     * @return the String representation of this Flight
     */
    @Override
    public String toString() {
        return this.flightNumber + "," + getDepartureDateTimeinString() + ","
        + getArrivalDateTimeinString() + "," + this.airline + "," + this.origin
        + "," + this.destination + "," + this.getPrice();
    }
    
    /**
     * Returns true if the given arrival date and time is before the given 
     * departure date and time.
     * @param arrivalDate arrival date and time
     * @param departureDate given arrival date and time
     * @return true if the given arrival date and time is before the given 
     * 			departure date and time, false otherwise.
     */
    private boolean checkInvalidDate(Date arrivalDate, Date departureDate) {
    	return !departureDate.before(arrivalDate);
    }
    
    /**
     * Returns the travel time of this flight in a String of format HH:MM.
     * @return the travel time of this flight
     */
    public String getTravelTime() {
    	
    	long timeDiff = arrivalDateTime.getTime() 
    			- departureDateTime.getTime();
    	int travelTimeMins = (int) (timeDiff) / 60000;

        return String.format(Locale.US, "%02d:%02d", travelTimeMins / 60, 
        		travelTimeMins % 60);
    	
    }
    
}