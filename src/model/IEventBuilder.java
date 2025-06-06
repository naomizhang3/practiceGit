package model;

/**
 * This interface represents all operations that a builder for single events or event series
 * can perform.
 *
 * @param <T> the type of builder to be returned for method chaining
 */
interface IEventBuilder<T> {

  /**
   * Sets the subject of the event using the given subject.
   *
   * @param subject the subject/title of the event
   * @return the builder object for method chaining
   */
  T subject(String subject);

  /**
   * Sets the start date and time of the event using the given string.
   * The string must be in the format: YYYY-MM-ddThh:mm in order to be parsed into a LocalDateTime.
   *
   * @param start the start time as a string
   * @return the builder object for method chaining
   */
  T start(String start);

  /**
   * Sets the end date and time of the event using the given string.
   * The string must be in the format: YYYY-MM-ddThh:mm in order to be parsed into a LocalDateTime.
   *
   * @param end the end time as a string
   * @return the builder object for method chaining
   */
  T end(String end);

  /**
   * Sets the event to be an all-day event on the specified date as a string, setting the event's
   * start and end date-times. The event will start at 8:00 am and end at 17:00 pm on that date.
   *
   * @param on the date of the event in the format: yyyy-MM-dd
   * @return the builder object for method chaining
   */
  T onTime(String on);

  /**
   * Sets the description of the event using the given description.
   *
   * @param description the description or notes for the event
   * @return the builder object for method chaining
   */
  T description(String description);

  /**
   * Sets the location of the event using a string.
   * The string is converted to an Event.Location enum using its assign method.
   *
   * @param location the location name as a string (either physical or online)
   * @return the builder object for method chaining
   */
  T location(String location);

  /**
   * Sets the status of the event using the given string.
   * The string is converted to an Event.EventStatus enum using its assign method.
   *
   * @param status the status of the event (either public or private)
   * @return the builder object for method chaining
   */
  T status(String status);

}
