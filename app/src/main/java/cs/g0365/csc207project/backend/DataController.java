package cs.g0365.csc207project.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * A Data Controller used to manage the data in the program.
 */
public class DataController {

    /** A mapping of Client emails to Clients. */
    private static Map<String, Client> clients;

    /** A mapping of Admin emails to Administrators. */
    private static Map<String, Administrator> admins;
    
    /** A mapping of Flight numbers to Flights. */
    private static Map<String, Flight> flights;
    
    /** A mapping of Itinerary numbers to Itineraries. */
    private static Map<String, Itinerary> itineraries;

    /** The Flight Graph. */
	private static FlightGraph flightGraph = new FlightGraph();
	
	/** The current user of the application. */
	private static User currentUser;
	
	/** The single DataController instance */
    private static DataController instance = null;
    
    /**
     * Creates a new empty DataController.
     * A private Constructor prevents any other class from instantiating.
     */
    private DataController() {
    	clients = new HashMap<String, Client>();
    	admins = new HashMap<String, Administrator>();
    	flights = new HashMap<String, Flight>();
    	itineraries = new HashMap<String, Itinerary>();
    	currentUser = null;
    }
    
    /** Static getInstance method **/
    public static DataController getInstance() {
    	
        if (instance == null) {
            instance = new DataController();
        }
        
        return instance;
    }
    
    /**
     * Clears any information in the clients map, flights map, flight graph, 
     * and itineraries.
     */
    public void clearInfo() {
    	clients = new HashMap<String, Client>();
    	flights = new HashMap<String, Flight>();
    	flightGraph = new FlightGraph();
    	itineraries = new HashMap<String, Itinerary>();
    }
    
	/**
	 * Adds the administrator to the admins map.
	 * @param admin the admin to add to the admins map
	 */
	public void addAdmin(Administrator admin){
		
		// check that the email is not already registered to a client or admin
		if(!admins.containsKey(admin.getEmail()) 
				&& !clients.containsKey(admin.getEmail())) {
			admins.put(admin.getEmail(), admin);
		} else {
	        System.out.println("The email " + admin.getEmail() 
	        		+ " is already in the system.");
		}
	}

    /**
     * Uploads admin information to the application from the file at the
     * given path. Populates the admins map.
     * @param path the path to an input csv file of admin information with
     * lines in the format: 
     * LastName,FirstNames,Email
     * @throws InvalidFileException when the given file is in invalid format
	 * @throws FileNotFoundException when the file is not found
     */
	public void uploadAdminInfo(String path) 
			throws InvalidFileException, FileNotFoundException {
		
		Scanner fileReader;
		fileReader = new Scanner(new FileInputStream(path));
		
		while(fileReader.hasNextLine()) {
			
			String current = fileReader.nextLine();
			String[] data = current.split(DataConstants.TOKEN);
			
			if (data.length != 3) {
				throw new InvalidFileException("Error in data format.");
			} else {
				Administrator admin 
				= new Administrator(data[DataConstants.USER_FIRST_NAME],
						data[DataConstants.USER_LAST_NAME],
						data[DataConstants.USER_EMAIL]);
				addAdmin(admin);
			}
			
		}
		
		fileReader.close();
	}
    
    /**
     * Returns the admins map.
     * @return the admins map
     */
	public Map<String, Administrator> getAdmins() {
		return admins;
	}
    
	/**
	 * Adds the client to the clients map.
	 * @param client the client to add to the clients map
	 */
	public void addClient(Client client){
		
		// check that the email is not already registered to a client or admin
		if(!clients.containsKey(client.getEmail()) && 
				!admins.containsKey(client.getEmail())){
			clients.put(client.getEmail(), client);
		} else {
	        System.out.println("The email " + client.getEmail() 
	        		+ " is already in the system.");
		}
	}

