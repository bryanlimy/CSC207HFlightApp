package cs.g0365.csc207project;

import java.io.FileOutputStream;
import java.util.Map;

import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.Flight;
import cs.g0365.csc207project.backend.InvalidFlightInfoException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Displays the activity to allow an admin to edit a flight's information.
 */
public class EditFlightInfoActivity extends Activity {
	
	/** The instance of this data. */
	private DataController datacontroller = DataController.getInstance();
	
	/** The flight that is being edit. */
	private Flight flight = null;
	
	/** The flight number of this flight. */
	private String flightNumber;
	
	/** The map of all flights. */
    private Map<String, Flight> flightsMap;

	/** The origin location. */
	private String originSearch;
	
	/** The destination location. */
	private String destinationSearch;
	
	/** The departure date searched for. */
	private String dateSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_flight_info);
		
		Intent intent = getIntent();
		
        // Uses keys to retrieve the date.
		originSearch = (String) intent.getSerializableExtra("originName");
		destinationSearch 
		= (String) intent.getSerializableExtra("destinationName");
		dateSearch = (String) intent.getSerializableExtra("flightDate");
        
		flightNumber = (String) intent.
				getSerializableExtra("flightNumber");
		
		flight = datacontroller.getFlight(flightNumber);
		flightsMap = datacontroller.getFlights();
		
		showFlightInfo();
	}
	
	/**
	 * Displays this flight's information.
	 */
	public void showFlightInfo() {
		//Display flight number
		EditText flightNumber = (EditText) findViewById(R.id.
				flight_number);
		flightNumber.setText(flight.getFlightNumber());
		
		//Get departure information
		String departureDateTime = flight.getDepartureDateTimeinString();
		int departYear = Integer.parseInt(departureDateTime.substring(0, 4));
		int departMonth = 
				Integer.parseInt(departureDateTime.substring(5, 7)) - 1;
		int departDay = Integer.parseInt(departureDateTime.substring(8, 10));
		int departHour = Integer.parseInt(departureDateTime.substring(11,13));
		int departMinute = Integer.parseInt(departureDateTime.substring(14,16));
		
		//Display departure date
		DatePicker departureDatePicker = 
				(DatePicker) findViewById(R.id.departure_date);
		departureDatePicker.updateDate(departYear, departMonth, departDay);
		
		//Display departure time
		TimePicker departureTimePicker = 
				(TimePicker) findViewById(R.id.departure_time);
		departureTimePicker.setIs24HourView(true);
		departureTimePicker.setCurrentHour(departHour);
		departureTimePicker.setCurrentMinute(departMinute);
		
		//Get arrival information
		String arrivalDateTime = flight.getArrivalDateTimeinString();
		int arriveYear = Integer.parseInt(arrivalDateTime.substring(0, 4));
		int arriveMonth = Integer.parseInt(arrivalDateTime.substring(5, 7)) - 1;
		int arriveDay = Integer.parseInt(arrivalDateTime.substring(8, 10));
		int arriveHour = Integer.parseInt(arrivalDateTime.substring(11,13));
		int arriveMinute = Integer.parseInt(arrivalDateTime.substring(14,16));
		
		//Display arrival date
		DatePicker arrivalDatePicker = 
				(DatePicker) findViewById(R.id.arrival_date);
		arrivalDatePicker.updateDate(arriveYear, arriveMonth, arriveDay);
		
		//Display arrival time
		TimePicker arrivalTimePicker = 
				(TimePicker) findViewById(R.id.arrival_time);
		arrivalTimePicker.setIs24HourView(true);
		arrivalTimePicker.setCurrentHour(arriveHour);
		arrivalTimePicker.setCurrentMinute(arriveMinute);
		
		//Display airline
		EditText airline = (EditText) findViewById(R.id.airline);
		airline.setText(flight.getAirline());
		
		//Display origin
		EditText origin = (EditText) findViewById(R.id.origin);
		origin.setText(flight.getOrigin());
		
		//Display destination
		EditText destination = (EditText) findViewById(R.id.destinatoin);
		destination.setText(flight.getDestination());
		
		//Display price
		EditText price = (EditText) findViewById(R.id.price);
		price.setText(flight.getPrice());
		
		//Display number of seats
		EditText numSeats = (EditText) findViewById(R.id.numSeats);
		numSeats.setText(flight.getNumSeats());
	}
	
	/**
	 * Updates this flight's information with the given inputs.
	 * @param view the current view
	 */
	public void editFlightInfo(View view) {
		
		// Get flight number
		EditText flightNumberField = 
				(EditText) findViewById(R.id.flight_number);
		String newFlightNumber = flightNumberField.getText().toString();
		
		// Get departure date and time
		DatePicker departureDatePicker = 
				(DatePicker)findViewById(R.id.departure_date);
		String departDay = formatDigit(departureDatePicker.getDayOfMonth());
		String departMonth = formatDigit(departureDatePicker.getMonth() + 1);
		int departYear = departureDatePicker.getYear();
		TimePicker departureTimePicker = 
				(TimePicker) findViewById(R.id.departure_time);
		String departHour = formatDigit(departureTimePicker.getCurrentHour());
		String departMintue = 
				formatDigit(departureTimePicker.getCurrentMinute());
		
		String departureDateTime = departYear + "-" + departMonth + "-" 
		+ departDay + " " + departHour + ":" + departMintue;
		
		// Get arrival date and time
		DatePicker arrivalDatePicker = 
				(DatePicker)findViewById(R.id.arrival_date);
		String arriveDay = formatDigit(arrivalDatePicker.getDayOfMonth());
		String arriveMonth = formatDigit(arrivalDatePicker.getMonth() + 1);
		int arriveYear = arrivalDatePicker.getYear();
		TimePicker arrivalTimePicker = 
				(TimePicker) findViewById(R.id.arrival_time);
		String arriveHour = formatDigit(arrivalTimePicker.getCurrentHour());
		String arriveMinute = formatDigit(arrivalTimePicker.getCurrentMinute());
		
		String arrivalDateTime = arriveYear + "-" + arriveMonth + "-" 
		+ arriveDay + " " + arriveHour + ":" + arriveMinute;
		
		// Get airline
		EditText airlineField = (EditText) findViewById(R.id.airline);
		String airline = airlineField.getText().toString();
		
		// Get origin
		EditText originField = (EditText) findViewById(R.id.origin);
		String origin = originField.getText().toString();
		
		// Get destination
		EditText destinationField = (EditText) findViewById(R.id.destinatoin);
		String destination = destinationField.getText().toString();
		
		// Get price
		EditText priceField = (EditText) findViewById(R.id.price);
		String price = priceField.getText().toString();
		
		// Get number of seats
		EditText numSeatsField = (EditText) findViewById(R.id.numSeats);
		String numSeats = numSeatsField.getText().toString();
		
		// Count the number of flights with the same flight number
		int numFlight = 0;
		Map<String, Flight> Flights = datacontroller.getFlights();
		for (String flight: Flights.keySet()) {
			if (flight.equals(newFlightNumber)) {
				numFlight += 1;
			}
		}
		
		if (newFlightNumber.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter a flight "
					+ "number.",Toast.LENGTH_LONG).show();
		} else if (numFlight >= 1 && !newFlightNumber.equals(flightNumber)) {
			Toast.makeText(getApplicationContext(),"Flight " + newFlightNumber 
					+ " already exist", Toast.LENGTH_LONG).show();
		} else if (airline.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter an airline.", 
            		Toast.LENGTH_LONG).show();
		} else if (origin.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter an origin.", 
            		Toast.LENGTH_LONG).show();
		} else if (destination.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter a "
					+ "destination.", Toast.LENGTH_LONG).show();
		} else if (price.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter a price.", 
            		Toast.LENGTH_LONG).show();
		} else if (numSeats.matches("")) {
			Toast.makeText(getApplicationContext(),"Please enter a number of "
					+ "seats.", 
            		Toast.LENGTH_LONG).show();
		} else if (origin.contentEquals(destination)) {
			Toast.makeText(getApplicationContext(),"The origin and destination "
					+ "cannot be the same.", 
					Toast.LENGTH_LONG).show();
		} else if (Integer.parseInt(numSeats) < 0) {
			Toast.makeText(getApplicationContext(),"The number of seats cannot "
					+ "be less than zero.", 
					Toast.LENGTH_LONG).show();
		} else {
			flight.setFlightNumber(newFlightNumber);
			flight.setAirline(airline);
			try {
				flight.setOrigin(origin);
				flight.setDestination(destination);
			} catch (InvalidFlightInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				flight.setPrice(price);
			} catch (InvalidFlightInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				flight.setNumSeats(numSeats);
			} catch (InvalidFlightInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				flight.setDepartureDateTime(departureDateTime);
				flight.setArrivalDateTime(arrivalDateTime);
				Toast.makeText(getApplicationContext(),"Edited flight "
						+ "information.", Toast.LENGTH_LONG).show();
				
				// update the flights key in the flights map
				if (!newFlightNumber.equals(flightNumber)) {
					flightsMap.put(newFlightNumber, 
							flightsMap.get(flightNumber));
					flightsMap.remove(flightNumber);
				}
				
			    saveData();
			    
		        // Specifies the next Activity to move to FlightResultsActivity
		        Intent intent = new Intent(this, FlightResultsActivity.class);
		        
		        // Passes flight search parameters back to FlightResultsActivity
		        intent.putExtra("originName", originSearch);
		        intent.putExtra("destinationName", destinationSearch);
		        intent.putExtra("flightDate", dateSearch);

		        startActivity(intent);
			    
			} catch (InvalidFlightInfoException e) {
				Toast.makeText(getApplicationContext(),"The arrival date and "
						+ "time cannot be before the departure date and time.", 
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
    /**
     * Returns the number with a leading 0 if it only has one digit.
     * @param number the number to format into 2 digits
     * @return the number with a leading 0 if it only has one digit
     */
    private String formatDigit(int number) {
    	if (number <= 9) {
    		return "0" + number;
    	} else {
    		return "" + number;
    	}
    }
	
    /**
     * Writes the clients, flights, and itineraries Maps to files.
     */
	public void saveData() {
        
		try {
			FileOutputStream cOut = openFileOutput("clients.ser", 
					Context.MODE_PRIVATE);
			FileOutputStream aOut = openFileOutput("admins.ser", 
					Context.MODE_PRIVATE);
			FileOutputStream fOut = openFileOutput("flights.ser", 
					Context.MODE_PRIVATE);
			FileOutputStream iOut = openFileOutput("itineraries.ser", 
					Context.MODE_PRIVATE);
			datacontroller.saveToFile(cOut, aOut, fOut, iOut);
			
			cOut.close();
			aOut.close();
			fOut.close();
			iOut.close();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(),"Problem saving the data.", 
					Toast.LENGTH_SHORT).show();
		}
		
	}
}
