package cs.g0365.csc207project.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An directed graph of locations.
 * Each location will be connected to its flights with an edge.
 */
public class FlightGraph {

    /** A mapping of location names to departing flights. */
	private Map<String, HashSet<Flight>> graph;

    /**
     * Creates a new empty Graph.
     */
    public FlightGraph() {
    	graph = new HashMap<String, HashSet<Flight>>();
	}

    /**
     * Returns the set of flights departing from the given location.
     * @return the set of flights departing from the given location
     */
    public Set<Flight> getFlights(String location) {
    	return graph.get(location);
    }
    
    /**
     * Adds a new location to this Graph. 
     * @param location the name of the location
     */
    public void addNode(String location) {
    	
    	// add the location if it doesn't already exist in the Graph
    	if (!graph.containsKey(location)) {
        	graph.put(location, new HashSet<Flight>());
    	}
    	
    }

    /**
     * Adds an edge from the origin to the flight.
     * @param flight the flight object to add to the origin
     */
    public void addEdge(Flight flight) {
    	
    	// if the flight is not connected to the location, add it
    	if (flight != null 
    			&& !graph.get(flight.getOrigin()).contains(flight) 
    			&& !containsFlight(graph.get(flight.getOrigin()), flight)) {
    		graph.get(flight.getOrigin()).add(flight);
    	}
    	
    }

    /**
     * Returns true if the set of flights leaving the origin already contains
     * the new flight.
     * @param originFlights the set of flights leaving the origin
     * @param newFlight the new flight
     * @return true if the set of flights leaving the origin already contains
     * the new flight
     */
    public boolean containsFlight(HashSet<Flight> originFlights, 
    		Flight newFlight) {
    	
		for (Flight flight : originFlights) {
			if (flight.getFlightNumber().equals(newFlight.getFlightNumber())) {
				return true;
			}
		}
		
		return false;
    }
    
    /**
     * Searches and returns a list of (list of flights) from origin to 
     * destination departing on the date.
     * @param origin the origin location
     * @param destination the destination location
     * @param date the departure date of the flight
     * @return a list of (list of flights) from origin to destination departing 
     * on the date.
     * @throws NoPathsFoundException if no paths are found
     */
    public List<List<Flight>> searchPath(String origin, String destination, 
    		String date) throws NoPathsFoundException {
    	
    	// check if the departure city is already the destination city
    	if (!origin.equalsIgnoreCase(destination) 
    			&& graph.containsKey(origin) 
    			&& graph.containsKey(destination)) 
    	{
    		// keep track of what cities were visited so far
	    	HashMap<String, Boolean> visitedCities;
	 
	    	// all flights departing from origin
	    	Set<Flight> currentSet = graph.get(origin);
	    	
	    	// store paths that are going to be constructed
	    	List<List<Flight>> allPaths = new ArrayList<List<Flight>>();
	    	
	    	// for each flight leaving this city
	    	for (Flight flight : currentSet) {

	    		// creates a list of ordered paths to go along this flight
	    		List<Flight> currentPath = new ArrayList<Flight>();
	    		
	    		// checks if this flight is departing at the date selected by 
	    		// the user
	    		if (flight.getDepartureDateinString().equals(date)) {
	    			
	    			// creates a hashmap to keep track of what cities where 
	    			// visited along the path o this specific flight
	    			// PS: Having only one hashmap for all flights
	    			// would avoid different flights visiting the same city
	    			visitedCities = new HashMap<String, Boolean>();

	    			
	    			// mark this city as visited in order to avoid infinite 
	    			// recursion
	    			visitedCities.put(flight.getOrigin(), 
	    					Boolean.valueOf(true));
	    			
	    			// call a recursive methods which are going to perform a DFS 
	    			searchPath(flight, currentPath, allPaths, visitedCities, 
	    					destination);
	    		}
	    	}
	    	
	    	if (allPaths.isEmpty()) {
	        	throw new NoPathsFoundException("No flight paths found.");
	    	}
	    	
	    	return allPaths;    	
    	}
    	throw new NoPathsFoundException("No flight paths found.");
    }
    
    /**
     * Searches for all possible paths the current flight can lead to. This 
     * method is a recursive method.
     * @param currentFlight the current flight 
     * @param currentPath all the flights taken since the beginning
     * @param allPaths all the paths (list of flights) that reached the
     * destination so far
     * @param visitedCities all the visited cities along the path of this 
     * flight
     * @param destination the destination city for the itinerary
     */
    @SuppressWarnings("unchecked")
	private void searchPath(Flight currentFlight, List<Flight> currentPath, 
		List<List<Flight>> allPaths,
		HashMap<String, Boolean> visitedCities, String destination) 
    {
    	// add the current flight to its own path
    	// it is the same as saying the this flight was choose
    	currentPath.add(currentFlight);
		
    	// one base case recursion: if this flight destination is
    	// equal to the itinerary destination
    	if (currentFlight.getDestination().equals(destination)) 
    	{
    		// adds the path constructed until this flight to the list
    		// of all reachable paths
    		allPaths.add(currentPath);
    	} else 
    	{
    		// general case
    		
    		// gets all flights leaving at the destination
    		// of the current flight
    		Set<Flight> currentSet = graph.get(currentFlight.getDestination());
    		
    		// for each flight departing at this destination
    		for (Flight flight : currentSet) {
    			
    			// measure the time difference between this flight and its 
    			// predecessor
    			long mileLastFlight = 
    					currentFlight.getArrivalDateTimeinDate().getTime();
        		long mileCurrentFlight = 
        				flight.getDepartureDateTimeinDate().getTime();
        		
        		long deltaMinutes = 
        				(mileCurrentFlight - mileLastFlight)/1000/60;
        				//TimeUnit.MILLISECONDS.toMinutes(mileCurrentFlight 
        				//		- mileLastFlight);
        		
        		int maxHoursInMinutes = DataConstants.MAX_DELTA_MIN;
    			
        		// check weather this flight is going to a city that was already 
        		// visited and is no more than 6 hours after its predecessor
    			if ((!visitedCities.containsKey(flight.getDestination())
    					|| visitedCities.get(flight.getDestination())
    					.equals(false))
    					&& deltaMinutes > 0 
    					&& deltaMinutes <= maxHoursInMinutes) {

    				//copy the path of it's predecessor
    				List<Flight> newPath = this.cloneFlights(currentPath);
    				
    				//copy the visited cities of its predecessor 
    				HashMap<String, Boolean> newVisited = 
    						(HashMap<String, Boolean>) visitedCities.clone();
    				
    				//mark the current city as visited
    				visitedCities.put(flight.getOrigin(), 
    						Boolean.valueOf(true));
    				
    				//search again in depth for a new possibility
    				searchPath(flight, newPath, allPaths, newVisited, 
    						destination);
    			}
				
			}
			
		}
	}
    
    /**
     * Creates and returns a copy of the list of Flights. 
     * @param flights the list of Flights to clone
     * @return a copy of the list of Flights
     */
    private List<Flight> cloneFlights(List<Flight> flights) {
    	List<Flight> newFlights = new ArrayList<Flight>();
    	
    	for(Flight flight : flights){
    		newFlights.add(flight);
    	}
    	
    	return newFlights;	
    }  
    
}