    /**
     * Uploads client information to the application from the file at the
     * given path. Populates the clients map.
     * @param path the path to an input csv file of client information with
     * lines in the format: 
     * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
     *  (the ExpiryDate is stored in the format YYYY-MM-DD)
     * @throws InvalidFileException thrown when the given file is in invalid 
     * format
	 * @throws ParseException thrown if the client info has incorrect format
	 * @throws FileNotFoundException if the file is not found
     */
	public void uploadClientInfo(String path) throws 
	InvalidFileException, ParseException, FileNotFoundException {
		
		Scanner fileReader;
		
		fileReader = new Scanner(new FileInputStream(path));
		
		while(fileReader.hasNextLine()) {
			
			String current = fileReader.nextLine();
			String[] data = current.split(DataConstants.TOKEN);
			
			if (data.length != 6) {
				throw new InvalidFileException("Error in data format.");
			} else {
				Client client = new Client(data[DataConstants.USER_FIRST_NAME],
						data[DataConstants.USER_LAST_NAME],
						data[DataConstants.USER_EMAIL],
						data[DataConstants.USER_ADDRESS],
						data[DataConstants.USER_CREDIT_CARD],
						data[DataConstants.USER_EXPIRE_DATE]);
				addClient(client);
			}
			
		}
		
		fileReader.close();
	}
    
    /**
     * Returns the clients map.
     * @return the clients map
     */
	public Map<String, Client> getClients() {
		return clients;
	}
	
	/**
	 * Adds the flight to the flights map.
	 * @param flight the flight to add to the flights map
	 */
	public void addFlight(Flight flight) {
		
		if(!flights.containsKey(flight.getFlightNumber())){
			flights.put(flight.getFlightNumber(), flight);
			
			// add the origin and destination as locations to the Flight Graph
			flightGraph.addNode(flight.getOrigin());
			flightGraph.addNode(flight.getDestination());
			
			// connect the origin to the flight with an edge
			flightGraph.addEdge(flight);
			
		} else {
	        System.out.println("Flight number " + flight.getFlightNumber() 
	        		+ " is already in the system.");
		}
	}
	
    /**
     * Uploads flight information to the application from the file at the
     * given path. Populates the flights map.
     * @param path the path to an input csv file of flight information with 
     * lines in the format: 
     * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,
     * Price,NumSeats
     * (the dates are in the format YYYY-MM-DD HH:MM; the price has exactly two
     * decimal places; the number of seats is a non-negative integer)
     * @throws InvalidFileException when flight information is in the
	 * incorrect format
	 * @throws InvalidFlightInfoException when flight information is in the
	 * incorrect format
	 * @throws FileNotFoundException when the file is not found
     */
	public void uploadFlightInfo(String path) throws 
	InvalidFileException, InvalidFlightInfoException, FileNotFoundException{
		
		Scanner fileReader;
		
		fileReader = new Scanner(new FileInputStream(path));
			
		while(fileReader.hasNextLine()) {
			String current = fileReader.nextLine();
			String[] data = current.split(DataConstants.TOKEN);

			if (data.length != 8) {
				throw new InvalidFileException("Error in data format.");
			} else {
				try {
					Flight flight 
					= new Flight(data[DataConstants.FLIGHT_NUMBER], 
							data[DataConstants.FLIGHT_DEPARTURE_DATE],
							data[DataConstants.FLIGHT_ARRIVAL_DATE],
							data[DataConstants.FLIGHT_AIRLINE], 
							data[DataConstants.FLIGHT_ORIGIN], 
							data[DataConstants.FLIGHT_DESTINATION],
							data[DataConstants.FLIGHT_PRICE], 
							data[DataConstants.FLIGHT_NUM_SEATS]);
					addFlight(flight);
				} catch (ParseException e) {
					throw new 
					InvalidFlightInfoException("Error in flight info.");
				}
			}
			
		}
		
		fileReader.close();
	}
	
	/**
	 * Returns the flights map.
	 * @return the flights map
	 */
	public Map<String, Flight> getFlights() {
		return flights;
	}
	
