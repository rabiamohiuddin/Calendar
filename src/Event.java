
/**
* Event class to store Calendar Events including name, date, time, and event type
* @author Rabia Mohiuddin
* @version 1.0 2/16/19 
* */

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Enum defining EventType as ONETIME or REGULAR
 * 
 * @author Rabia Mohiuddin
 * @version 1.0 2/16/19
 */
enum EventType {
	ONETIME, REGULAR;
}

public class Event implements Comparable<Event> {
	String name;
	TimeInterval time;
	EventType etype;

	/**
	 * Event constructor that takes given fields and creates Event object
	 * 
	 * @param name
	 *            - string name of event object, startDate - start date of event
	 *            (LocalDate), endDate - end date of event (LocalDate),
	 *            startTime - start time of event(LocalTime), endTime - end time
	 *            of event (LocalTime), etype - event type
	 */
	public Event(String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, EventType etype) {
		this.name = name;
		this.time = new TimeInterval(startDate, endDate, startTime, endTime);
		this.etype = etype;
	}

	/**
	 * Retrieves name of Event object
	 * 
	 * @return name of Event (String)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves TimeInterval object of event
	 * 
	 * @return time interval of Event (TimeInterval)
	 */
	public TimeInterval getTime() {
		return time;
	}

	/**
	 * Retrieves EventType object of event
	 * 
	 * @return type of Event (EventType)
	 */
	public EventType getEType() {
		return etype;
	}

	/**
	 * Checks if two events are the same by comparing their names
	 * @param name - name of event wanting to compare
	 * @return boolean - true if same, false otherwise
	 */
	public boolean sameEvent(String name) {
		// Check if the two names are the same
		if (this.getName().equals(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Compares two events by comparing Time Interval objects
	 * @param other - Event object wanting to compare
	 * @return int defining if Event before or after this event
	 */
	@Override
	public int compareTo(Event other) {
		// Call time interval compares method
		return this.getTime().compareTo(other.getTime());
	}

	/**
	 * Checks if two events overlap
	 * @param other - Event object wanting to compare
	 * @return boolean defining if Event overlaps with other Event or not
	 */
	public boolean doesOverlap(Event other) {
		return this.getTime().doesOverlap(other.getTime());
	}

	/**
	 * Prints event properties such as start time, end time, and name
	 */
	public void printEvent() {
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("H:mm");
		System.out.println(timeformatter.format(getTime().getStartTime()) + " - " + timeformatter.format(getTime().getEndTime()) + " " + getName());

	}

}
