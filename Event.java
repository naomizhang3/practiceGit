package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents an Event that can be created and appear on a Calendar.
 * An Event must have a subject, start and end time, and can optionally include a description,
 * location, status, and a reference to an EventSeries if it is part of a recurring event. It must
 * implement Comparable to for the sorting of Events.
 */
class Event implements Comparable<Event>, IEvent {

  protected final String subject;

  protected final LocalDateTime start;

  protected final LocalDateTime end;

  protected final String description;

  protected final Location location;

  protected final EventStatus status;

  protected final EventSeries series;

  /**
   * Constructs an event with the given parameters.
   *
   * @param subject     the event subject
   * @param start       the start time
   * @param end         the end time
   * @param description the optional description
   * @param location    the optional location
   * @param status      the optional status
   * @param series      the series this event is a part of (if any)
   * @throws IllegalArgumentException if the end time is before the start time
   */

  Event(String subject, LocalDateTime start, LocalDateTime end,
        String description, Location location, EventStatus status, EventSeries series) {
    if (end.isBefore(start)) {
      throw new IllegalArgumentException("model.Event cannot end before it begins.");
    }
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.status = status;
    this.series = series;
  }

  /**
   * An event should come before another even if its start time comes before the other event's
   * start time.
   *
   * @param o the object to be compared.
   * @return a negative int if this event comes before the other event, 0 if equal, a positive int
   *     if this event comes after the other event.
   */
  @Override
  public int compareTo(Event o) {
    return this.start.compareTo(o.start);
  }

  @Override
  public boolean includes(LocalDateTime time) {
    return (this.start.isBefore(time) || this.start.isEqual(time))
            && (this.end.isAfter(time) || this.end.isEqual(time));
  }

  @Override
  public boolean hasProp(Map.Entry<String, String> id) {
    switch (id.getKey()) {
      case "event":
        return this.subject.equals(id.getValue());
      case "from":
        return this.start.isEqual(LocalDateTime.parse(id.getValue()));
      case "to":
        return this.end.isEqual(LocalDateTime.parse(id.getValue()));
      case "description":
        return this.description != null && this.description.equals(id.getValue());
      case "location":
        return this.location != null && this.location.equals(Location.assign(id.getValue()));
      case "status":
        return this.status != null && this.status.equals(EventStatus.assign(id.getValue()));
      default:
        throw new IllegalArgumentException("The given identifying property is not a property " +
                "of events.");
    }
  }

  @Override
  public Event nextInSeries() {
    if (this.series == null) {
      return null;
    }
    return this.series.getNext(this);
  }

  @Override
  public Event updateEvent(String property, String newPropertyValue) {
    return this.buildEdited(property, newPropertyValue).build();
  }

  @Override
  public Event updateRestOfSeries(String property, String newPropertyValue) {
    EventBuilder buildUpdatedThis = this.buildEdited(property, newPropertyValue);
    EventSeries updatedSeries = new EventSeries();
    Event updatedThis = buildUpdatedThis
            .series(updatedSeries)
            .build();
    updatedSeries.addEvent(updatedThis);

    Event oldNext = this.nextInSeries();
    while (oldNext != null) {
      Event next = oldNext.buildEdited(property, newPropertyValue)
              .series(updatedSeries)
              .build();
      updatedSeries.addEvent(next);
      Event toRemove = oldNext;
      oldNext = oldNext.nextInSeries();
      if (property.equals("start")) {
        toRemove.removeFromSeries();
      }

    }

    return updatedThis;
  }

  /**
   * Removes this event from its series.
   */
  private void removeFromSeries() {
    this.series.removeItem(this);
  }