	/**
	 * Returns the Flight object with the flight number.
	 * @param flightNumber the Flight's flight number
	 * @return the Flight object with the flight number
	 */
	public Flight getFlight(String flightNumber) {
    	
	    return flights.get(flightNumber);
    	
	}
	
	/**
	 * Returns the User object with the given email.
	 * @param email the User's email
	 * @return the User object with the given email
	 */
	public User getUser(String email) {
    	
		if (clients.containsKey(email)) {
	    	return clients.get(email);
		} else if (admins.containsKey(email)) {
	    	return admins.get(email);
		} else {
			return null;
		}
    	
	}
	
	/**
	 * Returns the information stored for the client with the given email. 
	 * @param email the email address of a client
	 * @return the information stored for the client with the given email
	 * in this format:
	 * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
	 * (the ExpiryDate is stored in the format YYYY-MM-DD)
	 */
    public String getClientString(String email) {
    	
    	Client client = clients.get(email);
    	
    	if (client != null) {
    	    return client.toString();
    	} else {
    	    return "";
    	}
    	
    }
    
	/**
	 * Returns the information stored for the client with the given email. 
	 * @param email the email address of a client
	 * @return the information stored for the client with the given email
	 */
    public Client getClient(String email) {
    	
    	return clients.get(email);
    	
    }

	/**
	 * Returns the itineraries map.
	 * @return the itineraries map
	 */
	public Map<String, Itinerary> getItineraries() {
		return itineraries;
	}
    
