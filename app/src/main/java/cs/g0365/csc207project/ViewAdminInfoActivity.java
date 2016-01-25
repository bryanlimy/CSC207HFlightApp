package cs.g0365.csc207project;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.DataController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Displays the information of the current administrator.
 */
public class ViewAdminInfoActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();

	/** The current administrator. */
	private Administrator admin = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_admin_info);
		
		Intent intent = getIntent();
		
		String clientEmail = 
				(String) intent.getSerializableExtra("clientEmail");
		
		admin = (Administrator) dc.getUser(clientEmail);
		
		// Display admin's current first name
		TextView clientFirstNameField = (TextView) findViewById(R.id.
				first_name_label);
		clientFirstNameField.setText(admin.getFirstName());
		
		// Display admin's current last name
		TextView clientLastNameField = (TextView) findViewById(R.id.
				last_name_label);
		clientLastNameField.setText(admin.getLastName());
		
		// Display admin's current email address
		TextView clientEmailAddressField = 
				(TextView) findViewById(R.id.email_label);
		
		clientEmailAddressField.setText(admin.getEmail());
	}
	
}
