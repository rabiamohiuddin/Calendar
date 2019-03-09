
/**
* MyCalendarTester class that provides user interface options
* @author Rabia Mohiuddin
* @version 1.0 2/16/19 
* */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MyCalendarTester {
	public static void main(String[] args) {
		MyCalendar calendar = new MyCalendar();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
		loadEvents(calendar, formatter);
		calendar.printMonthCalendar();
		Scanner sc = new Scanner(System.in);
		String strDate = null;
		LocalDate date = null;

		String input = "";
		// While the user does not want to quit
		while (!input.equals("Q")) {
			System.out.println("\nSelect one of the following options:");
			// Display options
			System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
			input = sc.nextLine().toUpperCase(); // Read user input
			switch (input) { // Switch based which function user wants to
								// complete
			case "V": // View
				calendar.setCurCalAt(LocalDate.now());
				System.out.println("[D]ay view or [M]view ?"); // Ask user if
																// want to see
																// calendar by
																// Day or Month
				String view = sc.nextLine().toUpperCase(); // Read user input
				System.out.println();
				String option = "";
				int val = 0; // Amount to advance calendar by
				while (!option.equals("G")) {
					if (view.equals("D")) { // If selected Day view
						calendar.advanceByDay(val); // Advance calendar day
													// by
													// value
						calendar.printDayCalendar(); // Print Day Calendar
					} else if (view.equals("M")) { // If selected Month view
						calendar.advanceByMonth(val); // Advance calendar
														// month
														// by value
						calendar.printMonthCalendar(); // Print Month
														// Calendar
					} else {
						try {
							throw new Exception("Input not recognized. Try again");
						} catch (Exception e) {
							System.out.println(e.getMessage() + " - Invalid date");
						}
					}
					System.out.println("\n[P]revious or [N]ext or [G]o back to main menu ?");
					option = sc.nextLine().toUpperCase(); // Read user input
					if (option.equals("P")) { // If selected Previous
						val = -1; // Go backwards in calendar
					} else if (option.equals("N")) { // If selected Next
						val = 1; // Go forwards in calendar
					} else if (!option.equals("G")) { // If type anything
														// else,
														// try again
						try {
							throw new Exception("Input not recognized. Try again");
						} catch (Exception e) {
							System.out.println(e.getMessage() + " - Invalid date");
						}
					}

				}

				break;
			case "C": // Create Event
				System.out.println("Create a One Time Event!");
				System.out.println("Name of Event: ");
				String name = sc.nextLine();

				try {
					// Get date of event want to create
					System.out.println("Date of Event in MM/DD/YYYY: ");
					strDate = sc.nextLine();
					formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
					date = LocalDate.parse(strDate, formatter);
					formatter = DateTimeFormatter.ofPattern("H:mm");
					// Try to get start and end time in correct format
					System.out.println("Start Time of Event in 24 hr clock [format H:M]: ");
					String startTime = sc.nextLine();
					System.out.println("End Time of Event in 24 hr clock [format H:M]: ");
					String endTime = sc.nextLine();
					// Add one time event to the calendar
					calendar.addOneTime(new Event(name, date, date, LocalTime.parse(startTime, formatter), LocalTime.parse(endTime, formatter), EventType.ONETIME));
					// Catch any DateTimeFormatter exceptions
				} catch (Exception e) {
					System.out.println(e.getMessage() + " - Invalid date");
				}

				break;
			case "G": // Go to
				System.out.println("Enter a date in the form of MM/DD/YYYY to see events occuring on that day:");
				strDate = sc.nextLine();
				formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
				try {
					// Try to get date in correct format
					date = LocalDate.parse(strDate, formatter);
					calendar.setCurCalAt(date);
					calendar.printDayCalendar();
					// Catch any DateTimeFormatter exceptions
				} catch (Exception e) {
					System.out.println(e.getMessage() + " - Invalid date");
				}

				break;
			case "E": // Event list
				calendar.printEventList();
				break;
			case "D": // Delete
				System.out.println("Delete");
				// Give option to delete selected event or all
				System.out.println("\n[S]elected or [A]ll ? ");
				option = sc.nextLine().toUpperCase(); // Read user input
				System.out.println("Date of Event in MM/DD/YYYY: ");
				strDate = sc.nextLine();
				formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
				try {
					// Try to format given date
					date = LocalDate.parse(strDate, formatter);
					calendar.setCurCalAt(date);
					if (option.equals("S")) { // If selected event
						calendar.printDayCalendar();
						System.out.print("Enter name of Event to delete: ");
						String evName = sc.nextLine();
						try {
							// Try to delete event if in the calendar
							calendar.remove(date, evName);
							System.out.println("Event Deleted. Currently scheduled events:");
							// Print new calendar
							calendar.printDayCalendar();
						} catch (Exception e) {
							System.out.println(e.getMessage() + " Unable to delete specified event. Please try again");
						}
					} else if (option.equals("A")) { // If selected All events
						// Remove all events on given date
						calendar.removeAll(date);
						calendar.printDayCalendar();
						System.out.println("All Events Deleted.");
					}
				// Catch any deletion or formatting exceptions
				} catch (Exception e) {
					System.out.println(e.getMessage() + " - Invalid date");
				}

				break;
			case "Q": // Quit
				System.out.println("Quit");
				break;
			default:
				System.out.println("Input not recognized. Try again");
				break;
			}
		}

		System.out.println("Bye!");
		// Print calendar to 'output.txt'
		exportEvents(calendar);
		sc.close();
	}

	/**
	 * Loads events into calendar given calendar object and DateTimeFormatter
	 * 
	 * @param calendar
	 *            - MyCalendar object, formatter - DateTimeFormatter events will
	 *            be read with precondition - Events should be loaded in file
	 *            'events.txt'
	 * @throws FileNotFoundException,
	 *             IOException
	 */
	public static void loadEvents(MyCalendar calendar, DateTimeFormatter formatter) {
		String file = "events.txt";
		// Read in file
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			// Read file line by line
			while ((line = br.readLine()) != null) {
				// First line contains name
				String name = line;
				line = br.readLine();
				char firstChar = line.charAt(0);
				String elems[] = line.split("\\s+");
				// If line starts with letters, indicates regular event
				if (firstChar >= 'A' && firstChar <= 'Z') {
					// Split line by white space and parse into respective
					// elements
					formatter = DateTimeFormatter.ofPattern("M/d/yy");
					LocalDate startDate = LocalDate.parse(elems[3], formatter);
					LocalDate endDate = LocalDate.parse(elems[4], formatter);
					formatter = DateTimeFormatter.ofPattern("H:mm");
					// Add event to the calendar
					calendar.addRegular(new Event(name, startDate, endDate, LocalTime.parse(elems[1], formatter), LocalTime.parse(elems[2], formatter), EventType.REGULAR),
							elems[0]);
				} else {
					// One Time event
					formatter = DateTimeFormatter.ofPattern("M/d/yy");
					LocalDate startDate, endDate;
					startDate = endDate = LocalDate.parse(elems[0], formatter);
					formatter = DateTimeFormatter.ofPattern("H:mm");
					// Add event to the calendar
					calendar.addOneTime(new Event(name, startDate, endDate, LocalTime.parse(elems[1], formatter), LocalTime.parse(elems[2], formatter), EventType.ONETIME));
				}
			}
			// If file not found, throw exception
		} catch (FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
		}
	}

	/**
	 * export events from calendar to 'output.txt'
	 * 
	 * @param calendar
	 *            - MyCalendar object
	 * @throws FileNotFoundException
	 */
	public static void exportEvents(MyCalendar calendar) {
		// Creating a File object that represents the disk file.
		PrintStream printer = null;
		try {
			printer = new PrintStream(new File("output.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error " + e.getMessage());
		}

		// Assign printer to output stream
		System.setOut(printer);
		calendar.printEventList();
	}
}
