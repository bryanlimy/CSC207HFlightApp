package cs.g0365.csc207project;

import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Displays the main menu available for administrators.
 */
public class AdministratorActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrator);
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
	 * Starts the activity responsible for searching clients.
	 * @param view the current view
	 */
    public void searchClients(View view) {
    	Intent intent = new Intent(this, SearchClientActivity.class);
    	startActivity(intent);
    }
    
    /**
	 * Starts the activity responsible for uploading flights into the system.
	 * @param view the current view
	 */
    public void uploadFlightInfo(View view) {
    	Intent intent = new Intent(this, UploadFlightInfoActivity.class);
    	startActivity(intent);
    }
    
    /**
	 * Starts the activity responsible for uploading clients into the system.
	 * @param view the current view
	 */
    public void uploadClientInfo(View view) {
    	Intent intent = new Intent(this, UploadClientInfoActivity.class);
    	startActivity(intent);
    }
    
    /**
	 * Starts the activity responsible for showing the personal information of 
	 * the current Administrator.
	 * @param view the current view
	 */
    public void viewPersonalInformation(View view){
    	Intent intent = new Intent(this, ViewAdminInfoActivity.class);
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
