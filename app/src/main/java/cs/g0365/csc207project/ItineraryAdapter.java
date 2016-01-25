package cs.g0365.csc207project;

import java.util.List;

import cs.g0365.csc207project.backend.Client;
import cs.g0365.csc207project.backend.DataController;
import cs.g0365.csc207project.backend.Flight;
import cs.g0365.csc207project.backend.Itinerary;
import cs.g0365.csc207project.backend.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Itinerary adapter to customize display of itineraries in a list view.
 * The Adapter provides access to the data items. The Adapter is also 
 * responsible for making a View for each item in the data set.
 */
public class ItineraryAdapter extends ArrayAdapter<Itinerary>  {

	/** The list of itineraries in the list view. */
	private List<Itinerary> itineraries;

	/** The DataController instance. */
	private DataController dc = DataController.getInstance();
	
	/** The current user of the application. */
	private User currentUser;
	
	/** The client selected to view the itineraries for. */
	private Client currentClient;

	/**
	 * Creates a new ItineraryAdapter. 
	 * @param context the current context
	 * @param resource the resource ID for a layout file containing a TextView 
	 * to use when instantiating views
	 * @param itineraries the itinerary objects to represent in the ListView
	 */
	public ItineraryAdapter(Context context, int resource, 
			List<Itinerary> itineraries) {
		super(context, resource, itineraries);
		this.itineraries = itineraries;
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
			v = inflater.inflate(R.layout.itinerary_item, null);
		}
		
		Itinerary itinerary = itineraries.get(position);

		if (itinerary != null) {
			
			TextView itinerary_number = 
					(TextView) v.findViewById(R.id.itinerary_number_field);
			TextView flight_list = (TextView) v.findViewById(R.id.flight_list);
			TextView cost = (TextView) v.findViewById(R.id.cost_field);
			TextView time = (TextView) v.findViewById(R.id.time_field);
			TextView booked = (TextView) v.findViewById(R.id.booked);
			
			List<Flight> flightList = itinerary.getFlights();
			String flightString = "";
			
			for (Flight flight:flightList) {
				flightString += flight.getFlightNumber() 
	            		+ ", " + flight.getDepartureDateTimeinString() 
	            		+ ", " + flight.getArrivalDateTimeinString() 
	            		+ ", " + flight.getAirline() + ", " + flight.getOrigin() 
	            		+ ", " + flight.getDestination()
	            		+ ", " + flight.getNumSeats() + " seats\n";
			}
			
			flightString = flightString.substring(0, flightString.length() - 1);

			// assign the itinerary information to the appropriate TextView
			if (itinerary_number != null){
				itinerary_number.setText(itinerary.getItineraryNumber());
			}
			if (flight_list != null){
				flight_list.setText(flightString);
			}
			if (cost != null){
				cost.setText("$" + itinerary.getOverallCostInString());
			}
			if (time != null){
				time.setText(itinerary.getOverallTimeInStr());
			}
			if (booked != null){
				currentUser = dc.getCurrentUser();
				
		    	if (currentUser instanceof Client) {
					currentClient = (Client) DataController.getInstance().
							getCurrentUser();
					if (currentClient.containsItinerary(itinerary)) {
						booked.setText("Booked");
					} else {
						booked.setText("");
					}
		    	} else {
					booked.setText("");
		    	}
		    	
			}
		}

		// return the view to the activity
		return v;

	}

}
