package cs.g0365.csc207project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.InvalidFileException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Upload clients' information.
 */
public class UploadClientInfoActivity extends Activity {
	
	/** The instance of this data. */
	private DataController datacontroller = DataController.getInstance();
	
	/** The progress bar of the upload progress. */
	private ProgressBar progress;
	
	/** The spinner that shows all csv files in the internal storage folder.*/
	private Spinner spinner;
	
	/** The file path to this application's root folder. */
	private String applicationPath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_client_info);
		applicationPath = this.getApplicationContext().getFilesDir().
				getAbsolutePath();
		progress = (ProgressBar)findViewById(R.id.progressBar2);
		progress.setVisibility(View.GONE);
		
		loadSpinner();
	}
	
	/**
	 * Loads the csv files in the internal storage into the spinner and uploads
	 * information from a selected file.
	 */
	public void loadSpinner() {
		spinner = (Spinner) findViewById(R.id.uploadClientSpinner);
		List<String> list = new ArrayList<String>();
		list.add("Select a client file");
		for (String file: this.getApplicationContext().getFilesDir().list()) {
			if (file.toLowerCase().endsWith(".csv") || 
					file.toLowerCase().endsWith(".txt")) {
				list.add(file);
			}
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
		(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.
				simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(new 
				AdapterView.OnItemSelectedListener() {

            @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
            	String flightFile = spinner.getSelectedItem().toString();
            	
            	if (!flightFile.equals("Select a client file")) {
            		String uploadFlightPath = applicationPath 
            				+ "/" + flightFile;
                	uploadClient(uploadFlightPath);
            	}
            }
            @Override
			public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
	}
	
	/**
	 * Uploads client information from the user's typed path.
	 * @param view the current view
	 */
	public void uploadClientText(View view) {
		// Gets the data from the EditText field.
		EditText uploadClientField = (EditText) findViewById(R.id.
				upload_client_field);
		String uploadClientPath = this.getApplicationContext().getFilesDir().
				getAbsolutePath() + "/" + uploadClientField.getText().
				toString();
		uploadClient(uploadClientPath);
	}
	
	/**
	 * Uploads flight information from the given path.
	 * If the given file is not found, user is asked to re-enter the file path.
	 * @param uploadClientPath the path to the csv file
	 */
	public void uploadClient(String uploadClientPath) {
		
		// Gets the data from the EditText field.
		progress.setVisibility(View.VISIBLE);
		EditText uploadClientField = (EditText) findViewById(R.id.
				upload_client_field);
		
		File file = new File(uploadClientPath);
		if (file.exists()) {
			try {
				datacontroller.uploadClientInfo(uploadClientPath);
				Toast.makeText(getApplicationContext(),"Client info uploaded.", 
						Toast.LENGTH_SHORT).show();
		        saveData();
				
		        // Return to Administrator activity after uploading flight info
				Intent intent = new Intent(this, AdministratorActivity.class);
				startActivity(intent);
			} catch (InvalidFileException e) {
				Toast.makeText(getApplicationContext(),"Invalid File Format. "
						+ "Please re-enter file path.", Toast.LENGTH_LONG).
						show();
				uploadClientField.setText("");
				progress.setVisibility(View.GONE);
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(),"Invalid Clients format"
						+". Please re-enter file path.", Toast.LENGTH_LONG).
						show();
				uploadClientField.setText("");
				progress.setVisibility(View.GONE);
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(),"File Not Found. Please"
						+ " re-enter file path.", Toast.LENGTH_LONG).show();
				uploadClientField.setText("");
				progress.setVisibility(View.GONE);
			}
		} else {
			Toast.makeText(getApplicationContext(),"File Not Found. Please "
					+ "re-enter file path.", Toast.LENGTH_LONG).show();
			uploadClientField.setText("");
			progress.setVisibility(View.GONE);
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
			datacontroller.saveToFile(cOut, aOut, fOut, iOut);
			
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
