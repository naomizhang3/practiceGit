package model;

import java.util.List;
import java.util.Map;

/**
 * This interface represents all the operations a Calendar model can perform, including creating,
 * editing, querying events, and checking availability.
 */
public interface CalendarModel {

  /**
   * Creates an event (single or series) based on the given properties.
   * The map should include required keys such as "subject", "start", and potentially "end".
   *
   * @param properties a map of property names to values used to construct the event.
   *                   Required fields depend on event type and could include:
   *                   "subject", "start", "end", "location", "description",
   *                   "status", "weekdays", "nTimes", or "until".
   * @throws IllegalArgumentException if required properties are missing or invalid
   */
  void createEvent(Map<String, String> properties);

  /**
   * Edits a specified property of an event to the new property value, identified by a combination
   * of properties.
   *
   * @param eventType        the type of event to edit, can be "event", "events", or "series"
   * @param property         the property to change (such as "location", "description")
   * @param identifier       a map of identifying properties for the event
   * @param newPropertyValue the new value to set for the specified property
   * @throws IllegalArgumentException if the event cannot be identified or the change is invalid
   */
  void editEvent(String eventType, String property, Map<String, String> identifier,
                 String newPropertyValue);

  /**
   * Retrieves all events on or within the provided time range as a List of Maps which contain
   * the key and value for each property of a found event.
   *
   * @param start the start date-time of the desired time frame as a String in the format:
   *              YYYY-MM-DDThh:mm
   * @param end   the start date-time of the desired time frame as a String in the format:
   *              YYYY-MM-DDThh:mm
   * @return a List of Maps containing all properties of all events found within the given range
   */
  List<Map<String, String>> getSchedule(String start, String end);

  /**
   * Retrieves all events on the given day as a List of Maps which contain the key and value for
   * each property of a found event.
   *
   * @param day the day to query all events on as a String in the format: YYYY-MM-DD
   * @return a List of Maps containing all properties of all events found on the given day
   */
  List<Map<String, String>> getSchedule(String day);

  /**
   * Retrieves the status of the user on the given day and time. The user is busy if there are any
   * events that start/end or overlap on the specified time. The user if available otherwise.
   *
   * @param dateTime the specified date and time to query the calendar for events
   * @return "busy" if user has any events scheduled on the given date-time, "available" otherwise
   */
  String getStatus(String dateTime);

}
