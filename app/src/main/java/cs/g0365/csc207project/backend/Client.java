package cs.g0365.csc207project.backend;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a Client.
 */
public class Client extends User implements Serializable {
	
	/** Serial version ID */
	private static final long serialVersionUID = -1882551207320707135L;

	/** Address of this client */
	private String address;
	
	/*BILLING INFORMATION*/
	
	/** Credit Card number owned by this client */
	private String creditCardNumber;
	
	/** Expiry date of the credit card */
	private String expiryDate;

	/** List of Itineraries for this client */
	private List<Itinerary> itineraries;
	
	/**
	 * Creates a new client.
	 * @param firstName First name of this client
	 * @param lastName Last name of this client 
	 * @param email Email of this client
	 * @param address Address of this client
	 * @param creditCardNumber Credit Card number owned by this client
	 * @param expireDate Expire date of the credit card
	 * @throws ParseException when there is an error in the expiry date format
	 */
	public Client(String firstName, String lastName, String email,
			String address, String creditCardNumber, String expireDate) 
					throws ParseException {
		
		super(firstName, lastName, email);
		
		this.address = address;
		this.creditCardNumber = creditCardNumber;
		this.setExpiryDate(expireDate);
		
		this.setItineraries(new ArrayList<Itinerary>());
	}
	
	/**
	 * Gets the address of this client.
	 * @return the address of this client
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of this client.
	 * @param address the address of this client
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the credit card number of this client.
	 * @return the credit card number of this client
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * Sets the credit card number of this client.
	 * @param creditCardNumber the credit card number of this client
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * Gets the expiry date of this client's credit card.
	 * @return the expiry date of this client's credit card
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the expiry date of this client's credit card.
	 * @param expiryDate the expiry date of this client's credit card
	 * @throws ParseException if there is an error in the date format
	 */
	public void setExpiryDate(String expiryDate) throws ParseException {
		try {
			DataConstants.DATEPARSE.parse(expiryDate);
			this.expiryDate = expiryDate;
		} catch (ParseException e) {
			throw new ParseException("Expiry date not valid", 
					e.getErrorOffset());
		}
	}
	
	/**
	 * Adds a new itinerary to this client's itinerary list.
	 * If the client has already booked this itinerary, or the itinerary is not
	 * bookable, do nothing.
	 * @param itinerary the itinerary to book the client to
	 * @throws InvalidFlightInfoException if a flight in the itinerary has less 
	 * than 1 seat available.
	 */
	public void bookItinerary(Itinerary itinerary) 
			throws InvalidFlightInfoException {
		
		if(containsItinerary(itinerary) || 
				!DataController.getInstance().
				isBookable(itinerary.getFlights())) {
			throw new InvalidFlightInfoException("Itinerary is not bookable.");
		} else {
			itinerary.bookClient();
			this.itineraries.add(itinerary);
		}
		
	}
	
	/**
	 * Returns true if the client has already booked this new itinerary, returns
	 * false otherwise.
	 * @param newItinerary the itinerary to check if it is already booked
	 * @return true if the client has already booked this new itinerary, false 
	 * otherwise
	 */
	public boolean containsItinerary(Itinerary newItinerary) {
		
		for (Itinerary itinerary:itineraries) {
			if (itinerary.getItineraryNumber().
					equals(newItinerary.getItineraryNumber())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the list of itineraries of this client.
	 * @return the list of itineraries of this client
	 */
	public List<Itinerary> getItineraries() {
		return itineraries;
	}

	/**
	 * Sets the list of itineraries of this client to newItineraries.
	 * @param newItineraries the new list of itineraries
	 */
	public void setItineraries(List<Itinerary> newItineraries) {
		this.itineraries = newItineraries;
	}
	
	/**
	 * Returns the String representation of this client in the format:
	 * LastName,FirstNames,Email,Address,CreditCardNumber,ExpiryDate
	 * @return the String representation of this client
	 */
	@Override
	public String toString() {
		return super.toString() + "," + address + "," + creditCardNumber 
				+ "," + expiryDate;
	}
	
}