  /**
   * Builds an EventBuilder pre-set with current properties and edits this Event's property to the
   * given new property value if it matches the given property.
   *
   * @param property         the property name to change ("subject", "start", "end", etc.)
   * @param newPropertyValue the new value to be assigned to the specified property
   * @return the EventBuilder that has built an edited Event
   */
  private EventBuilder buildEdited(String property, String newPropertyValue) {
    EventBuilder builder = new EventBuilder(this.subject)
            .start(this.start.toString())
            .end(this.end.toString())
            .description(this.description);
    if (this.location != null) {
      builder.location(this.location.toString());
    }
    if (this.status != null) {
      builder.status(this.status.toString());
    }

    switch (property) {
      // changed all of these from event, from, to -> subject, start, end
      case "subject":
        builder.subject(newPropertyValue);
        break;
      case "start":
        LocalDateTime oldStartDay;
        try {
          oldStartDay = LocalDateTime.parse(newPropertyValue);
        } catch (Exception e) {
          throw new IllegalArgumentException("New start time was not in a valid format.");
        }
        LocalDateTime newStartDay = oldStartDay.withDayOfYear(this.start.getDayOfYear());
        builder.start(newStartDay.toString());
        break;
      case "end":
        LocalDateTime oldEndDay;
        try {
          oldEndDay = LocalDateTime.parse(newPropertyValue);
        } catch (Exception e) {
          throw new IllegalArgumentException("New end time was not in a valid format.");
        }
        LocalDateTime newEndDay = oldEndDay.withDayOfYear(this.end.getDayOfYear());
        builder.end(newEndDay.toString());
        break;
      case "description":
        builder.description(newPropertyValue);
        break;
      case "location":
        builder.location(newPropertyValue);
        break;
      case "status":
        builder.status(newPropertyValue);
        break;
      default:
        throw new IllegalArgumentException("The property that is being edited in this event is " +
                "not recognized.");
    }

    return builder;
  }

  @Override
  public Event updateFullSeries(String property, String newPropertyValue) {
    Event first = this.startOfSeries();
    if (first == null) {
      return this.buildEdited(property, newPropertyValue).build();
    }
    return first.updateRestOfSeries(property, newPropertyValue);
  }

  @Override
  public Event startOfSeries() {
    try {
      return this.series.firstEvent();
    } catch (Exception e) {
      return this;
    }
  }

  @Override
  public boolean isInRange(LocalDateTime startTime, LocalDateTime endTime) {
    return (this.start.isAfter(startTime) || this.start.isEqual(startTime))
            && (this.end.isBefore(endTime) || this.end.isEqual(endTime));
  }

  @Override
  public Map<String, String> asScheduleItem() {
    Map<String, String> eventProperties = new HashMap<>();
    eventProperties.put("event", this.subject);
    eventProperties.put("from", this.start.toString());
    eventProperties.put("to", this.end.toString());
    eventProperties.put("description", this.description);
    if (this.location != null) {
      eventProperties.put("location", this.location.toString());
    }
    if (this.status != null) {
      eventProperties.put("status", this.status.toString());
    }
    return eventProperties;
  }

  /**
   * This inner class represents an EventBuilder, which helps build an event with many optional
   * properties.
   */
  static class EventBuilder extends AbstractEventBuilder<EventBuilder> {

    protected EventSeries series;

    /**
     * Constructs an EventBuilder with the given subject.
     *
     * @param subject the subject of the event
     */
    EventBuilder(String subject) {
      super(subject);
      this.series = null;
    }

    @Override
    protected EventBuilder returnBuilder() {
      return this;
    }

    /**
     * Sets the series of this event to reference the given event series, allowing the resulting
     * event to be part of a series.
     *
     * @param series the EventSeries to associate with this event
     * @return this EventBuilder with the series added
     */
    EventBuilder series(EventSeries series) {
      this.series = series;
      return this.returnBuilder();
    }

    /**
     * Builds a new Event object using the current values in this builder.
     * If the end time is not provided, it defaults to 5:00 pm on the same day
     * with the start time defaulting to 8:00 am if also not provided.
     *
     * @return a new Event object with the specified or defaulted properties
     * @throws IllegalArgumentException if the start time has not been set (is null)
     */
    Event build() {
      if (this.start == null) {
        throw new IllegalArgumentException("All events must have a start time.");
      }
      if (this.end == null) {
        this.start = this.start.withHour(8).withMinute(0);
        this.end = this.start.withHour(17).withMinute(0);
      }
      return new Event(this.subject, this.start, this.end, this.description,
              this.location, this.status, this.series);
    }
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (!(that instanceof Event)) {
      return false;
    }
    Event o = (Event) that;

