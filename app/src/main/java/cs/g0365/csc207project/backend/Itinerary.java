package cs.g0365.csc207project.backend;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A representation of an Itinerary.
 */
public class Itinerary implements Serializable {

	/** Serial version ID */
	private static final long serialVersionUID = 5291911098860911962L;

	/** Itinerary number of this Itinerary*/
    private String itineraryNumber;
    
    /** Collection of flights of this Itinerary*/
    private List<Flight> flights;
    
    /** Overall cost of this Itinerary*/
    private double overallCost;
    
    /** Overall travel time, in minutes*/
    private int overallTime;
    
    /**
     * Creates a new Itinerary object.
     * @param itineraryNumber the Itinerary Number
     * @param flights the list of Flight objects
     * @throws ParseException when the DateTime of a Flight object in the list 
     * does not follow the correct format
     */
    public Itinerary(String itineraryNumber, List<Flight> flights) 
    		throws ParseException {
        this.itineraryNumber = itineraryNumber;
        this.flights = flights;
        this.updateCostTime();
    }
    
    /**
     * Updates overallCost and overallTime using the current flights list.
     * @throws ParseException when DateTime of a Flight object in the list does 
     * not follow the correct format
     */
    private void updateCostTime() throws ParseException{
        this.overallCost = 0;
        for (Flight f:flights){
            this.overallCost += Double.parseDouble(f.getPrice());
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", 
        		Locale.US);
        
        long firstDep = Long.MAX_VALUE;
        long lastArr = 0;
        
        for (Flight f:flights){
            
            //find the first departure time and last arrival time
            long departure = 
            		formatter.parse(f.getDepartureDateTimeinString()).getTime();
            long arrival = 
            		formatter.parse(f.getArrivalDateTimeinString()).getTime();
            if (departure < firstDep){
                firstDep = departure;
            }
            if (arrival > lastArr){
                lastArr = arrival;
            }
        }
        
        //convert millisecond to minute
        this.overallTime = (int) (lastArr - firstDep) / 60000;
    }
    
    /**
     * Returns the itinerary number.
     * @return the itinerary number
     */
    public String getItineraryNumber() {
        return itineraryNumber;
    }
    
    /**
     * Returns the flight list.
     * @return the flight list
     */
    public List<Flight> getFlights() {
        return flights;
    }
    
    /**
     * Returns the overall cost.
     * @return the overall cost
     */
    public double getOverallCost() {
        return overallCost;
    }
    
    /**
     * Returns the overall cost.
     * @return the overall cost
     */
    public String getOverallCostInString() {
        DecimalFormat decimalformat = new DecimalFormat("0.00");
        return decimalformat.format(this.overallCost);
    }
    
    /**
     * Returns the overall time in number of minutes.
     * @return the overall time
     */
    public int getOverallTimeInMins() {
        return overallTime;
    }
    
    /**
     * Returns the overall time in a String of format HH:MM.
     * @return the overall time
     */
    public String getOverallTimeInStr(){
        return String.format(Locale.US, "%02d:%02d", overallTime / 60, 
        		overallTime % 60);
    }
    
    /**
     * Returns the list of flight numbers of the Flights in this Itinerary.
     * The list is in the format:
     * [Flight number 1, Flight number 2]
     * @return the list of flight numbers of the Flights in this Itinerary
     */
    public String getFlightString(){
    	
    	String flightList = "[";
    	
    	if (flights.isEmpty()) {
    		return "[]";
    	}
    	
        for (Flight flight:flights) {
        	
        	flightList += flight.getFlightNumber() + ",";
        	
        }

        flightList = flightList.substring(0, flightList.length() - 1);
		flightList += "]";
    	
        return flightList;
    }
    
	/**
	 * Returns the String representation of this Itinerary in the format:
	 * One line per flight in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 * @return the String representation of this Itinerary
	 */
	@Override
    public String toString(){
    	
        String output = "";
        
        for (Flight f : flights){
            output += f.getFlightNumber() 
            		+ "," + f.getDepartureDateTimeinString() 
            		+ "," + f.getArrivalDateTimeinString() 
            		+ "," + f.getAirline() + "," + f.getOrigin() + "," 
            		+ f.getDestination() + "\n";
        }
        
        output += this.getOverallCostInString() 
        		+ "\n" + this.getOverallTimeInStr();
        
        return output;
    }
    
	/**
	 * Decreases each seat number in each flight in this Itinerary by 1.
	 * @throws InvalidFlightInfoException if a flight has less than 1 seat
	 * available
	 */
    public void bookClient() throws InvalidFlightInfoException {
    	
    	for (Flight flight:flights) {
    		flight.bookFlight();
    	}
    	
    }
    
}
