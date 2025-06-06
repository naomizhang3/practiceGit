package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an abstract event builder class with all operations needed to build an event.
 *
 * @param <T> the generic type that must be of the type AbstractEventBuilder
 */
public abstract class AbstractEventBuilder<T extends AbstractEventBuilder<T>>
        implements IEventBuilder<AbstractEventBuilder<T>> {

  protected String subject;

  protected LocalDateTime start;

  protected LocalDateTime end;

  protected String description;

  protected Event.Location location;

  protected Event.EventStatus status;

  /**
   * Constructs an AbstractEventBuilder object with the specified subject. The subject cannot be
   * empty or null.
   *
   * @param subject the subject of the event to be built as a string
   * @throws NullPointerException if the given subject is null
   */
  public AbstractEventBuilder(String subject) throws NullPointerException {

    this.subject = Objects.requireNonNull(subject, "Events require a subject.");
    this.start = null;
    this.end = null;
    this.description = null;
    this.location = null;
    this.status = null;
  }

  @Override
  public T subject(String subject) {
    this.subject = subject;
    return this.returnBuilder();
  }

  @Override
  public T start(String start) {
    if (start != null) {
      this.start = LocalDateTime.parse(start);
    }
    return this.returnBuilder();
  }

  @Override
  public T end(String end) {
    if (end != null) {
      this.end = LocalDateTime.parse(end);
    }
    return this.returnBuilder();
  }

  @Override
  public T onTime(String on) {
    if (on != null) {
      this.start = LocalDate.parse(on).atTime(8, 0);
      this.end = LocalDate.parse(on).atTime(17, 0);
    }
    return this.returnBuilder();
  }

  @Override
  public T description(String description) {
    this.description = description;
    return this.returnBuilder();
  }

  @Override
  public T location(String location) {
    if (location != null) {
      this.location = Event.Location.assign(location);
    }
    return this.returnBuilder();
  }

  @Override
  public T status(String status) {
    if (status != null) {
      this.status = Event.EventStatus.assign(status);
    }
    return this.returnBuilder();
  }

  /**
   * Returns the current builder instance (part of the builder pattern) and must be overridden
   * in each concrete subclass.
   *
   * @return the current builder instance of type T
   */
  protected abstract T returnBuilder();

}
