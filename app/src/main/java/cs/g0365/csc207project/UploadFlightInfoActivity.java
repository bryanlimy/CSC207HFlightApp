package cs.g0365.csc207project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.InvalidFileException;
import cs.g0365.csc207project.backend.InvalidFlightInfoException;
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
 * Upload flights' information.
 */
public class UploadFlightInfoActivity extends Activity {
	
	/** The DataController instance. */
	private DataController datacontroller = DataController.getInstance();
	
	/** The progress bar of this upload progress. */
	private ProgressBar progress;

	/** The spinner that shows all csv files in the internal storage folder.*/
	private Spinner spinner;
	
	/** The file path to this application's root folder. */
	private String applicationPath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_flight_info);
		applicationPath = this.getApplicationContext().getFilesDir().
				getAbsolutePath();
		progress = (ProgressBar) findViewById(R.id.progressBar1);
		progress.setVisibility(View.GONE);
		loadSpinner();
	}
	
	/**
	 * Loads the csv files in the internal storage into the spinner and uploads
	 * information from a selected file.
	 */
	protected void loadSpinner() {
		spinner = (Spinner) findViewById(R.id.flightSpinner);
		List<String> list = new ArrayList<String>();
		list.add("Select a flight file");
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
            	
            	if (!flightFile.equals("Select a flight file")) {
            		String uploadFlightPath = applicationPath 
            				+ "/" + flightFile;
                	uploadFlight(uploadFlightPath);
            	}
            }
            @Override
			public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
		
	}
	
	/**
	 * Uploads flight information from the user's typed path.
	 * @param view the current view
	 */
	public void uploadFlightsText(View view) {
		// Gets the data from the EditText field.
		EditText uploadFlightField = 
				(EditText) findViewById(R.id.upload_flight_field);
		String uploadFlightPath = applicationPath + "/" 
		+ uploadFlightField.getText().toString();
		uploadFlight(uploadFlightPath);
	}
	
	/**
	 * Uploads flight information from the given path.
	 * If the given file is not found, or file format is incorrect, or the
	 * flight format is incorrect, user is asked to re-enter the file path.
	 * @param uploadFlightPath the path to the csv file
	 */
	protected void uploadFlight(String uploadFlightPath){
		progress.setVisibility(View.VISIBLE);
		EditText uploadFlightField = 
				(EditText) findViewById(R.id.upload_flight_field);
	
		File file = new File(uploadFlightPath);
		if (file.exists()) {
			try {
				datacontroller.uploadFlightInfo(uploadFlightPath);
				Toast.makeText(getApplicationContext(),"Flight info uploaded.", 
						Toast.LENGTH_SHORT).show();
		        saveData();
		        
		        // Return to Administrator activity after uploading flight info
				Intent intent = new Intent(this, AdministratorActivity.class);
				startActivity(intent);
			} catch (InvalidFileException e) {
				Toast.makeText(getApplicationContext(),"Invalid File Format. "
						+ "Please re-enter file path.", 
						Toast.LENGTH_LONG).show();
				uploadFlightField.setText("");
				progress.setVisibility(View.GONE);
			} catch (InvalidFlightInfoException e) {
				Toast.makeText(getApplicationContext(),"Invalid Flights Format."
						+ " Please re-enter file path.", 
						Toast.LENGTH_LONG).show();
				uploadFlightField.setText("");
				progress.setVisibility(View.GONE);
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(),"File Not Found. Please "
						+ "re-enter file path.", 
						Toast.LENGTH_LONG).show();
				uploadFlightField.setText("");
				progress.setVisibility(View.GONE);
			}

		} else {
			Toast.makeText(getApplicationContext(),"File Not Found. Please "
					+ "re-enter file path.", Toast.LENGTH_LONG).show();
			uploadFlightField.setText("");
			progress.setVisibility(View.GONE);
		}
	}
	
    /**
     * Writes the clients, flights, and itineraries Maps to files.
     */
	protected void saveData() {
        
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
