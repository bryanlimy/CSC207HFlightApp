package cs.g0365.csc207project;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Allows a user to enter a departure date, origin, and destination and sends
 * them to the flight results activity.
 */
public class SearchFlightsActivity extends Activity {
	
	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_flights);
		
	}
    
    /**
     * Gets the search information that the user entered and passes the
     * information to the FlightResultsActivity to display the results.
     * @param view the current view
     */
    public void searchFlights(View view) {
         
        // Gets the origin from the origin_field.
        EditText originField = (EditText) findViewById(R.id.origin_field);
        String origin = originField.getText().toString();
        
        // Gets the destination from the destination_field.
        EditText destinationField = 
        		(EditText) findViewById(R.id.destination_field);
        String destination = destinationField.getText().toString();
        
        // Gets the DatePicker from the flight_date.
        DatePicker datePicker = (DatePicker) findViewById(R.id.flight_date);
        
        String day = formatDigit(datePicker.getDayOfMonth());
        String month = formatDigit(datePicker.getMonth() + 1);
        int year = datePicker.getYear();
        
        String date = year + "-" + month + "-" + day;
        
        // Specifies the next Activity to move to: FlightResultsActivity.
        Intent intent = new Intent(this, FlightResultsActivity.class);
        
        // Passes flight search parameters to FlightResultsActivity
        intent.putExtra("originName", origin);
        intent.putExtra("destinationName", destination);
        intent.putExtra("flightDate", date);
        
        startActivity(intent);
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
	 * Returns the user to AdministratorActivity if they are an Administrator 
	 * or ClientActivity if they are a Client when pressing the "Back" 
	 * button.
	 */
    @Override
    public void onBackPressed() {
    	if (dc.getCurrentUser() instanceof Administrator) {
    		Intent intent = new Intent(this, AdministratorActivity.class);
    		startActivity(intent);
    	} else {
    		Intent intent = new Intent(this, ClientActivity.class);
    		startActivity(intent);
    	}
    	
    }
    
}
