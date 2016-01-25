package cs.g0365.csc207project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Searches for a client, displaying their personal and billing information.
 */
public class SearchClientActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();

	/** The spinner to display the clients' emails. */
	private Spinner spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_client);
		
		loadEmailSpinner();
	}
	
	/**
	 * Loads the clients' emails into the spinner.
	 */
	private void loadEmailSpinner() {
		spinner = (Spinner) findViewById(R.id.client_email_spinner);
		List<String> emailList = new ArrayList<String>();
		Map<String, Client> clients = dc.getClients();
		
		emailList.add("Select a client's email");
		
		for (String email: clients.keySet()) {
			emailList.add(email);
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
		(this,android.R.layout.simple_spinner_item, emailList);
		dataAdapter.setDropDownViewResource(android.R.layout.
				simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(new 
				AdapterView.OnItemSelectedListener() {

            @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
            	String spinnerEmail = spinner.getSelectedItem().toString();
            	
            	if (!spinnerEmail.equals("Select a client's email")) {
                	Intent intent = new Intent(SearchClientActivity.this, 
                			ViewClientInfoActivity.class);
                	intent.putExtra("clientEmail", spinnerEmail);
                	startActivity(intent);
            	}

            }

            @Override
			public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
		
	}
	
	/**
	 * Reads the UI email field and searches for a client with the given email.
	 * If the given email is empty, shows all clients available in the system.
	 * If a client with the given email is found, shows the personal and billing
	 * information of the client.
	 * If no client is found, displays a message saying that no client was 
	 * found.
	 * @param view the current view
	 */
	public void searchClient(View view){
		
		// Input from EditText
        EditText editEmail = (EditText) findViewById(R.id.editClientEmailText);
        String textEmail = editEmail.getText().toString();
        
        // Input from Spinner
        Spinner spinner = (Spinner) findViewById(R.id.client_email_spinner);
        String spinnerEmail = spinner.getSelectedItem().toString();
        
        // If input from EditText is empty, use the selected email address
        // from spinner
        String email = null;
        if (!textEmail.matches("")) {
        	email = textEmail;
        } else {
        	email = spinnerEmail;
        }
        
        Client client = null;
        
        try {
        	client = dc.getClient(email);
        } catch(Exception e){
        	
        }
        
        if (client == null){
        	Toast.makeText(getApplicationContext(),"There is no client with the"
        			+ " given email.", Toast.LENGTH_LONG).show();
        }
        else {
        	Intent intent = new Intent(this, ViewClientInfoActivity.class);
        	intent.putExtra("clientEmail", email);
        	startActivity(intent);
        }
	}
    
    /**
	 * Displays activity responsible for showing all clients.
	 */
    public void viewAll(View view){
    	Intent intent = new Intent(this, ListClientsResultActivity.class);
    	startActivity(intent);
    }
	
	/**
	 * Returns the user to the AdministratorActivity when pressing "Back" 
	 * button.
	 */
    @Override
    public void onBackPressed() {
    	Intent intent = new Intent(this, AdministratorActivity.class);
    	startActivity(intent);
    }
    
}
