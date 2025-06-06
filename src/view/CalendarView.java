package view;

import java.util.List;
import java.util.Map;

/**
 * Defines operations for displaying data to a calendar's user interface.
 */
public interface CalendarView {

  /**
   * Display a welcome message like would be seen when the user boots up the calendar program.
   */
  void welcome();

  /**
   * Display the options menu.
   */
  void showMenu();

  /**
   * Print the given message to the user, such as a notification or warning/error message.
   * @param arg     The String message to be shown.
   */
  void write(String arg);

  /**
   * Format and display a schedule given as a list of maps. The exact makeup of these maps is
   * determined by the calendar implementation being used, as is the way they should be read.
   * @param events    A list of events in which each event is represented as one map, with its
   *                  properties listed as the keys and their details as the values.
   */
  void formatSchedule(List<Map<String, String>> events);
}
