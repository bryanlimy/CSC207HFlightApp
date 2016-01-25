package cs.g0365.csc207project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.Flight;
import cs.g0365.csc207project.backend.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Displays the flights matching the user's search departure date, origin, and 
 * destination.
 */
public class FlightResultsActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	/** The list view to display the flights. */
	private ListView lv;
	
	/** The adapter to customize the display of the flights. */
	private FlightAdapter flightAdapter;

	/** The list of flights matching the user's search. */
	private List<Flight> flightResultsList;
	
	/** The current user of the application. */
	private User currentUser;
	
	/** The show results only button. */
	private Button showResults;
	
	/** The show all flights button. */
	private Button showAllFlights;

	/** The origin location. */
	private String origin;
	
	/** The destination location. */
	private String destination;
	
	/** The departure date searched for. */
	private String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_results);
		
        // Gets the Intent passed from SearchFlightsActivity
        Intent intent = getIntent();
        
        currentUser = dc.getCurrentUser();
		
        // alert user if no flight data has been uploaded
        if (dc.getFlights().isEmpty()) {
            Toast.makeText(getApplicationContext(),"No flight data has been "
            		+ "uploaded.", Toast.LENGTH_LONG).show();
        }
        
        // Uses keys to retrieve the date.
        origin = (String) intent.getSerializableExtra("originName");
        destination = (String) intent.getSerializableExtra("destinationName");
        date = (String) intent.getSerializableExtra("flightDate");
        
        // get the list view
        lv = (ListView) findViewById(R.id.flight_results_list);

        // get the flights in a list
        flightResultsList = dc.getFlights(date, origin, destination);

        // display the flights in the listView
        flightAdapter = new FlightAdapter(this,
        		android.R.layout.simple_list_item_1, flightResultsList);

        lv.setAdapter(flightAdapter); 
        flightAdapter.notifyDataSetChanged();
        
        // action if a flight is clicked
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
		        
		    	// display a confirmation dialog for the client
		    	if (currentUser instanceof Administrator) {
		    		confirm_edit(position);
		    	}
		    	
			}
			
		});	  

		showResults = (Button) findViewById(R.id.show_results);
		showAllFlights = (Button) findViewById(R.id.show_all_flights);
		
	}
	
    /**
     * Displays a dialog for the Admin to confirm editing flight information.
     * @param position the position of the selected flight in the list
     */
    private void confirm_edit(int position) {
    	
	    final Flight flight = 
	    		flightAdapter.getItem(position);
	    final int pos = position;
	    
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setMessage("Edit flight " + flight.getFlightNumber())
    	       .setTitle("Edit Flight Information");

		builder.setPositiveButton("Confirm", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		        	   
		        	Flight flight = flightAdapter.getItem(pos);
		        	
		        	// pass the flight number to the EditFlightInfoActivity
	    	    	Intent intent = 
	    	    			new Intent(FlightResultsActivity.this, 
	    	    					EditFlightInfoActivity.class);
	    	    	intent.putExtra("flightNumber", flight.getFlightNumber());
	    	        
	    	        // Passes flight search parameters
	    	        intent.putExtra("originName", origin);
	    	        intent.putExtra("destinationName", destination);
	    	        intent.putExtra("flightDate", date);
	    	        
	    	    	startActivity(intent);
		        	
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
	 * Displays only the flights matching the search fields.
	 * @param view the current view
	 */
    public void showResults(View view) {

		showResults.setBackgroundResource(R.drawable.itinerary_sort_button);
		showAllFlights.setBackgroundColor(Color.parseColor("#2BBABD"));
    	
        // display the flights in the listView
        flightAdapter = new FlightAdapter(this,
        		android.R.layout.simple_list_item_1, flightResultsList);
        lv.setAdapter(flightAdapter);
        
    }
    
	/**
	 * Displays all the flights stored in the application.
	 * @param view the current view
	 */
    public void showAllFlights(View view) {

    	showAllFlights.setBackgroundResource(R.drawable.itinerary_sort_button);
    	showResults.setBackgroundColor(Color.parseColor("#2BBABD"));
    	
		List<Flight> flightList = new ArrayList<Flight>();
		Map<String, Flight> flightMap = 
				DataController.getInstance().getFlights();
		
		for (String key: flightMap.keySet()) {
			flightList.add(flightMap.get(key));
		}

        // display the flights in the listView
        flightAdapter = new FlightAdapter(this,
        		android.R.layout.simple_list_item_1, flightList);
        
        lv.setAdapter(flightAdapter);
        
    }
    
	/**
	 * Return to SearchFlightsActivity when pressing "Back" button.
	 */
    @Override
    public void onBackPressed() {
    	Intent intent = new Intent(this, SearchFlightsActivity.class);
		startActivity(intent);
    }
}
