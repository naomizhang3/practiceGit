package test;

import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import controller.CalendarController;
import controller.CalendarControllerImpl;
import model.CalendarModel;
import view.CalendarView;
import view.CalendarViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test class for calendar controller.
 */
public class TestCalendarController {
  StringBuilder log;
  Reader in;
  StringBuilder out;
  CalendarModel mockModel;
  CalendarView view;
  CalendarController controller;
  String expected;
  StringReader quit;


  @Before
  public void setUp() {
    log = new StringBuilder();
    in = new StringReader("");
    out = new StringBuilder();
    mockModel = new MockCalendar(log);
    view = new CalendarViewImpl(out);
    controller = new CalendarControllerImpl(mockModel, in, view);
    expected = "";
    quit = new StringReader("q");
  }


  @Test
  public void testExecuteCreateCmd() {
    in = new StringReader("create event awesomeSubject from 2025-06-05T09:00 " +
            "to 2025-06-05T10:00 description cool location physical status public");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();

    expected = "description: cool\n" + "from: 2025-06-05T09:00\n" + "location: physical\n"
            + "to: 2025-06-05T10:00\n" + "event: awesomeSubject\n" + "status: public\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testExecuteEditCmd() {
    in = new StringReader("edit event subject oldSubj from 2025-06-05T09:00 to 2025-06-05T09:00"
            + " with newSubj");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();

    expected = "property: subject\n" + "identifiers: from - 2025-06-05T09:00\n"
            + "identifiers: to - 2025-06-05T09:00\n"
            + "identifiers: event - oldSubj\n" + "newPropValue: newSubj\n";
    assertEquals(expected, log.toString());

  }

  @Test
  public void testExecutePrintCmdOn() {
    in = new StringReader("print events on 2025-06-05");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();

    expected = "day: 2025-06-05\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testExecutePrintCmdFromTo() {
    in = new StringReader("print events from 2025-06-05T10:00 to 2025-06-05T12:00");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();

    expected = "start: 2025-06-05T10:00\n" + "end: 2025-06-05T12:00\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testExecuteShowCmd() {
    in = new StringReader("show status on 2025-06-05T10:00");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();

    expected = "specified dateTime: 2025-06-05T10:00\n";
    assertEquals(expected, log.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidControllerNullModel() {
    new CalendarControllerImpl(null, quit, view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidControllerNullInput() {
    new CalendarControllerImpl(mockModel, null, view);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidControllerNullView() {
    new CalendarControllerImpl(mockModel, quit, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testControllerCreateBadCommand() {
    in = new StringReader("bad event CoolSubject on 2025-06-05");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();
  }

  @Test
  public void testControllerCreateCommandNoEvent() {
    in = new StringReader("create aaaaa CoolSubject on 2025-06-05");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();
    expected = "aaaaa: CoolSubject\n" + "on: 2025-06-05\n";
    assertEquals(expected, log.toString());
  }

  @Test
  public void testControllerCreateCommandInvalidTime() {
    in = new StringReader("create event CoolSubject on HELLO-06-05");

    controller = new CalendarControllerImpl(mockModel, in, view);

    controller.control();
    expected = "event: CoolSubject\n" + "on: HELLO-06-05\n";
    assertEquals(expected, log.toString());
  }

}

