import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import view.CalendarView;
import view.CalendarViewImpl;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the view package.
 */
public class TestCalendarView {
  CalendarView view;
  Appendable output;

  @Before
  public void setup() {
    output = new StringBuilder();
    view = new CalendarViewImpl(output);
  }

  /**
   * The welcome method should print a greeting followed by the options menu.
   */
  @Test
  public void testWelcome() {
    view.welcome();
    assertEquals("Hello, welcome to the calendar app."
                    + System.lineSeparator()
                    + "Your options are:"
                    + System.lineSeparator()
                    + System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> "
                    + System.lineSeparator()
                    + "-  creates an event with the given subject " +
                    "within the given time range. subject must "
                    + "be contained in quotes if longer than one word"
                    + System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> repeats " +
                    "<weekdays> for <n> "
                    + "times"
                    + System.lineSeparator() +
                    "-  creates an event like above, repeating on the given days for N weeks" +
                    System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> repeats " +
                    "<weekdays> until " +
                    "<date>"
                    + System.lineSeparator() +
                    "-  creates an event like above, repeating on the given days until the " +
                    "given date" +
                    System.lineSeparator() +
                    "create event <eventSubject> on <date>"
                    + System.lineSeparator() +
                    "-  creates an all-day event from 8am-5pm, which can also repeat using " +
                    "the above syntax"
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
                    "-  edits the given property (one of subject, start, end, description, " +
                    "location, " +
                    "status) of the event specified"
                    + System.lineSeparator() +
                    "   by its subject, start and end times to have the new property value." +
                    System.lineSeparator() +
                    "edit events <property> <eventSubject> from <dateTime> with " +
                    "<newPropertyValue>" +
                    System.lineSeparator() +
                    "-  edits this event or this and the following events in a series as above, " +
                    "starting at the given date and time, to have the new property value"
                    + System.lineSeparator() +
                    "edit series <property> <eventSubject> from <dateTime> with " +
                    "<newPropertyValue>" +
                    System.lineSeparator() +
                    "-  edits this event or the entire series this event is a part of to have " +
                    "the given " +
                    "property value"
                    + System.lineSeparator() +
                    System.lineSeparator() +
                    "print events on <date>"
                    + System.lineSeparator() +
                    "-  prints a bulleted schedule containing all events on this day" +
                    System.lineSeparator() +
                    "print events from <dateTime> to <dateTime>" + System.lineSeparator() +
                    "-  prints a bulleted schedule containing all events within the given time " +
                    "interval" +
                    System.lineSeparator()
                    + "show status on <dateTime>"
                    + System.lineSeparator() +
                    "-  prints busy or available based on whether the user has any events at " +
                    "this date and " +
                    "time"
                    + System.lineSeparator()
                    + System.lineSeparator()
                    + "menu" + System.lineSeparator()
                    + "-  print this menu" + System.lineSeparator()
                    + "q or quit" + System.lineSeparator()
                    + "-  quit the calendar program" + System.lineSeparator()
                    + System.lineSeparator()
                    + "Enter an instruction: "
                    + System.lineSeparator(),
            output.toString());
  }

  /**
   * Check that showMenu prints the options menu.
   */
  @Test
  public void testShowMenu() {
    view.showMenu();
    assertEquals("Your options are:"
                    + System.lineSeparator()
                    + System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> "
                    + System.lineSeparator()
                    + "-  creates an event with the given subject " +
                    "within the given time range. subject must "
                    + "be contained in quotes if longer than one word"
                    + System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> repeats " +
                    "<weekdays> for <n> "
                    + "times"
                    + System.lineSeparator() +
                    "-  creates an event like above, repeating on the given days for N weeks" +
                    System.lineSeparator() +
                    "create event <eventSubject> from <dateTime> to <dateTime> repeats " +
                    "<weekdays> until " +
                    "<date>"
                    + System.lineSeparator() +
                    "-  creates an event like above, repeating on the given days until the " +
                    "given date" +
                    System.lineSeparator() +
                    "create event <eventSubject> on <date>"
                    + System.lineSeparator() +
                    "-  creates an all-day event from 8am-5pm, which can also repeat using " +
                    "the above syntax"
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
                    "-  edits the given property (one of subject, start, end, description, " +
                    "location, " +
                    "status) of the event specified"
                    + System.lineSeparator() +
                    "   by its subject, start and end times to have the new property value." +
                    System.lineSeparator() +
                    "edit events <property> <eventSubject> from <dateTime> with " +
                    "<newPropertyValue>" +
                    System.lineSeparator() +
                    "-  edits this event or this and the following events in a series as above, " +
                    "starting at the given date and time, to have the new property value"
                    + System.lineSeparator() +
                    "edit series <property> <eventSubject> from <dateTime> with " +
                    "<newPropertyValue>" +
                    System.lineSeparator() +
                    "-  edits this event or the entire series this event is a part of to have " +
                    "the given " +
                    "property value"
                    + System.lineSeparator() +
                    System.lineSeparator() +
                    "print events on <date>"
                    + System.lineSeparator() +
                    "-  prints a bulleted schedule containing all events on this day" +
                    System.lineSeparator() +
                    "print events from <dateTime> to <dateTime>" + System.lineSeparator() +
                    "-  prints a bulleted schedule containing all events within the given time " +
                    "interval" +
                    System.lineSeparator()
                    + "show status on <dateTime>"
                    + System.lineSeparator() +
                    "-  prints busy or available based on whether the user has any events at " +
                    "this date and " +
                    "time"
                    + System.lineSeparator()
                    + System.lineSeparator()
                    + "menu" + System.lineSeparator()
                    + "-  print this menu" + System.lineSeparator()
                    + "q or quit" + System.lineSeparator()
                    + "-  quit the calendar program" + System.lineSeparator()
                    + System.lineSeparator()
                    + "Enter an instruction: "
                    + System.lineSeparator(),
            output.toString());
  }

