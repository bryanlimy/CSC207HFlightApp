package cs.g0365.csc207project;

import java.util.List;

import cs.g0365.csc207project.backend.Client;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Client Adapter to customize display of clients in list view.
 * The Adapter provides access to the data items. The Adapter is also 
 * responsible for making a View for each item in the data set.
 */
public class ClientAdapter extends ArrayAdapter<Client>  {

	/** The list of clients in the list view. */
	private List<Client> clients;
	
	/**
	 * Creates a new ClientAdapter. 
	 * @param context the current context
	 * @param resource the resource ID for a layout file containing a TextView 
	 * to use when instantiating views
	 * @param clients the client objects to represent in the ListView
	 */
	public ClientAdapter(Context context, int resource, 
			List<Client> clients) {
		super(context, resource, clients);
		this.clients = clients;
	}

	/**
	 * Get a View that displays the data at the specified position in the data 
	 * set.
	 * @param position the position of the item within the adapter's data set 
	 * of the item whose view we want
	 * @param convertView old view to reuse, if possible
	 * @param parent the parent that this view will eventually be attached to
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		View v = convertView;

		// render the view if it is null
		if (v == null) {
			LayoutInflater inflater = 
					(LayoutInflater) getContext().
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.client_item, null);
		}
		
		Client client = clients.get(position);

		if (client != null) {
			
			TextView email = 
					(TextView) v.findViewById(R.id.email_field);

			TextView firstName = 
					(TextView) v.findViewById(R.id.firstName_field);
			
			TextView lastName = 
					(TextView) v.findViewById(R.id.lastName_field);

			
			
			email.setText(client.getEmail());
			firstName.setText(client.getFirstName());
			lastName.setText(client.getLastName());
		}

		// return the view to the activity
		return v;

	}

}
