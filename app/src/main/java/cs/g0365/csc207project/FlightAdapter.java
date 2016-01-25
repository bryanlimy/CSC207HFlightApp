package cs.g0365.csc207project;

import java.util.List;

import cs.g0365.csc207project.backend.Flight;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Flight Adapter to customize display of flights in list view.
 * The Adapter provides access to the data items. The Adapter is also 
 * responsible for making a View for each item in the data set.
 */
public class FlightAdapter extends ArrayAdapter<Flight>  {

	/** The list of flights in the list view. */
	private List<Flight> flights;
	
	/**
	 * Creates a new FlightAdapter. 
	 * @param context the current context
	 * @param resource the resource ID for a layout file containing a TextView 
	 * to use when instantiating views
	 * @param flights the flight objects to represent in the ListView
	 */
	public FlightAdapter(Context context, int resource, List<Flight> flights) {
		super(context, resource, flights);
		this.flights = flights;
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
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		// render the view if it is null
		if (v == null) {
			LayoutInflater inflater = 
					(LayoutInflater) getContext().
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.flight_item, null);
		}
		
		Flight flight = flights.get(position);

		if (flight != null) {
			
			TextView flightNum = 
					(TextView) v.findViewById(R.id.flight_number_field);
			TextView origin = (TextView) v.findViewById(R.id.origin_field);
			TextView destination = 
					(TextView) v.findViewById(R.id.destination_field);
			TextView departure = 
					(TextView) v.findViewById(R.id.departure_field);
			TextView arrival = (TextView) v.findViewById(R.id.arrival_field);
			TextView airline = (TextView) v.findViewById(R.id.airline_field);
			TextView cost = (TextView) v.findViewById(R.id.cost_field);
			TextView time = (TextView) v.findViewById(R.id.time_field);
			TextView numSeats = (TextView) v.findViewById(R.id.num_seats);

			// assign the flight information to the appropriate TextView
			if (flightNum != null){
				flightNum.setText(flight.getFlightNumber());
			}
			if (origin != null){
				origin.setText(flight.getOrigin());
			}
			if (destination != null){
				destination.setText(flight.getDestination());
			}
			if (departure != null){
				departure.setText(flight.getDepartureDateTimeinString());
			}
			if (arrival != null){
				arrival.setText(flight.getArrivalDateTimeinString());
			}
			if (airline != null){
				airline.setText(flight.getAirline());
			}
			if (cost != null){
				cost.setText("$" + flight.getPrice());
			}
			if (time != null){
				time.setText(flight.getTravelTime());
			}
			if (numSeats != null){
				numSeats.setText(flight.getNumSeats() + " seats");
			}
		}

		// return the view to the activity
		return v;

	}

}
