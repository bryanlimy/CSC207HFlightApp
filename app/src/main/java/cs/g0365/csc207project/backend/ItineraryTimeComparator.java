package cs.g0365.csc207project.backend;

import java.util.Comparator;

/**
 * A comparator class that is used in sorting List of Itinerary,
 * which compares Itinerary by time.
 */
public class ItineraryTimeComparator implements Comparator<Itinerary>{

	@Override
	public int compare(Itinerary o1, Itinerary o2) {
		return Integer.valueOf(o1.getOverallTimeInMins())
			.compareTo(Integer.valueOf(o2.getOverallTimeInMins()));
	}

}
