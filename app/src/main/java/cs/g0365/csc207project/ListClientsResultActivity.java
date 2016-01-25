package cs.g0365.csc207project;

import java.util.ArrayList;
import java.util.List;

import cs.g0365.csc207project.backend.Administrator;
import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Displays a list of all the clients and allows an administrator to select a
 * client to view their information.
 */
public class ListClientsResultActivity extends Activity {

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	/** The list view to display the clients. */
	private ListView lv;
	
	/** The adapter to customize the display of the clients. */
	private ClientAdapter clientAdapter;

	/** The list of all the clients. */
	private List<Client> clientResultsList;
	
	/** The current user of the application. */
	private User currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_clients_result);
        
        currentUser = dc.getCurrentUser();
		     
        // get the list view
        lv = (ListView) findViewById(R.id.client_results_list);

        // get the client in a list
        clientResultsList = new ArrayList<Client>(dc.getClients().values());

        // display the client in the listView
        clientAdapter = new ClientAdapter(this,
        		android.R.layout.simple_list_item_1, clientResultsList);

        lv.setAdapter(clientAdapter); 
        clientAdapter.notifyDataSetChanged();
        
        // action if a client is clicked
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
		        
		    	// display a confirmation dialog for the client
		    	if (currentUser instanceof Administrator) {
		    		confirm(position);
		    	}
		    	
			}
			
		});
	}
	
	/**
     * Displays a dialog for the admin to confirm to view client's information.
     * @param position the position of the selected itinerary in the list
     */
    private void confirm(int position) {

	    final Client client = 
	    		clientAdapter.getItem(position);
	    final int pos = position;
	    
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setMessage(client.getFirstName() + " " + client.getLastName())
    				.setTitle("View Client Information");

		builder.setPositiveButton("Confirm", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {

		       	    Client client = 
		       	    		clientAdapter.getItem(pos);
		       	    
		            
	    	    	Intent intent = 
	    	    			new Intent(ListClientsResultActivity.this, 
	    	    					ViewClientInfoActivity.class);
	    	    	
	    	    	intent.putExtra("clientEmail", 
	    	    			client.getEmail());
				    
	    	    	startActivity(intent);
		           }
		       });
		builder.setNegativeButton("Cancel", 
				new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // do nothing if the user pressed 'Cancel'
		       }
		   });

    	builder.create().show();
    }
    
	/**
	 * Sends the user back to the search client activity.
	 * @param view the current view
	 */
    public void backToSearch(View view) {
        Intent intent = new Intent(this, SearchClientActivity.class);
        startActivity(intent);
    }
    
}