  /**
   * Check that write will append exactly the given string onto the output.
   */
  @Test
  public void testWrite() {
    view.write("hello ! !");
    assertEquals("hello ! !", output.toString());
  }

  /**
   * Check that calling write with an empty string will put nothing into the output.
   */
  @Test
  public void testWriteEmpty() {
    view.write("");
    assertEquals("", output.toString());
  }

  /**
   * Check that passing formatSchedule an event with only a start time, end time, and subject
   * will print only those details.
   */
  @Test
  public void testFormatScheduleMinimumOptions() {
    List<Map<String, String>> oneEvent = new ArrayList<>();
    Map<String, String> basicEvent = new HashMap<>();
    basicEvent.put("event", "Event Name");
    basicEvent.put("from", "2025-06-04T13:00");
    basicEvent.put("to", "2025-06-04T16:00");
    oneEvent.add(basicEvent);

    view.formatSchedule(oneEvent);
    assertEquals("* Event Name : from 2025-06-04T13:00 to 2025-06-04T16:00"
            + System.lineSeparator(), output.toString());
  }

  /**
   * Check that passing formatSchedule an event with all possible properties for an event will
   * print all of them.
   */
  @Test
  public void testFormatScheduleAllOptions() {
    List<Map<String, String>> oneEvent = new ArrayList<>();
    Map<String, String> bigEvent = new HashMap<>();
    bigEvent.put("event", "Event Name");
    bigEvent.put("from", "2025-06-04T13:00");
    bigEvent.put("to", "2025-06-04T16:00");
    bigEvent.put("description", "Long description");
    bigEvent.put("location", "physical");
    bigEvent.put("status", "public");
    oneEvent.add(bigEvent);

    view.formatSchedule(oneEvent);
    assertEquals("* Event Name : from 2025-06-04T13:00 to 2025-06-04T16:00 Details: " +
            "Long description @ physical is public"
            + System.lineSeparator(), output.toString());
  }

  /**
   * Since the view only formats what the model gives it, check that formatSchedule calls that
   * lack required arguments will print null in their place.
   */
  @Test
  public void testFormatScheduleMissingRequired() {
    List<Map<String, String>> oneEvent = new ArrayList<>();
    Map<String, String> basicEvent = new HashMap<>();
    basicEvent.put("to", "2025-06-04T16:00");
    oneEvent.add(basicEvent);

    view.formatSchedule(oneEvent);
    assertEquals("* null : from null to 2025-06-04T16:00"
            + System.lineSeparator(), output.toString());
  }

  /**
   * CalendarViews are meant to format input based on the calendar implementation being used,
   * so when an unrecognized key is passed in the events, they should ignore it.
   */
  @Test
  public void testFormatScheduleBadKey() {
    List<Map<String, String>> oneEvent = new ArrayList<>();
    Map<String, String> wrongEvent = new HashMap<>();
    wrongEvent.put("event", "Event Name");
    wrongEvent.put("from", "2025-06-04T13:00");
    wrongEvent.put("to", "2025-06-04T16:00");
    wrongEvent.put("wrong field", "snuck in");
    oneEvent.add(wrongEvent);

    view.formatSchedule(oneEvent);
    assertEquals("* Event Name : from 2025-06-04T13:00 to 2025-06-04T16:00"
            + System.lineSeparator(), output.toString());
  }

  /**
   * Check that when multiple events are passed, they will all be formatted and output.
   */
  @Test
  public void testFormatScheduleMultipleEvents() {
    List<Map<String, String>> oneEvent = new ArrayList<>();
    Map<String, String> eventOne = new HashMap<>();
    eventOne.put("event", "Event Name");
    eventOne.put("from", "2025-06-04T13:00");
    eventOne.put("to", "2025-06-04T16:00");
    oneEvent.add(eventOne);
    Map<String, String> eventTwo = new HashMap<>();
    eventTwo.put("event", "Event Name 2");
    eventTwo.put("from", "2025-06-04T13:00");
    eventTwo.put("to", "2025-06-04T16:00");
    oneEvent.add(eventTwo);

    view.formatSchedule(oneEvent);
    assertEquals("* Event Name : from 2025-06-04T13:00 to 2025-06-04T16:00"
            + System.lineSeparator()
            + "* Event Name 2 : from 2025-06-04T13:00 to 2025-06-04T16:00"
            + System.lineSeparator(), output.toString());
  }

}
