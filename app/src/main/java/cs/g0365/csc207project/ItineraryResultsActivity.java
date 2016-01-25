package cs.g0365.csc207project;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.Flight;
import cs.g0365.csc207project.backend.InvalidFlightInfoException;
import cs.g0365.csc207project.backend.Itinerary;
import cs.g0365.csc207project.backend.ItineraryCostComparator;
import cs.g0365.csc207project.backend.ItineraryTimeComparator;
import cs.g0365.csc207project.backend.NoPathsFoundException;
import cs.g0365.csc207project.backend.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Displays the itineraries found and allows the user to sort the itineraries
 * by total travel time or total cost and select an itinerary for booking.
 */
public class ItineraryResultsActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();

	/** The list of itineraries matching the user's search. */
	private List<Itinerary> flightResultsList = new ArrayList<Itinerary>();
	
	/** The list view to display the itineraries. */
	private ListView lv;
	
	/** The adapter to customize the display of the itineraries. */
	private ItineraryAdapter itineraryAdapter;
	
	/** The current user of the application. */
	private User currentUser;
	
	/** The client selected to book the itinerary for. */
	private Client selectedClient;
	
	/** The sort by time button. */
	private Button sortTimeButton;
	
	/** The sort by cost button. */
	private Button sortCostButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itinerary_results);

        // alert user if no flight data has been uploaded
        if (dc.getFlights().isEmpty()) {
            Toast.makeText(getApplicationContext(),"No flight data has been "
            		+ "uploaded.", Toast.LENGTH_LONG).show();
        }
        
        // Gets the Intent passed from SearchItinerariesActivity
        Intent intent = getIntent();
        
        currentUser = dc.getCurrentUser();
		
        // Uses keys to retrieve the date.
        String origin = (String) intent.getSerializableExtra("originName");
        String destination = 
        		(String) intent.getSerializableExtra("destinationName");
        String date = (String) intent.getSerializableExtra("flightDate");
        
        // get the list view
        lv = (ListView) findViewById(R.id.itinerary_results_list);
		
		try {
	        // get the flights in a list
	        flightResultsList = dc.getItineraries(date, origin, destination);
	        saveData();

	        // display the itineraries in the listView
	        itineraryAdapter = new ItineraryAdapter(this,
	        		android.R.layout.simple_list_item_1, flightResultsList);
	        
	        lv.setAdapter(itineraryAdapter); 
	        itineraryAdapter.notifyDataSetChanged();
			
	        // action if an itinerary is clicked
			lv.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
			        
				    if (currentUser == null) {
						Toast.makeText(getBaseContext(), 
								"Please log in again.", 
								Toast.LENGTH_SHORT).show();
				    } else {

				    	// display a confirmation dialog for the client
				    	if (currentUser instanceof Client) {
				    		confirm_booking(position);
				    	}
				    	// display list of client's that admin can book for
				    	else if (currentUser instanceof Administrator) {
				    		admin_booking_dialog(position);
				    	}
				    }
				}
				
			});	    
	    	
		} catch (NoPathsFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
		// disable sort buttons if no itineraries are found
    	if (flightResultsList.isEmpty()) {
            Toast.makeText(getBaseContext(), "No results found.", 
            		Toast.LENGTH_SHORT).show();
    		Button sortTimeButton = (Button) findViewById(R.id.sort_time);
    		sortTimeButton.setEnabled(false);
    		
    		Button sortCostButton = (Button) findViewById(R.id.sort_cost);
    		sortCostButton.setEnabled(false);
    	}
    	
    	sortTimeButton = (Button) findViewById(R.id.sort_time);
    	sortCostButton = (Button) findViewById(R.id.sort_cost);
    	
	}
	
    /**
     * Sorts and displays the itineraries in non-decreasing order of travel 
     * itinerary travel time.
     * @param view the current view
     */
    public void sortByTime(View view) {
    	
		sortTimeButton.setBackgroundResource(R.drawable.itinerary_sort_button);
		sortCostButton.setBackgroundColor(Color.parseColor("#2BBABD"));
		
		// Sort the itineraries by time
		Collections.sort(flightResultsList, new ItineraryTimeComparator());
		itineraryAdapter = new ItineraryAdapter(this,
                android.R.layout.simple_list_item_1,
                flightResultsList);
        lv.setAdapter(itineraryAdapter);
        
    }
    
    /**
     * Sorts and displays the itineraries in non-decreasing order of total 
     * itinerary cost in the current view.
     * @param view the current view
     */
    public void sortByCost(View view) {
    	
    	sortCostButton.setBackgroundResource(R.drawable.itinerary_sort_button);
    	sortTimeButton.setBackgroundColor(Color.parseColor("#2BBABD"));
		
		// Sort the itineraries by cost
		Collections.sort(flightResultsList, new ItineraryCostComparator());
		itineraryAdapter = new ItineraryAdapter(this,
                android.R.layout.simple_list_item_1,
                flightResultsList);
        lv.setAdapter(itineraryAdapter); 
        
    }
    
    /**
     * Displays a dialog for the Administrator to select a client to book an 
     * itinerary for.
     * @param position the position of the selected itinerary in the list
     */
    private void admin_booking_dialog(int position) {
    	
	    final int pos = position;
	    
    	// the list of clients
    	ArrayList<String> clientList = new ArrayList<String>();
    	
		for (Map.Entry<String, Client> entry : dc.getClients().entrySet()) {
			Client client = entry.getValue();
			clientList.add(client.getFirstName() + " " + client.getLastName() 
					+ " (" + client.getEmail() + ")");
		}

        final ArrayAdapter<String> adp = 
        		new ArrayAdapter<String>(ItineraryResultsActivity.this,
                android.R.layout.simple_spinner_item, clientList);
        
        // add the users' names to a spinner
        final Spinner client_list_spinner = 
        		new Spinner(ItineraryResultsActivity.this);
        client_list_spinner.setLayoutParams(new LinearLayout.LayoutParams
        		(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.
        				ViewGroup.LayoutParams.WRAP_CONTENT));
        client_list_spinner.setAdapter(adp);
        
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setMessage("Select client to book for:")
    	       .setTitle("Book itinerary");

		builder.setPositiveButton("Book", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {

			       	    Itinerary itinerary = 
			       	    		itineraryAdapter.getItem(pos);
			       	    
			       	    // get the selected client's email
			    		String clientInfo = client_list_spinner.
			    				getSelectedItem().toString();
			            
			            Matcher matcher = Pattern.compile("\\(([^)]+)\\)").
			            		matcher(clientInfo);
			            String clientEmail = "";
			            
			            // extract the client's email
						if (matcher.find())	{
							clientEmail = matcher.group(1);
						}
						
						selectedClient = dc.getClient(clientEmail);
			            
			            try {
			            	selectedClient.bookItinerary(itinerary);
			            	updateFlightSeats(itinerary);
			    			
			    			String alertMessage = 
			    					"Successfully booked itinerary " 
			    			+ itinerary.getItineraryNumber() + " for "
			    			+ selectedClient.getFirstName() + " " 
			    			+ selectedClient.getLastName();
			    			
			    	        Toast.makeText(getBaseContext(), alertMessage, 
			    	        		Toast.LENGTH_SHORT).show();
			    	        
			    	        // save the data
			    	        saveData();
			    	        
			    	        // display the user's booked itineraries
			    	    	Intent intent = 
			    	    			new Intent(ItineraryResultsActivity.this, 
			    	    					BookedItinerariesActivity.class);
			    	    	
			    	    	intent.putExtra("clientEmail", 
			    	    			selectedClient.getEmail());
			    	    	
			    	    	startActivity(intent);
			    	        
			    		} catch (InvalidFlightInfoException e) {
			    			
			    			String alertMessage = selectedClient.getFirstName() 
			    					+ " " + selectedClient.getLastName() 
			    					+ " has already booked this itinerary or "
			    					+ "the itinerary is full";
			    			
			    			Toast.makeText(getBaseContext(), alertMessage, 
			    					Toast.LENGTH_SHORT).show();
			    		}
			            
		           }
		       });
		builder.setNegativeButton("Cancel", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // do nothing if the user pressed 'Cancel'
		       }
		   });

	    builder.setView(client_list_spinner);
    	builder.create().show();
    } 
    
    /**
     * Displays a dialog for the client to confirm an itinerary booking.
     * @param position the position of the selected itinerary in the list
     */
    private void confirm_booking(int position) {

	    final Itinerary itinerary = 
	    		itineraryAdapter.getItem(position);
	    final int pos = position;
	    
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setMessage("Book itinerary " + itinerary.getItineraryNumber())
    	       .setTitle("Book itinerary");

		builder.setPositiveButton("Confirm", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {

		       	    Itinerary itinerary = 
		       	    		itineraryAdapter.getItem(pos);
		       	    
		            try {
		            	
		    			((Client) currentUser).bookItinerary(itinerary);
		            	updateFlightSeats(itinerary);
		    			
		    	        // Display the itinerary number.
		    	        Toast.makeText(getBaseContext(), 
		    	        		"You have successfully booked itinerary " 
		    	        + itinerary.getItineraryNumber(), 
		    	        		Toast.LENGTH_SHORT).show();

		    	        saveData();
		    	        
		    	        // display the user's booked itineraries
		    	    	Intent intent = 
		    	    			new Intent(ItineraryResultsActivity.this, 
		    	    					BookedItinerariesActivity.class);
		    	    	
		    	    	intent.putExtra("clientEmail", 
		    	    			dc.getCurrentUser().getEmail());
		    	    	
		    	    	startActivity(intent);
		    	        
		    		} catch (InvalidFlightInfoException e) {
		    			Toast.makeText(getBaseContext(), 
		    					"You have already booked this itinerary or "
		    					+ "the itinerary is full.", 
		    					Toast.LENGTH_SHORT).show();
		    		}
				        
		           }
		       });
		builder.setNegativeButton("Cancel", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // do nothing if the user pressed 'Cancel'
		       }
		   });

    	builder.create().show();
    }
    
    /**
     * Update each flight in the itinerary with the corresponding flight in the 
     * flights map in the Data Controller class to reflect the change in number 
     * of seats.
     * @param itinerary the itinerary with the flights to update
     */
    private void updateFlightSeats(Itinerary itinerary) {
    	
    	List<Flight> flightList = itinerary.getFlights();
    	Flight mapFlight = null;
    	
    	for (Flight flight:flightList) {
    		
    		// get the flight in the flight map
    		mapFlight = dc.getFlight(flight.getFlightNumber());
    		
    		// update the flight's seat number
    		try {
				mapFlight.setNumSeats(flight.getNumSeats());
			} catch (InvalidFlightInfoException e) {
				e.printStackTrace();
			}
    		
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
			dc.saveToFile(cOut, aOut, fOut, iOut);
			
			cOut.close();
			aOut.close();
			fOut.close();
			iOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
}
