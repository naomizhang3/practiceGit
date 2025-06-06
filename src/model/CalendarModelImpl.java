package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents one implementation of the CalendarModel which can create, edit, query
 * events and show user status.
 */
public class CalendarModelImpl implements CalendarModel {

  private final Set<Event> events;

  /**
   * Constructs an empty CalendarModelImpl, initializing events as a new TreeSet.
   */
  public CalendarModelImpl() {
    this.events = new TreeSet<Event>();
  }

  @Override
  public void createEvent(Map<String, String> properties) throws NullPointerException {

    if (properties.containsKey("repeats") || properties.containsKey("for")
            || properties.containsKey("until")) {
      Event.EventSeries series = new Event.EventSeries.EventSeriesBuilder(properties.get("event"),
              properties.get("repeats"))
              .start(properties.get("from"))
              .end(properties.get("to"))
              .onTime(properties.get("on"))
              .description(properties.get("description"))
              .location(properties.get("location"))
              .status(properties.get("status"))
              .nTimes(properties.get("for"))
              .until(properties.get("until")).buildSeries();

      for (Event e : series.asList()) {
        this.addEvent(e);
      }
      return;
    }

    Event event = new Event.EventBuilder(properties.get("event"))
            .start(properties.get("from"))
            .end(properties.get("to"))
            .onTime(properties.get("on"))
            .description(properties.get("description"))
            .location(properties.get("location"))
            .status(properties.get("status")).build();

    this.addEvent(event);
  }

  /**
   * Adds the given event to this Calendar's set of events.
   * @param event the Event to add to this Calendar
   * @throws IllegalArgumentException if the event to be added already exists in this calendar
   */
  private void addEvent(Event event) throws IllegalArgumentException {
    if (!this.events.add(event)) {
      throw new IllegalArgumentException("A duplicate event was found to an event in this " +
              "calendar. (shares the same subject and date/time frame.)");
    }
  }

  @Override
  public void editEvent(String eventType, String property, Map<String, String> identifier,
                        String newPropertyValue) {
    List<Event> filtered = new ArrayList<>(this.events);

    for (Map.Entry<String, String> id : identifier.entrySet()) {
      for (Event e : this.events) {
        if (!e.hasProp(id)) {
          filtered.remove(e);
        }
      }
    }

    if (filtered.size() != 1) {
      throw new IllegalArgumentException("Identify exactly one event in this calendar to edit " +
              "it. The given identifying properties were either too broad or did not match any " +
              "event.");
    }

    switch (eventType.toLowerCase()) {
      case "event":
        Event baseEvent = filtered.get(0);
        this.events.remove(filtered.get(0));
        Event editedEvent = filtered.get(0).updateEvent(property, newPropertyValue);
        this.events.add(editedEvent);
        break;
      case "events":
        Event toRemove = filtered.get(0);
        while (toRemove != null) {
          this.events.remove(toRemove);
          toRemove = toRemove.nextInSeries();
        }
        Event editedEvents = filtered.get(0).updateRestOfSeries(property, newPropertyValue);
        while (editedEvents != null) {
          this.events.add(editedEvents);
          editedEvents = editedEvents.nextInSeries();
        }
        break;
      case "series":
        Event toRemoveInSeries = filtered.get(0).startOfSeries();
        while (toRemoveInSeries != null) {
          this.events.remove(toRemoveInSeries);
          toRemoveInSeries = toRemoveInSeries.nextInSeries();
        }
        Event editedSeries = filtered.get(0).updateFullSeries(property, newPropertyValue);
        while (editedSeries != null) {
          this.events.add(editedSeries);
          editedSeries = editedSeries.nextInSeries();
        }
        break;
      default:
        throw new IllegalArgumentException("Edit commands must specify whether they should be " +
                "executed on one event, multiple events, or an entire series.");
    }
  }

  @Override
  public List<Map<String, String>> getSchedule(String start, String end) {
    LocalDateTime startTime = LocalDateTime.parse(start);
    LocalDateTime endTime = LocalDateTime.parse(end);
    List<Map<String, String>> schedule = new ArrayList<>();

    if (startTime.isAfter(endTime)) {
      throw new IllegalArgumentException("Start time cannot be after end time.");
    }

    for (Event e : this.events) {
      if (e.includes(startTime)
              || e.includes(endTime)
              || e.isInRange(startTime, endTime)) {
        schedule.add(e.asScheduleItem());
      }
    }

    return schedule;
  }

  @Override
  public List<Map<String, String>> getSchedule(String day) {
    LocalDateTime start = LocalDate.parse(day).atTime(0, 0);
    LocalDateTime end = LocalDate.parse(day).atTime(23, 59);
    return this.getSchedule(start.toString(), end.toString());
  }

  @Override
  public String getStatus(String dateTime) {
    LocalDateTime time = LocalDateTime.parse(dateTime);
    for (Event e : this.events) {
      if (e.includes(time)) {
        return Availability.BUSY.toString();
      }
    }
    return Availability.AVAILABLE.toString();
  }

  /**
   * An enumeration representing the possible statuses of a user: busy or available.
   */
  private enum Availability {
    BUSY("busy"), AVAILABLE("available");

    private final String msg;

    /**
     * Constructs an Availability enum with the given string.
     *
     * @param msg the string to be turned into an Availability enum
     */
    Availability(String msg) {
      this.msg = msg;
    }

    @Override
    public String toString() {
      return this.msg;
    }
  }
}
