package model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents all the operations that can be performed on an Event.
 * These methods are package-private because Events should only be created
 * and modified through a Calendar.
 */
interface IEvent {
  /**
   * Returns a new event with the specified property updated to the given new property value.
   * This affects only the current event
   *
   * @param property         the name of the property to update (one of: "subject", "start", "end",
   *                         "description", "location", "status")
   * @param newPropertyValue the new value to set
   * @return a new Event with the updated property
   */
  Event updateEvent(String property, String newPropertyValue);

  /**
   * Updates the current event and all subsequent events in the series with the new property value.
   * Does not modify earlier events in the series.
   *
   * @param property         the name of the property to update
   * @param newPropertyValue the new value to set
   * @return the updated instance of this Event with the new series (with the updated properties)
   */
  Event updateRestOfSeries(String property, String newPropertyValue);

  /**
   * Updates all events in the series, including the current one, and past events with the
   * specified property value.
   *
   * @param property         the name of the property to update
   * @param newPropertyValue the new value to set
   * @return the updated instance of this Event with the fully updated series
   */
  Event updateFullSeries(String property, String newPropertyValue);

  /**
   * Returns the first event in this event's series. Returns null if this event is not part of
   * a series.
   *
   * @return the first event in the series, or null if this event is not part of a series
   */
  Event startOfSeries();

  /**
   * Returns the next event in the series after this one or returns null if this event is the
   * last event or is not part of a series.
   *
   * @return the next event in the series, or null if none
   */
  Event nextInSeries();

  /**
   * Determines whether this event includes the given time. The time is included if it is within
   * or equal to the start and end time.
   *
   * @param time the time to check as a LocalDateTime
   * @return true if the time falls within the event duration, false otherwise
   */
  boolean includes(LocalDateTime time);

  /**
   * Determines whether this event starts on or after the given start time and ends on or
   * before the given end time.
   *
   * @param startTime the beginning of the time range
   * @param endTime   the end of the time range
   * @return true if this event falls completely within the range, false otherwise
   */
  boolean isInRange(LocalDateTime startTime, LocalDateTime endTime);

  /**
   * Checks whether this event has the given property and value and is used to identify events
   * based on certain properties.
   *
   * @param id the entry representing a key-value pair of a property name and its value
   * @return true if the event has a matching property, false otherwise
   * @throws IllegalArgumentException if the property key is invalid
   */
  boolean hasProp(Map.Entry<String, String> id);

  /**
   * Returns this event as a schedule item represented by a map of its properties.
   * The map includes keys such as "event", "from", "to", "description", "location", and "status".
   *
   * @return a map representing this event's key properties
   */
  Map<String, String> asScheduleItem();

  /**
   * Determines if two Events are equal. Two events are equal if they have the same subject, start,
   * and end time.
   *
   * @param that the object to be compared
   * @return true if two Events are equal, false otherwise
   */
  @Override
  boolean equals(Object that);

  /**
   * Produces a unique hash code for an Event using the hash of its subject, start, and end time.
   *
   * @return a hash code for an Event object
   */
  @Override
  int hashCode();


}
