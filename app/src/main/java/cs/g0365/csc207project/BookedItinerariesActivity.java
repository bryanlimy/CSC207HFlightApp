package cs.g0365.csc207project;

import java.io.FileOutputStream;
import java.util.List;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.Itinerary;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Displays the booked itineraries of a client.
 */
public class BookedItinerariesActivity extends Activity {

	/** The DataController instance. */
	private DataController dc;
	
	/** The Client to display the itineraries for. */
	private Client client;
	
	/** The list of itineraries of the client. */
	private List<Itinerary> itnLst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booked_itineraries);
		
		dc = DataController.getInstance();
		
		Intent intent = getIntent();
		
		// get the client object from the email that was passed into the intent
		String clientEmail = 
				(String) intent.getSerializableExtra("clientEmail");
		client = (Client) dc.getUser(clientEmail);
		itnLst = client.getItineraries();
		
		// set the title
		String title = client.getFirstName() + " " + client.getLastName() 
				+ "'s Itineraries";
		setTitle(title);
		
		ItineraryAdapter itineraryAdapter = new ItineraryAdapter(this,
                android.R.layout.simple_list_item_1,
                itnLst );
        ((ListView)findViewById(R.id.booked_itinerary_list))
        	.setAdapter(itineraryAdapter); 
        itineraryAdapter.notifyDataSetChanged();
		
	}
    
	/**
	 * Sends the user back to the main screen.
	 * @param view the current view
	 */
    public void mainScreen(View view) {
    	if (dc.getCurrentUser() instanceof Administrator){
            Intent intent = new Intent(this, AdministratorActivity.class);
            startActivity(intent);
    	}
    	else{
            Intent intent = new Intent(this, ClientActivity.class);
            startActivity(intent);
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
			fOut.close();
			iOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
