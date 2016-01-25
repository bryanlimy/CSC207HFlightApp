package cs.g0365.csc207project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.InvalidFileException;
import cs.g0365.csc207project.backend.LoginAuthenticator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Displays login screen and authenticates the user's login information.
 */
public class LoginActivity extends Activity {

    /** The path to the passwords file */
	private static final String PASSWORDS_FILE = "passwords.txt";

    /** The DataController instance */
	private DataController dc = DataController.getInstance();

	/** The LoginAuthenticator instance. */
	private LoginAuthenticator la;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        
		initData();
		initAuthenticator();
		
	}
	
	/**
	 * Loads the information from the passwords file into the LoginAuthenticator
	 * object.
	 */
	private void initAuthenticator() {
    	String dir = this.getApplicationContext().getFilesDir().
    			getAbsolutePath();
        File pwdFile = new File(dir, PASSWORDS_FILE);
        try {
			la = new LoginAuthenticator(new FileInputStream(pwdFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFileException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads any serialized data and uploads the default client and admin
	 * information.
	 */
	private void initData(){
		
		// load the serialized data
		loadData();
		
        // upload the data from default_clients.csv and admins.csv
		try {
			uploadData();
		} catch (InvalidFileException e) {
			Toast.makeText(getApplicationContext(),"Unable to read file.", 
					Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			new AlertDialog.Builder(this)
		    .setTitle("Error")
		    .setMessage("Please push the required files to the internal "
		    		+ "storage. Read the README file for instructions.")
		    .setPositiveButton(android.R.string.yes, 
		    		new DialogInterface.OnClickListener() {
		        @Override
				public void onClick(DialogInterface dialog, int which) { 
		            // nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}
	}
	
	/**
	 * Validates the user's login and sends them to the next activity if they
	 * successfully login.
	 * @param view the current view
	 */
	public void login(View view){
		
		EditText emailField = (EditText) findViewById(R.id.email_field);
	    String email = emailField.getText().toString();
	    
	    EditText pwdField = (EditText) findViewById(R.id.password_field);
	    String pwd = pwdField.getText().toString();

	    if(la != null && la.auth(email, pwd)){
	    	
	    	if (dc.getCurrentUser() instanceof Administrator){
	            Intent intent = new Intent(this, AdministratorActivity.class);
	            
	            startActivity(intent);
	    	}
	    	else{
	            Intent intent = new Intent(this, ClientActivity.class);
	            startActivity(intent);
	    	}
	    }
	    else{
			new AlertDialog.Builder(this)
		    .setTitle("Error")
		    .setMessage("Wrong Login/Password")
		    .setPositiveButton(android.R.string.yes, 
		    		new DialogInterface.OnClickListener() {
		        @Override
				public void onClick(DialogInterface dialog, int which) { 
		            // nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
	    }
	    
	}
		
    /**
     * Reads data from the serialized files and deserializes the clients, 
     * flights, and itineraries Maps.
     */
	private void loadData() {
        
		String clientPath = this.getApplicationContext().getFilesDir() 
				+ "/clients.ser";
		String adminPath = this.getApplicationContext().getFilesDir() 
				+ "/admins.ser";
		String flightPath = this.getApplicationContext().getFilesDir() 
				+ "/flights.ser";
		String itineraryPath = this.getApplicationContext().getFilesDir() 
				+ "/itineraries.ser";
		dc.readFromFile(clientPath, adminPath, flightPath, itineraryPath);
		
	}
	
	/**
	 * Uploads the csv data from the internal storage.
     * @throws InvalidFileException when the given file is in invalid format
	 * @throws FileNotFoundException when the file is not found
	 */
	private void uploadData() 
			throws InvalidFileException, FileNotFoundException {
        
		String clientPath = this.getApplicationContext().getFilesDir() 
				+ "/default_clients.csv";
		String adminPath = this.getApplicationContext().getFilesDir() 
				+ "/admins.csv";

		dc.uploadAdminInfo(adminPath);
		
		try {
			dc.uploadClientInfo(clientPath);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		saveData();
		
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
    
    /**
     * Do nothing when pressing the "Back" button.
     */
    @Override
    public void onBackPressed() {
    }
	
}
