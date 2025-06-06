package view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A text UI for a calendar, matching the CalendarModelImpl style of events.
 */
public class CalendarViewImpl implements CalendarView {

  private final Appendable output;

  /**
   * Construct a new calendar view with an output source that can be appended onto.
   * @param output    The text-based output stream that this view should print to.
   */
  public CalendarViewImpl(Appendable output) {
    this.output = output;
  }

  /**
   * Print a welcome message, then display the options menu.
   */
  public void welcome() {
    this.write("Hello, welcome to the calendar app." + System.lineSeparator());
    this.showMenu();
  }

  /**
   * Write the given message to the output source.
   * @param s                         The String message to be shown.
   * @throws IllegalStateException    Throws when the output operation fails.
   */
  public void write(String s) throws IllegalStateException {
    try {
      output.append(s);
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  /**
   * Print the given schedule as a bulleted list, with each event comprising one bullet,
   * using CalendarModelImpl-style key values.
   * @param events    A list of events in which each event is represented as one map, with its
   *                  properties listed as the keys and their details as the values.
   */
  public void formatSchedule(List<Map<String, String>> events) {
    for (Map<String, String> event : events) {
      StringBuilder eventString = new StringBuilder();
      eventString.append("* ").append(event.get("event")).append(" : ");
      eventString.append("from ").append(event.get("from"))
              .append(" to ").append(event.get("to"));

      if (event.get("description") != null) {
        eventString.append(" Details: ").append(event.get("description"));
      }

      if (event.get("location") != null) {
        eventString.append(" @ ").append(event.get("location"));
      }

      if (event.get("status") != null) {
        eventString.append(" is ").append(event.get("status"));
      }
      eventString.append(System.lineSeparator());
      this.write(eventString.toString());
    }
  }

  /**
   * Print the options menu containing commands for this program.
   */
  public void showMenu() {
    write("Your options are:"
            + System.lineSeparator() +
            System.lineSeparator() +
            "create event <eventSubject> from <dateTime> to <dateTime> "
            + System.lineSeparator() +
            "-  creates an event with the given subject within the given time range. subject must "
            + "be contained in quotes if longer than one word"
            + System.lineSeparator() +
            "create event <eventSubject> from <dateTime> to <dateTime> repeats <weekdays> for <n> "
            + "times"
            + System.lineSeparator() +
            "-  creates an event like above, repeating on the given days for N weeks" +
            System.lineSeparator() +
            "create event <eventSubject> from <dateTime> to <dateTime> repeats <weekdays> until " +
            "<date>"
            + System.lineSeparator() +
            "-  creates an event like above, repeating on the given days until the given date" +
            System.lineSeparator() +
            "create event <eventSubject> on <date>"
            + System.lineSeparator() +
            "-  creates an all-day event from 8am-5pm, which can also repeat using the above " +
            "syntax"
            + System.lineSeparator()
            + System.lineSeparator() +
            "optional additions to any command:"
            + System.lineSeparator() +
            "description <description>"
            + System.lineSeparator() +
            "-  descriptions longer than one word must be contained in quotes" +
            System.lineSeparator() +
            "location <location>"
            + System.lineSeparator() +
            "-  can be one of: physical or online"
            + System.lineSeparator() +
            "status <status>"
            + System.lineSeparator() +
            "-  can be one of: public or private"
            + System.lineSeparator() +
            System.lineSeparator() +
            "edit event <property> <eventSubject> from <dateTime> to <dateTime> with " +
            "<newPropertyValue>"
            + System.lineSeparator() +
            "-  edits the given property (one of subject, start, end, description, location, " +
            "status) of the event specified"
            + System.lineSeparator() +
            "   by its subject, start and end times to have the new property value." +
            System.lineSeparator() +
            "edit events <property> <eventSubject> from <dateTime> with <newPropertyValue>" +
            System.lineSeparator() +
            "-  edits this event or this and the following events in a series as above, " +
            "starting at the given date and time, to have the new property value"
            + System.lineSeparator() +
            "edit series <property> <eventSubject> from <dateTime> with <newPropertyValue>" +
            System.lineSeparator() +
            "-  edits this event or the entire series this event is a part of to have the given " +
            "property value"
            + System.lineSeparator() +
            System.lineSeparator() +
            "print events on <date>"
            + System.lineSeparator() +
            "-  prints a bulleted schedule containing all events on this day" +
            System.lineSeparator() +
            "print events from <dateTime> to <dateTime>"
            + System.lineSeparator() +
            "-  prints a bulleted schedule containing all events within the given time interval" +
            System.lineSeparator() +
            "show status on <dateTime>"
            + System.lineSeparator() +
            "-  prints busy or available based on whether the user has any events at this date " +
            "and time"
            + System.lineSeparator()
            + System.lineSeparator()
            + "menu"
            + System.lineSeparator()
            + "-  print this menu"
            + System.lineSeparator()
            + "q or quit"
            + System.lineSeparator()
            + "-  quit the calendar program"
            + System.lineSeparator()
            + System.lineSeparator()
            + "Enter an instruction: "
            + System.lineSeparator());
  }

}