    return (this.subject.equals(o.subject)
            && this.start.equals(o.start)
            && this.end.equals(o.end));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.subject, this.start, this.end);
  }

  /**
   * This enumeration represents the options of an events Location property. A Location can be
   * either physical or online.
   */
  protected enum Location {
    PHYSICAL("physical"), ONLINE("online");

    private final String value;

    /**
     * Constructs a Location enum with the given string as the value.
     *
     * @param value the string to be turned into a Location enum
     */
    Location(String value) {
      this.value = value;
    }

    /**
     * Assigns the given string to its corresponding enum.
     *
     * @param in a string to be represented as a location
     * @return a Location enum equivalent of the given location string
     * @throws IllegalArgumentException if the given location was not a valid location
     *                                  (physical or online)
     */
    protected static Location assign(String in) throws IllegalArgumentException {
      switch (in.toLowerCase()) {
        case "physical":
          return PHYSICAL;
        case "online":
          return ONLINE;
        default:
          throw new IllegalArgumentException("Given location was not one of physical or online.");
      }
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /**
   * This enumeration represents the possible statuses of an event. An EventStatus is can be
   * either public or private.
   */
  protected enum EventStatus {
    PUBLIC("public"), PRIVATE("private");

    private final String value;

    /**
     * Constructs an EventStatus enum with the given string as the value.
     *
     * @param value the string to be turned into an EventStatus enum
     */
    EventStatus(String value) {
      this.value = value;
    }

    /**
     * Assigns the given string to its corresponding EventStatus enum.
     *
     * @param in a string to be represented as an EventStatus
     * @return an EventStatus equivalent of the given string status
     * @throws IllegalArgumentException if the given string was not a valid status
     *                                  (public or private0
     */
    protected static EventStatus assign(String in) throws IllegalArgumentException {
      switch (in.toLowerCase()) {
        case "public":
          return PUBLIC;
        case "private":
          return PRIVATE;
        default:
          throw new IllegalArgumentException("Given status was not one of public or private.");
      }
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /**
   * This class represents an EventSeries, which is a recurring event. An EventSeries stores
   * a list of all occurrences of this recurring event.
   */
  protected static class EventSeries {

    private final List<Event> occurrences;

    /**
     * Constructs an EventSeries with multiple recurring events using the given properties.
     * The recurrence pattern is defined either by a specific number of times or an end date.
     *
     * <p>Each event in the series may not span multiple days. The series will repeat on the
     * specified days of the week, starting from the initial date.</p>
     *
     * @param subject     the title of the event
     * @param start       the start date and time of the first event
     * @param end         the end date and time of the first event (must be on
     *                    the same day as start)
     * @param description the optional description of the event
     * @param location    the optional location of the event
     * @param status      the optional visibility status of the event
     *                    (public/private)
     * @param weekdays    a string containing characters representing repeat
     *                    days (e.g. 'MWF')
     * @param nTimes      number of times the event should occur, null if until
     *                    is used
     * @param until       the final date to repeat until, null if nTimes is used
     * @throws IllegalArgumentException if the event spans multiple days, no repeat weekdays are
     *                                  specified,
     *                                  or neither {@code nTimes} nor {@code until} is provided
     */

    protected EventSeries(String subject, LocalDateTime start, LocalDateTime end,
                          String description, Location location, EventStatus status,
                          String weekdays, Integer nTimes, LocalDate until)
            throws IllegalArgumentException {

      if (!start.toLocalDate().isEqual(end.toLocalDate())) {
        throw new IllegalArgumentException("A single event in a series cannot span more than " +
                "one day.");
      }

      List<DayOfWeek> days = new ArrayList<>();
      if (weekdays == null) {
        throw new IllegalArgumentException("Must specify what days to repeat this event.");
      }
      for (char c : weekdays.toUpperCase().toCharArray()) {
        days.add(assignDayOfWeek(c));
      }

      if (nTimes != null) {
        this.occurrences = this.addNOccurrences(days, subject, start, end, description,
                location, status, nTimes);
      } else if (until != null) {
        this.occurrences = this.addOccurencesUntil(days, subject, start, end, description,
                location, status, until);
      } else {
        throw new IllegalArgumentException("Must specify the duration that this event should " +
                "repeat for.");
      }
    }

    /**
     * Constructs an empty event series where there are currently no occurrences of this repeating
     * Event.
     */
    protected EventSeries() {
      this.occurrences = new ArrayList<>();
    }

    /**
     * Creates and returns a list of recurring Event objects that repeat on the specified days
     * of the week starting from the given start date until the specified end date.
     *
     * <p>The first event is added if its start date falls on a valid repeating weekday. Then, the
     * following events are added by checking one day forward and including only those that match
     * the provided repeat days.</p>
     *
     * @param days        the list of DayOfWeek enums on which the event should repeat
     * @param subject     the event subject
     * @param start       the start date and time of the first event
     * @param end         the end date and time of the first event
     * @param description the optional description of the event
     * @param location    the optional location of the event
     * @param status      the optional visibility status of the event
     * @param until       the date up to which event should repeat
     */
    private List<Event> addOccurencesUntil(List<DayOfWeek> days, String subject,
                                           LocalDateTime start, LocalDateTime end,
                                           String description,
                                           Location location, EventStatus status, LocalDate until) {

      List<Event> occurrences = new ArrayList<>();
      if (days.contains(start.getDayOfWeek())) {
        Event todaysEvent = new Event(subject, start, end, description,
                location, status, this);
        occurrences.add(todaysEvent);
      }

      LocalDateTime currentStart = start;
      LocalDateTime currentEnd = end;

      while (currentStart.toLocalDate().isBefore(until)) {
        currentStart = currentStart.plusDays(1);
        currentEnd = currentEnd.plusDays(1);

        if (days.contains(currentStart.getDayOfWeek())) {
          occurrences.add(new Event(subject, currentStart, currentEnd, description,
                  location, status, this));
        }
      }
      return occurrences;
    }

    /**
     * CHANGED THIS METHOD TO NOT DO BY WEEK
     * Creates and returns a list of recurring Events that repeat on the specified days of the week,
     * for a given number of total occurrences.
     *
     * <p>The method starts from the given start date and checks each subsequent day to see if the
     * event should be scheduled on that date based on the specified repeating weekdays. The
     * process continues until the desired number of total events are added.</p>
     *
     * @param days        the list of DayOfWeek enums on which the event should repeat
     * @param subject     the event subject
     * @param start       the start date and time of the first event
     * @param end         the end date and time of the first event
     * @param description the optional description of the event
     * @param location    the optional location of the event
     * @param status      the optional visibility status of the event
     * @param nTimes      the number of total occurrences of the event (must be >= 1)
     * @return a list of recurring events up to the specified number of times
     * @throws IllegalArgumentException if nTimes is less than 1
     */
    private List<Event> addNOccurrences(List<DayOfWeek> days, String subject, LocalDateTime start,
                                        LocalDateTime end, String description,
                                        Location location, EventStatus status, Integer nTimes) {
      if (nTimes == null || nTimes < 1) {
        throw new IllegalArgumentException("Must specify that this event occurs more than once.");
      }

      List<Event> occurrences = new ArrayList<>();
      LocalDateTime currentStart = start;
      LocalDateTime currentEnd = end;

      while (occurrences.size() < nTimes) {
        if (days.contains(currentStart.getDayOfWeek())) {
          occurrences.add(new Event(subject, currentStart, currentEnd, description,
                  location, status, this));
        }

        currentStart = currentStart.plusDays(1);
        currentEnd = currentEnd.plusDays(1);
      }

      return occurrences;
    }

    /**
     * Retrieves the next Event in this EventSeries, if it exists. Otherwise, returns null.
     *
     * @param event the next event in this EventSeries.
     * @return the next Event in this series, or null if it does not exist
     */
    private Event getNext(Event event) {
      if (this.occurrences.contains(event)) {
        try {
          return this.occurrences.get(occurrences.indexOf(event) + 1);
        } catch (Exception e) {
          return null;
        }
      }
      throw new IllegalArgumentException("getNext of EventSeries is trying to find the " +
              "neighbor of an event that is not in this series.");
    }

    /**
     * Adds the given updated Event to this EventSeries' list of occurrences.
     *
     * @param updatedEvent the event to be added, which contains updated properties
     */
    private void addEvent(Event updatedEvent) {
      this.occurrences.add(updatedEvent);
    }

    /**
     * Retrieves the first event of this EventSeries.
     *
     * @return the first Event of this EventSeries.
     */
    private Event firstEvent() {
      return this.occurrences.get(0);
    }

    /**
     * Removes the given event from this series.
     *
     * @param event The event to remove.
     */
    private void removeItem(Event event) {
      this.occurrences.remove(event);
    }

    /**
     * This inner class represents an EvenSeriesBuilder which helps build an EventSeries that can
     * either repeat n times or until a specified date.
     */
    protected static class EventSeriesBuilder extends AbstractEventBuilder<EventSeriesBuilder> {

      private final String weekdays;

      private Integer nTimes;

      private LocalDate until;

      /**
       * Constructs an EventSeriesBuilder for building recurring event series.
       *
       * @param subject  the title or subject of the recurring event
       * @param weekdays the string representing the days of the week to repeat on (e.g. "TR")
       */
      protected EventSeriesBuilder(String subject, String weekdays) {
        super(subject);
        this.weekdays = weekdays;
        this.nTimes = null;
        this.until = null;
      }

      /**
       * Specifies the number of times the event should occur and sets until to null because the
       * user can only choose to repeat for nTimes or until a date, not both.
       *
       * @param nTimes the number of occurrences as a string
       * @return this builder with the updated number of occurrences
       * @throws NumberFormatException if nTimes is not a valid integer
       */
      protected EventSeriesBuilder nTimes(String nTimes) {
        if (nTimes != null) {
          this.nTimes = Integer.parseInt(nTimes);
          this.until = null;
        }
        return this.returnBuilder();
      }

      /**
       * Specifies the end date up to which the event should repeat and sets until to null because
       * the user can only choose to repeat for nTimes or until a date, not both.
       *
       * @param until the final date as a string (e.g. "2025-06-04")
       * @return this builder with the updated end date
       */
      protected EventSeriesBuilder until(String until) {
        if (until != null) {
          this.until = LocalDate.parse(until);
          this.nTimes = null;
        }
        return this.returnBuilder();
      }

      /**
       * Builds and returns the EventSeries instance with the set properties.
       *
       * @return the constructed recurring event series
       * @throws IllegalArgumentException if the event configuration is invalid (spans multiple
       *                                  days, missing repeat days, or does not have a repeat
       *                                  duration (nTimes or until)
       */
      protected EventSeries buildSeries() {
        return new EventSeries(this.subject, this.start, this.end, this.description,
                this.location, this.status, this.weekdays, this.nTimes, this.until);
      }

      @Override
      protected EventSeriesBuilder returnBuilder() {
        return this;
      }
    }

    /**
     * Assigns the given character to a certain DayOfWeek enum.
     *
     * @param c the given weekday character (MTWRFSU) to be evaluated
     * @return the DayOfWeek enum equivalent to the given character, otherwise throws an error.
     * @throws IllegalArgumentException if given character is not a valid day of week abbreviation.
     */
    private DayOfWeek assignDayOfWeek(char c) {
      switch (c) {
        case 'M':
          return DayOfWeek.MONDAY;
        case 'T':
          return DayOfWeek.TUESDAY;
        case 'W':
          return DayOfWeek.WEDNESDAY;
        case 'R':
          return DayOfWeek.THURSDAY;
        case 'F':
          return DayOfWeek.FRIDAY;
        case 'S':
          return DayOfWeek.SATURDAY;
        case 'U':
          return DayOfWeek.SUNDAY;
        default:
          throw new IllegalArgumentException("Invalid day monogram.");
      }
    }

    /**
     * Retrieves a new list containing all the occurrences of an event in this series.
     *
     * @return a new list of all occurrences of this event
     */
    List<Event> asList() {
      return new ArrayList<>(this.occurrences);
    }
  }
}