	/**
	 * Checks if the newItinerary already exists in the Itineraries map and
	 * returns the existing Itinerary object, returns null otherwise.
	 * @param newItinerary the new itinerary to check if it already exists
	 * @return the existing identical itinerary object or null otherwise
	 */
	public Itinerary checkExistingItinerary(Itinerary newItinerary) {
		
		for (Map.Entry<String, Itinerary> entry : itineraries.entrySet()) {
			
			Itinerary existingItinerary = entry.getValue();
			
			// if the itinerary already exists, return the old itinerary
			if (existingItinerary.getFlightString()
					.equals(newItinerary.getFlightString())) {
				return existingItinerary;
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Adds the itinerary to the itineraries map.
	 * @param itinerary the itinerary to add to the itineraries map
	 */
	public void addItinerary(Itinerary itinerary) {
		if(!itineraries.containsKey(itinerary.getItineraryNumber())){
			itineraries.put(itinerary.getItineraryNumber(), itinerary);
		} else {
	        System.out.println("The itinerary number " 
			+ itinerary.getItineraryNumber() + " is already in the system.");
		}
	}
	
	/**
	 * Returns a list of all itineraries that depart from origin and arrive at
	 * destination on the given date. If an itinerary contains two consecutive
	 * flights F1 and F2, then the destination of F1 should match the origin of
	 * F2. To simplify our task, if there are more than 6 hours between the
	 * arrival of F1 and the departure of F2, then we do not consider this
	 * sequence for a possible itinerary (we judge that the stopover is too
	 * long).
     * Every flight in an itinerary must have at least one seat
     * available for sale. That is, the itinerary must be bookable.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return list of itineraries that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 * @throws NoPathsFoundException if no paths are found
	 * @throws ParseException if there is an error in the date format
	 */
	public List<Itinerary> getItineraries(String date, String origin, 
			String destination) throws NoPathsFoundException, ParseException {
		
		// Itinerary list
		List<Itinerary> itineraryList = new ArrayList<Itinerary>();
		
		// Check if it is a valid date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		format.parse(date);
		
		// Get all the possible flight paths from origin to destination
		List<List<Flight>> flightPaths = flightGraph.searchPath(origin, 
				destination, date);
    	
		for (List<Flight> flightPath : flightPaths) {
			
			// Check if the itinerary is valid and bookable
			if (isValidItinerary(date, flightPath) && isBookable(flightPath)) {
			
				// Generate an itinerary number
				int itineraryNumber = itineraries.size();
				String itineraryID = "I" + itineraryNumber;
				
				// Keep adding to itinerary number if itinerary number 
				// already exists
				while (itineraries.containsKey(itineraryID)) {
					itineraryNumber++;
					itineraryID = "I" + itineraryNumber;
				}
				
				// Create the Itinerary and add it to the itineraries map and
				// list of itineraries to return
				Itinerary itinerary;
				try {
					itinerary = new Itinerary(itineraryID, flightPath);
					
					// if the itinerary already exists, do not add it to the
					// itineraries map
					Itinerary existingItinerary = 
							checkExistingItinerary(itinerary);
					if (checkExistingItinerary(itinerary) != null) {
						itineraryList.add(existingItinerary);
					} else {
						addItinerary(itinerary);
						itineraryList.add(itinerary);
					}
				} catch (ParseException e) {
					System.out.println("Error in flight date format.");
				}
				
			}
				
		}

		return itineraryList;
		
	}	
	
	/**
	 * Returns all itineraries that depart from origin and arrive at
	 * destination on the given date. If an itinerary contains two consecutive
	 * flights F1 and F2, then the destination of F1 should match the origin of
	 * F2. To simplify our task, if there are more than 6 hours between the
	 * arrival of F1 and the departure of F2, then we do not consider this
	 * sequence for a possible itinerary (we judge that the stopover is too
	 * long).
     * Every flight in an itinerary must have at least one seat
     * available for sale. That is, the itinerary must be bookable.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 */
	public String getItinerariesUnsorted(String date, String origin, 
			String destination) {
		
		// Itinerary list
		List<Itinerary> itineraryList;
		
		String result = "";
		
		try {
			itineraryList = getItineraries(date, origin, destination);
			
			if (itineraryList.isEmpty()) {
				return "";
			}
			
	    	for(Itinerary itinerary: itineraryList) {
	    		result += itinerary + "\n";
	    	}
	    	
	    	return result;
	    	
		} catch (NoPathsFoundException e) {
			return "";
		} catch (ParseException e) {
			return "";
		}
		
	}
	
	/**
	 * Returns the same itineraries as getItineraries produces, but sorted 
	 * according to total itinerary cost, in non-decreasing order.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries (sorted in non-decreasing order of total itinerary 
	 * cost) that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 */
	public String getItinerariesSortedByCost(String date, String origin, 
			String destination) {
		
		// Itinerary list
		List<Itinerary> itineraryList;
		
		String result = "";
		
		try {
			itineraryList = getItineraries(date, origin, destination);
			
			// Call getItineraries and sort the returned list by cost
			Collections.sort(itineraryList, new ItineraryCostComparator());
			
			if (itineraryList.isEmpty()) {
				return "";
			}
			
	    	for(Itinerary itinerary: itineraryList) {
	    		result += itinerary + "\n";
	    	}
	    	
	    	return result;
	    	
		} catch (NoPathsFoundException e) {
			return "";
		} catch (ParseException e) {
			return "";
		}
		
	}
	
	/**
	 * Returns the same itineraries as getItineraries produces, but sorted 
	 * according to total itinerary travel time, in non-decreasing order.
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight original
	 * @param destination a flight destination
	 * @return itineraries (sorted in non-decreasing order of travel itinerary 
	 * travel time) that depart from origin and arrive at
	 * destination on the given date with stopovers at or under 6 hours.
	 * Each itinerary in the output should contain one line per flight,
	 * in the format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination
	 * followed by total price (on its own line, exactly two decimal places),
	 * followed by total duration (on its own line, in format HH:MM).
	 */
	public String getItinerariesSortedByTime(String date, String origin, 
			String destination) {
		
		// Itinerary list
		List<Itinerary> itineraryList;
		
		String result = "";
		
		try {
			itineraryList = getItineraries(date, origin, destination);
			
			// Call getItineraries and sort the returned list by time
			Collections.sort(itineraryList, new ItineraryTimeComparator());

			if (itineraryList.isEmpty()) {
				return "";
			}
			
	    	for(Itinerary itinerary: itineraryList) {
	    		result += itinerary + "\n";
	    	}
	    	
	    	return result;
	    	
		} catch (NoPathsFoundException e) {
			return "";
		} catch (ParseException e) {
			return "";
		}
		
	}

	/**
	 * Returns all flights that depart from origin and arrive at destination on
	 * the given date. 
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight origin
	 * @param destination a flight destination
	 * @return list of flights that depart from origin and arrive at 
	 * destination on the given date 
	 */
	public List<Flight> getFlights(String date, String origin, 
			String destination) {
		
		ArrayList<Flight> tempFlights = new ArrayList<Flight>();
		
		for (Map.Entry<String, Flight> entry : flights.entrySet()) {
			
			Flight flight = entry.getValue();
			
			if (flight.getDepartureDateinString().equals(date) 
					&& flight.getOrigin().equals(origin)
					&& flight.getDestination().equals(destination)) {
				tempFlights.add(flight);
			}
		}
		
    	return tempFlights;
	}
    
	/**
	 * Returns all flights that depart from origin and arrive at destination on
	 * the given date. 
	 * @param date a departure date (in the format YYYY-MM-DD)
	 * @param origin a flight origin
	 * @param destination a flight destination
	 * @return the flights that depart from origin and arrive at destination
	 *  on the given date formatted with one flight per line in exactly this
	 *  format:
	 * Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,Destination,
	 * Price (the dates are in the format YYYY-MM-DD; the price has exactly two
	 * decimal places) 
	 */
	public String getStringFlights(String date, String origin, 
			String destination) {
		
		String flightResult = "";
		List<Flight> listFlights = this.getFlights(date, origin, destination);
		
		for (Flight flight : listFlights) {
			flightResult += flight.toString() + "\n";		
		}
		
    	if (flightResult != "") {
    		// remove last newline character
    		flightResult = flightResult.substring(0, flightResult.length() - 1);
    	    return flightResult;
    	} else {
    	    return "";
    	}
	}

    /**
     * Writes the clients, flights, and itineraries Maps to files.
     * @throws IOException if file is not found
     */
    public void saveToFile() throws IOException {
    	
    	FileOutputStream clientOut = 
    			new FileOutputStream(DataConstants.CLIENTS_SERIALIZED);
    	FileOutputStream adminOut = 
    			new FileOutputStream(DataConstants.ADMINS_SERIALIZED);
    	FileOutputStream flightOut = 
    			new FileOutputStream(DataConstants.FLIGHTS_SERIALIZED);
        FileOutputStream itineraryOut = 
        		new FileOutputStream(DataConstants.ITINERARYS_SERIALIZED);
        
    	saveToFile(clientOut, adminOut, flightOut, itineraryOut);
        
    }

    /**
     * Writes the clients, flights, and itineraries Maps to files.
     * @throws IOException if file is not found
     */
    public void saveToFile(FileOutputStream cOut, FileOutputStream aOut, 
    		FileOutputStream fOut, FileOutputStream iOut) throws IOException {

        OutputStream file = cOut;
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the clients Map
        output.writeObject(clients);
        output.close();

        file = aOut;
        buffer = new BufferedOutputStream(file);
        output = new ObjectOutputStream(buffer);

        // serialize the admins map
        output.writeObject(admins);
        output.close();

        file = fOut;
        buffer = new BufferedOutputStream(file);
        output = new ObjectOutputStream(buffer);

        // serialize the flights map
        output.writeObject(flights);
        output.close();

        file = iOut;
        buffer = new BufferedOutputStream(file);
        output = new ObjectOutputStream(buffer);

        // serialize the itineraries map
        output.writeObject(itineraries);
        output.close();
        
        System.out.println("Written to file.");
        
    }
    
    /**
     * Reads data from the serialized files and deserializes the clients, 
     * flights, and itineraries Maps.
     */
	public void readFromFile() {
    	
    	readFromFile(DataConstants.CLIENTS_SERIALIZED, 
    			DataConstants.ADMINS_SERIALIZED, 
    			DataConstants.FLIGHTS_SERIALIZED,
    			DataConstants.ITINERARYS_SERIALIZED);
    	
    }
    
    /**
     * Reads data from files and deserializes the clients, flights, and
     * itineraries Maps.
     */
    @SuppressWarnings({ "unchecked" })
	public void readFromFile(String clientPath, String adminPath, 
			String flightPath, String itineraryPath) {

        try {
            InputStream file = 
            		new FileInputStream(clientPath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize the clients Map
            clients = (Map<String, Client>) input.readObject();
            input.close();
            
            file = new FileInputStream(adminPath);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            // deserialize the admins Map
            admins = (Map<String, Administrator>) input.readObject();
            input.close();
            
            file = new FileInputStream(flightPath);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            // deserialize the flights Map
            flights = (Map<String, Flight>) input.readObject();
            input.close();
            
            // clear the flight graph data
            flightGraph = new FlightGraph();
            
            // go through each flight and add it to the Flight Graph
    		for (Map.Entry<String, Flight> entry : flights.entrySet()) {
    			
    			Flight flight = entry.getValue();

				// add the location nodes to the FlightGraph
				flightGraph.addNode(flight.getOrigin());
				flightGraph.addNode(flight.getDestination());
				
				// connect the flight to the origin with an edge
				flightGraph.addEdge(flight);
				
    		}
            
            file = new FileInputStream(itineraryPath);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            // deserialize the itineraries Map
            itineraries = (Map<String, Itinerary>) input.readObject();
            input.close();
            
        } catch (IOException ex) {
            System.out.println("Serialized data not found.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            e.printStackTrace();
		}     
        
    }
    
    /**
     * Checks whether the flights of number in the list of flight numbers
     * can form a valid itinerary.
     * @param departureDate the departure date of the first flight
     * 						(in the format YYYY-MM-DD)
     * @param flightList the list of Flight objects
     * @return true if the list of flights can form a valid Itinerary
     */
    public boolean isValidItinerary (String departureDate, 
    		List<Flight> flightList) {
        
    	String firstDeparture = flightList.get(0).getDepartureDateinString();
    	
        if (!(departureDate.equals(firstDeparture))) {
            return false;
        }
        for (int i = 1; i < flightList.size(); i++) {
        	
            Flight f1 = flightList.get(i-1);
            Flight f2 = flightList.get(i);
            
            if ((f1 == null) || (f2 == null)){
                return false;
            }
            
            long timeDifference = f2.getDepartureDateTimeinDate().getTime()
                    - f1.getArrivalDateTimeinDate().getTime();
            
            // check if the next flight departure is between 0 and 6 hours of 
            // the previous flight landing
            if (timeDifference <= 0 
            		|| timeDifference > DataConstants.MAX_STOPOVER) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns true if the list of flights all have at least 1 seat available.
     * @param flightList the list of flights to check if bookable
     * @return true if the list of flights all have at least 1 seat available
     */
    public boolean isBookable(List<Flight> flightList) {
        
    	for (Flight flight:flightList) {
    		if (Integer.parseInt(flight.getNumSeats()) < 1) {
    			return false;
    		}
    	}
    	
    	return true;
    	
    }
    
    /**
     * Sets the current user of the application.
     * @param user the current user of the application
     */
    public void setCurrentUser(User user) {
    	currentUser = user;
    }
    
    /**
     * Returns the current user of the application.
     * @return the current user of the application
     */
    public User getCurrentUser() {
    	return currentUser;
    }
    
}
