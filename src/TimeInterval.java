
/**
* Time Interval class to store start and end date and start and end time
* @author Rabia Mohiuddin
* @version 1.0 2/16/19 
* */

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeInterval implements Comparable<TimeInterval> {
	LocalDate startDate;
	LocalDate endDate;
	LocalTime startTime;
	LocalTime endTime;

	/**
	 * Constructs a time interval for one time event with start and end time.
	 * 
	 * @param date
	 *            - the date of the event
	 * @param startTime
	 *            - time of the start of event
	 * @param endTime
	 *            - time of the start of event precondition: endTime must be
	 *            after the startTime
	 */
	public TimeInterval(LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.startDate = this.endDate = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Constructs a time interval for a regular event with start date, end date,
	 * start time, and end time.
	 * 
	 * @param startDate
	 *            - the start date of the event
	 * @param endDate
	 *            - the end date of the event
	 * @param startTime
	 *            - time of the start of event
	 * @param endTime
	 *            - time of the start of event precondition: endDate must be
	 *            after startDate and endTime must be after the startTime
	 */
	public TimeInterval(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Retrieves start date as LocalDate object
	 * 
	 * @return start date (LocalDate) of Time interval
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Retrieves end date as LocalDate object
	 * 
	 * @return end date (LocalDate) of Time interval
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Retrieves start time as LocalTime object
	 * 
	 * @return start time (LocalTime) of Time interval
	 */
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * Retrieves end time as LocalTime object
	 * 
	 * @return end time (LocalTime) of Time interval
	 */
	public LocalTime getEndTime() {
		return endTime;
	}

	/**
	 * Compares two TimeInterval objects and returns whether or not they overlap
	 * 
	 * @param TimeInterval
	 *            object, precondition - TimeInterval fields can not be null
	 * @return true if the two objects overlap, false otherwise
	 */
	public boolean doesOverlap(TimeInterval timeInterval) {
		// If the two time intervals are on the same day, check if one start
		// time is before the other TimeInterval end time
		if (this.getStartDate().equals(timeInterval.getStartDate())) {
			return this.getStartTime().isBefore(timeInterval.getEndTime()) && timeInterval.getStartTime().isBefore(this.getEndTime());
		}
		// False if not on the same day
		return false;
	}

	/**
	 * Overriden compareTo method that compares their start times
	 * 
	 * @param other
	 *            TimeInterval, precondition - TimeInterval fields can not be
	 *            null
	 * @return Negative is this object's start time is before other's start
	 *         time, positive if after other's, equal if same
	 */
	@Override
	public int compareTo(TimeInterval other) {
		return this.getStartTime().compareTo(other.getStartTime());
	}
}
