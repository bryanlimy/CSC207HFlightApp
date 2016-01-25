package cs.g0365.csc207project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Allows a user to enter a departure date, origin, and destination and sends
 * them to the itinerary results activity.
 */
public class SearchItinerariesActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_itineraries);
	}
    
    /**
     * Gets the search information that the user entered and passes the
     * information to the ItineraryResultsActivity to display the results.
     * @param view the current view
     */
    public void searchItineraries(View view) {
         
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
        
        // Specifies the next Activity to move to: ItineraryResultsActivity
        Intent intent = new Intent(this, ItineraryResultsActivity.class);
        
        // Passes flight search parameters to ItineraryResultsActivity
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
    
}
