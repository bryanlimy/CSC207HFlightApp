package cs.g0365.csc207project;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Map;

import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Displays the activity to allow a user to edit a client's information.
 */
public class EditClientInfoActivity extends Activity {
	
	/** The instance of this data. */
	private DataController dc = DataController.getInstance();
	
	/** The client that is being edit. */
	private Client client = null;
	
	/** The email of this client. */
	private String clientEmail;
	
	/** The map of all clients. */
	private Map<String, Client> clientsMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_client_info);
		
		Intent intent = getIntent();
		clientEmail = (String) intent.getSerializableExtra("clientEmail");
		
		client = (Client) dc.getUser(clientEmail);
		clientsMap = dc.getClients();
		
		showClientInfo();
	}
	
	/**
	 * Displays this client's information.
	 */
	private void showClientInfo() {
		
		// Display client's current first name
		EditText clientFirstNameField = (EditText) findViewById(R.id.
				first_name_label);
		clientFirstNameField.setText(client.getFirstName());
		
		// Display client's current last name
		EditText clientLastNameField = (EditText) findViewById(R.id.
				last_name_label);
		clientLastNameField.setText(client.getLastName());
		
		// Display client's current email address
		EditText clientEmailAddressField = 
				(EditText) findViewById(R.id.email_label);
		clientEmailAddressField.setText(client.getEmail());
		
		// Display client's current address
		EditText clientAddressField = 
				(EditText) findViewById(R.id.address_label);
		clientAddressField.setText(client.getAddress());
		
		// Display client's current credit card number
		EditText clientCreditCardField = 
				(EditText) findViewById(R.id.credit_card_number);
		clientCreditCardField.setText(client.getCreditCardNumber());
		
		// Display client's current credit card expire date
		DatePicker datePicker = 
				(DatePicker) findViewById(R.id.expire_date_label);
		String expireDate = client.getExpiryDate();
		int year = Integer.parseInt(expireDate.substring(0, 4));
		int month = Integer.parseInt(expireDate.substring(5, 7)) - 1;
		int day = Integer.parseInt(expireDate.substring(8, 10));
		datePicker.updateDate(year, month, day);
	}
	
	/**
	 * Updates the selected client's information with the new inputs.
	 * @param view the current view
	 */
	public void editClientInfo(View view) {
		
		// Client's first name
		EditText clientFirstNameField = 
				(EditText) findViewById(R.id.first_name_label);
		
		String clientFirstName = 
				clientFirstNameField.getText().toString();
		
		// Client's last name
		EditText clientLastNameField = 
				(EditText) findViewById(R.id.last_name_label);
		
		String clientLastName = 
				clientLastNameField.getText().toString();
		
		// Client's email address
		EditText clientEmailAddressField = (EditText) 
				findViewById(R.id.email_label);
		
		String clientEmailAddress = clientEmailAddressField
				.getText().toString();
		
		// Client's address
		EditText clientAddressField = 
				(EditText) findViewById(R.id.address_label);
		
		String clientAddress = clientAddressField.getText().toString();
		
		// Client's credit card number
		EditText clientCreditCardField = 
				(EditText) findViewById(R.id.credit_card_number);
		
		String clientCreditCard = clientCreditCardField.getText().toString();
		
		// Client's credit card expire date
		DatePicker datePicker = 
				(DatePicker) findViewById(R.id.expire_date_label);
		
        String day = formatDigit(datePicker.getDayOfMonth());
        String month = formatDigit(datePicker.getMonth() + 1);
        int year = datePicker.getYear();
        String clientCreditCardExpire = year + "-" + month + "-" + day;
        
        // Check if the email already exists
        int numEmail = 0;
        for (String email: clientsMap.keySet()) {
        	if (clientEmailAddress.equals(email)) {
        		numEmail += 1;
        	}
        }
        if (clientFirstName.matches("")) {
            Toast.makeText(getApplicationContext(),"Please enter a first name.", 
            		Toast.LENGTH_LONG).show();
        } else if (clientLastName.matches("")) {
            Toast.makeText(getApplicationContext(),"Please enter a last name.", 
            		Toast.LENGTH_LONG).show();
        } else if (numEmail >= 1 && !clientEmailAddress.equals(clientEmail)) {
        	Toast.makeText(getApplicationContext(), "Email " 
        + clientEmailAddress + " already exists.", Toast.LENGTH_LONG).show();
        } else if (clientEmailAddress.matches("")) {
            Toast.makeText(getApplicationContext(),"Please enter an email "
            		+ "address.", Toast.LENGTH_LONG).show();
        } else if (clientAddress.matches("")) {
            Toast.makeText(getApplicationContext(),"Please enter an address.", 
            		Toast.LENGTH_LONG).show();
        } else if (clientCreditCard.matches("")) {
            Toast.makeText(getApplicationContext(),"Please enter a credit card "
            		+ "number.", 
            		Toast.LENGTH_LONG).show();
        } else {
        	client.setFirstName(clientFirstName);
        	client.setLastName(clientLastName);
        	client.setEmail(clientEmailAddress);
        	client.setAddress(clientAddress);
        	client.setCreditCardNumber(clientCreditCard);
            try {
            	client.setExpiryDate(clientCreditCardExpire);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
            saveData();
            Toast.makeText(getApplicationContext(),"Edited client's "
            		+ "information.", Toast.LENGTH_SHORT).show();
            
            // Update client map
            if (!clientEmailAddress.equals(clientEmail)) {
				clientsMap.put(clientEmailAddress, 
						clientsMap.get(clientEmail));
				clientsMap.remove(clientEmail);
			}
            
            Intent intent = new Intent(this, ViewClientInfoActivity.class);
            intent.putExtra("clientEmail", clientEmailAddress);
            startActivity(intent);
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
			dc.saveToFile(cOut, aOut, fOut, iOut);
			
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
