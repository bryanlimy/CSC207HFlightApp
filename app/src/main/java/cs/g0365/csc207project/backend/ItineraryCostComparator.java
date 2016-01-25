package cs.g0365.csc207project.backend;

import java.util.Comparator;

/**
 * A comparator class that is used in sorting List of Itinerary,
 * which compares Itinerary by cost.
 */
public class ItineraryCostComparator implements Comparator<Itinerary>{

	@Override
	public int compare(Itinerary o1, Itinerary o2) {
		return Double.valueOf(o1.getOverallCost())
				.compareTo(Double.valueOf(o2.getOverallCost()));
	}

}
