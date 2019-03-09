
/**
* MyCalendar class to provide Calendar functionality such as move, add, and delete
* @author Rabia Mohiuddin
* @version 1.0 2/16/19 
* */

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class MyCalendar {
	// Sorted HashMap to store LocalDate as key, and ArrayList of Events as
	// value
	TreeMap<LocalDate, ArrayList<Event>> events = new TreeMap<LocalDate, ArrayList<Event>>();
	// Store where the calendar is currently at during user interaction
	LocalDate curCalAt = LocalDate.now();

	/**
	 * Prints all events currently in the calendar
	 */
	public void printEventList() {
		// Format in 'Day, Month Date'
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("EEEE, MMMM d ");
		// Print year of first event in calendar
		int year = events.firstKey().getYear();
		System.out.println(year);
		// For every entry in the TreeMap
		for (Entry<LocalDate, ArrayList<Event>> entry : events.entrySet()) {
			// Sort the Events ArrayList
			Collections.sort(entry.getValue());
			// If the year of the current event is not the one that has already
			// been printed, print
			if (entry.getKey().getYear() != year) {
				year = entry.getKey().getYear();
				System.out.println(year);
			}
			// Print event data for all in the Events ArrayList
			for (Event e : entry.getValue()) {
				System.out.print("    " + dateformatter.format(entry.getKey()) + " ");
				e.printEvent();
			}
		}
		System.out.println();
	}

	/**
	 * Retrieves the date at which the calendar is currently at
	 * 
	 * @return current date (LocalDate) of Calendar
	 */
	public LocalDate getCurCalAt() {
		return curCalAt;
	}

	/**
	 * Sets the current calendar to a specified date
	 * 
	 * @param date
	 *            - date (LocalDate) want Calendar to be moved to
	 */
	public void setCurCalAt(LocalDate curCalAt) {
		this.curCalAt = curCalAt;
	}

	/**
	 * Retrieved regular event specific dates for their given date range
	 * 
	 * @param days
	 *            - string of days of event (e.g. 'MT' Monday, Tuesday),
	 *            startDate - start date (LocalDate) of regular event, endDate -
	 *            end date (LocalDate) of regular event
	 * @return ArrayList of LocalDate objects in which the regular event occurs
	 */
	public ArrayList<LocalDate> getSpecificDates(String days, LocalDate startDate, LocalDate endDate) {
		ArrayList<DayOfWeek> daysNeeded = new ArrayList<DayOfWeek>();
		// Add the correct day to the ArrayList based on days String given
		for (char day : days.toCharArray()) {
			if (day == 'M') {
				daysNeeded.add(DayOfWeek.MONDAY);
			} else if (day == 'T') {
				daysNeeded.add(DayOfWeek.TUESDAY);
			} else if (day == 'W') {
				daysNeeded.add(DayOfWeek.WEDNESDAY);
			} else if (day == 'R') {
				daysNeeded.add(DayOfWeek.THURSDAY);
			} else if (day == 'F') {
				daysNeeded.add(DayOfWeek.FRIDAY);
			} else if (day == 'S') {
				daysNeeded.add(DayOfWeek.SATURDAY);
			} else if (day == 'U') {
				daysNeeded.add(DayOfWeek.SUNDAY);
			}
		}

		// Find the dates corresponding to the days needed and add to ArrayList
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		// Iterate through date range to find days given
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
			if (daysNeeded.contains(d.getDayOfWeek())) {
				dates.add(d);
			}
		}
		return dates;
	}

	/**
	 * Adds a one time event to the TreeMap events as long as does not overlap
	 * with other Event objects
	 * 
	 * @param e
	 *            - Event object want to add
	 */
	public void addOneTime(Event e) {
		LocalDate start = e.getTime().getStartDate();
		// Check if the event overlaps with any other events
		if (!doesOverlap(start, e)) {
			// If there are no events in that date, add a new ArrayList of
			// Events
			events.putIfAbsent(start, new ArrayList<Event>());
			events.get(start).add(e);
			System.out.println("Event " + e.getName() + " has been added!");
		} else {
			System.out.println("Time conflict! Could not add " + e.getName());

		}
	}

	/**
	 * Adds a regular event to the TreeMap events by getting specific dates and
	 * adding Event to all those dates
	 * 
	 * @param e
	 *            - Event object want to add, days - String of which days want
	 *            to be added
	 */
	public void addRegular(Event e, String days) {
		// Get the specific dates between the date range
		ArrayList<LocalDate> dates = getSpecificDates(days, e.getTime().getStartDate(), e.getTime().getEndDate());
		for (LocalDate date : dates) {
			// Check if date overlaps with any on that day and add
			if (!doesOverlap(date, e)) {
				// If there are no events in that date, add a new ArrayList of
				// Events
				events.putIfAbsent(date, new ArrayList<Event>());
				events.get(date).add(e);
			}
		}
	}

	/**
	 * Checks with the Event does overlap function to see if they overlap
	 * 
	 * @param date
	 *            - date want to add event (LocalDate), potentialEvent - Event
	 *            object want to add
	 * @return boolean of whether it overlaps or not
	 */
	public boolean doesOverlap(LocalDate date, Event potentialEvent) {
		ArrayList<Event> dayEvents = events.get(date);
		if (dayEvents != null) { // There are events on that day
			for (Event event : dayEvents) {
				// Call event doesOverlap method
				if (event.doesOverlap(potentialEvent) == true) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes an event on a specific date given the name of the Event
	 * 
	 * @param date
	 *            - date want to remove event (LocalDate), name - Event name
	 * @throws IllegalArgumentException
	 *             if Event is not in Calendar
	 */
	public void remove(LocalDate date, String name) {
		Event found;
		int size = events.get(date).size();
		// Iterates through ArrayList of events on that day and finds the name
		// of wanted event
		for (int i = 0; i < size; i++) {
			found = events.get(date).get(i);
			// If find the event with name given, remove from ArrayList
			if (found.sameEvent(name) == true) {
				events.get(date).remove(i);
				break;
			}
		}
		// If the size of the ArrayList did not decrease, event was not removed
		// because does not exist
		if (events.get(date).size() == size) {
			throw new IllegalArgumentException("Event not in calendar");
		}
	}

	/**
	 * Removes all events on a given date
	 * 
	 * @param date
	 *            - date want to remove events (LocalDate)
	 */
	public void removeAll(LocalDate date) {
		// Clear all events on given date
		if (events.get(date) != null) {
			events.get(date).clear();
		}
	}

	/**
	 * Advances Calendar by amount of given months
	 * 
	 * @param numberOfMonths
	 *            - number of months want to move Calendar by
	 */
	public void advanceByMonth(int numberOfMonths) {
		curCalAt = curCalAt.plusMonths(numberOfMonths);
	}

	/**
	 * Advances Calendar by amount of given days
	 * 
	 * @param numberOfDays
	 *            - number of days want to move Calendar by
	 */
	public void advanceByDay(int numberOfDays) {
		curCalAt = curCalAt.plusDays(numberOfDays);
	}

	/**
	 * Prints a monthly calendar in correct format
	 */
	public void printMonthCalendar() {
		// Print Month and Year
		String month = curCalAt.getMonth().toString();
		month = month.charAt(0) + month.toLowerCase().substring(1, month.length());
		System.out.println("  " + month + " " + curCalAt.getYear());
		// Print Days of Week
		System.out.println("Su Mo Tu We Th Fr Sa");
		LocalDate x = LocalDate.of(curCalAt.getYear(), curCalAt.getMonth(), 1);
		String firstWeekSpace = "";
		// Add space depending on what day the 1st falls on
		for (int i = 0; i < x.getDayOfWeek().getValue(); i++) {
			firstWeekSpace += "   ";
		}
		System.out.print(firstWeekSpace);

		// Print every date in the month, today indicated in { }
		for (int i = 0; i < curCalAt.lengthOfMonth(); i++) {
			if (x.equals(LocalDate.now())) {
				System.out.printf("{%2s} ", x.getDayOfMonth());
			} else {
				System.out.printf("%2s ", x.getDayOfMonth());
			}
			// Go to next day
			x = x.plusDays(1);
			if (x.getDayOfWeek() == DayOfWeek.SUNDAY) {
				System.out.print("\n");
			}
		}

		System.out.println();
	}

	/**
	 * Prints events on current day the calendar is at
	 * 
	 */
	public void printDayCalendar() {
		// Formats Day in wanted format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d, yyyy");
		System.out.println(" " + formatter.format(curCalAt));
		// Get events at current day
		ArrayList<Event> dayEvents = events.get(curCalAt);
		if (dayEvents != null) {
			// Sort events in order of time
			Collections.sort(dayEvents);
			// Print event details
			for (Event event : dayEvents) {
				event.printEvent();
			}
		}
		System.out.println();
	}
}
