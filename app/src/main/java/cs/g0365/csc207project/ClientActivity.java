package cs.g0365.csc207project;

import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Displays the main menu available for clients.
 */
public class ClientActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		
	}
	
	/**
	 * Starts the activity responsible for viewing the personal information of 
	 * this client.
	 * @param view the current view
	 */
	public void viewClientInfo(View view) {
		Intent intent = new Intent(this, ViewClientInfoActivity.class);
    	intent.putExtra("clientEmail", this.dc.getCurrentUser().getEmail());
    	startActivity(intent);
    }
	
	/**
	 * Starts the activity responsible for searching flights.
	 * @param view the current view
	 */
	public void searchFlights(View view) {
        Intent intent = new Intent(this, SearchFlightsActivity.class);
        startActivity(intent);
    }
	
	/**
	 * Starts the activity responsible for searching itineraries.
	 * @param view the current view
	 */
    public void searchItineraries(View view) {
        Intent intent = new Intent(this, SearchItinerariesActivity.class);
        startActivity(intent);
    }
    
	/**
	 * Starts the activity responsible for viewing booked itineraries of the
	 * current client.
	 * @param view the current view
	 */
    public void viewBookedItineraries(View view){
    	Intent intent = new Intent(this, BookedItinerariesActivity.class);
    	intent.putExtra("clientEmail", dc.getCurrentUser().getEmail());
    	startActivity(intent);
    }
    
	/**
	 * Starts the activity responsible for editing personal information of the 
	 * current client.
	 * @param view the current view
	 */
    public void editClientInfo(View view) {
    	Intent intent = new Intent(this, EditClientInfoActivity.class);
    	intent.putExtra("clientEmail", this.dc.getCurrentUser().getEmail());
    	startActivity(intent);
	}
    
	/**
	 * Logs the current user out.
	 * @param view the current view
	 */
    public void logout(View view) {
    	dc.setCurrentUser(null);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
	}
    
	/**
	 * Do nothing when pressing "Back" button.
	 */
    @Override
    public void onBackPressed() {
    	return;
    }

}
