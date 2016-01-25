package cs.g0365.csc207project;

import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Displays the personal and billing information of a given client.
 */
public class ViewClientInfoActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();

	/** The selected client. */
	private Client client = null;

	/** The selected client's email. */
	private String clientEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_client_info);
		
		Intent intent = getIntent();
		clientEmail = (String) intent.getSerializableExtra("clientEmail");
		
		client = (Client) dc.getUser(clientEmail);
		
		// Display client's current first name
		TextView clientFirstNameField = (TextView) findViewById(R.id.
				first_name_label);
		clientFirstNameField.setText(client.getFirstName());
		
		// Display client's current last name
		TextView clientLastNameField = (TextView) findViewById(R.id.
				last_name_label);
		clientLastNameField.setText(client.getLastName());
		
		// Display client's current email address
		TextView clientEmailAddressField = 
				(TextView) findViewById(R.id.email_label);
		
		clientEmailAddressField.setText(client.getEmail());
		
		// Display client's current address
		TextView clientAddressField = 
				(TextView) findViewById(R.id.address_label);
		
		clientAddressField.setText(client.getAddress());
		
		// Display client's current credit card number
		TextView clientCreditCardField = 
				(TextView) findViewById(R.id.credit_card_number);
		
		clientCreditCardField.setText(client.getCreditCardNumber());
		
		// Display client's current credit card expire date
		TextView date = (TextView) findViewById(R.id.credit_card_expire);
		date.setText(this.client.getExpiryDate());
	}
	
	/**
	 * Starts the BookedItinerariesActivity to display all the booked 
	 * itineraries of the current client.
	 * @param view the current view
	 */
	public void viewBookedItineraries(View view){
		Intent intent = new Intent(this, BookedItinerariesActivity.class);
    	intent.putExtra("clientEmail", clientEmail);
    	startActivity(intent);
	}
	
	/**
	 * Starts the EditClientInfoActivity for the current client.
	 * @param view the current view
	 */
	public void editClient(View view){
		Intent intent = new Intent(ViewClientInfoActivity.this, 
				EditClientInfoActivity.class);
    	intent.putExtra("clientEmail", client.getEmail());
    	startActivity(intent);
	}
	
	/**
	 * Returns the user to SearchClientActivity if they are an Administrator 
	 * or ClientActivity if they are a Client when pressing the "Back" 
	 * button.
	 */
    @Override
    public void onBackPressed() {
    	if (dc.getCurrentUser() instanceof Client) {
    		Intent intent = new Intent(this, ClientActivity.class);
    		startActivity(intent);
    	} else {
        	Intent intent = new Intent(this, SearchClientActivity.class);
        	startActivity(intent);
    	}
    }
	
}
