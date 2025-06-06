package test;

import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import model.CalendarModel;
import model.CalendarModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for calendar model.
 */
public class TestCalendarModel {

  CalendarModel cal;
  CalendarModel seriesCal2;
  CalendarModel cal1;
  CalendarModel seriesCal;

  @Before
  public void setup() {
    this.cal = new CalendarModelImpl();
    this.seriesCal2 = new CalendarModelImpl();
    HashMap<String, String> seriesProps1 = new HashMap<>();
    seriesProps1.put("event", "First");
    seriesProps1.put("from", "2025-05-05T10:00");
    seriesProps1.put("to", "2025-05-05T11:00");
    seriesProps1.put("repeats", "MW");
    seriesProps1.put("for", "6");
    seriesCal2.createEvent(seriesProps1);

    this.cal1 = new CalendarModelImpl();

    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal1.createEvent(properties);

    this.seriesCal = new CalendarModelImpl();
    HashMap<String, String> seriesProps = new HashMap<>();
    seriesProps.put("event", "A series.");
    seriesProps.put("from", "2025-06-01T13:00");
    seriesProps.put("to", "2025-06-01T16:00");
    seriesProps.put("repeats", "MTWR");
    seriesProps.put("for", "4");
    seriesCal.createEvent(seriesProps);


  }

  @Test(expected = NullPointerException.class)
  public void createEventNoSubject() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("description", "An event.");
    properties.put("location", "physical");
    properties.put("status", "public");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventNoStartTime() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Bad event.");
    properties.put("to", "2025-05-31T16:00");
    properties.put("description", "Sad.");
    properties.put("location", "physical");
    properties.put("status", "public");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventEndsBeforeBeginsTime() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Ends at an earlier time than it begins.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T10:00");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventEndsBeforeBeginsDate() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Ends at an earlier date than it begins.");
    properties.put("from", "2025-07-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventDuplicate() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Duplicate event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("description", "Sad.");
    properties.put("location", "physical");
    properties.put("status", "public");

    cal.createEvent(properties);
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventNearDuplicate() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "Duplicate event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    properties0.put("description", "Sad.");

    HashMap<String, String> properties1 = new HashMap<>();
    properties1.put("event", "Duplicate event.");
    properties1.put("from", "2025-05-31T13:00");
    properties1.put("to", "2025-05-31T16:00");
    properties1.put("description", "Sadder.");

    cal.createEvent(properties0);
    cal.createEvent(properties1);
  }

  @Test(expected = DateTimeParseException.class)
  public void createSeriesTooLongEventUntil() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Series with an event lasting >1 day.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-06-01T16:00");
    properties.put("repeats", "MTWR");
    properties.put("until", "2025-06-31");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createSeriesTooLongEventFor() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Series with an event lasting >1 day.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-06-01T16:00");
    properties.put("repeats", "MTWR");
    properties.put("for", "5");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createSeriesOverlapsEvent() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "Series that overlaps an existing event.");
    properties0.put("from", "2025-06-02T13:00");
    properties0.put("to", "2025-06-02T16:00");
    cal.createEvent(properties0);

    HashMap<String, String> properties1 = new HashMap<>();
    properties1.put("event", "Series that overlaps an existing event.");
    properties1.put("from", "2025-05-31T13:00");
    properties1.put("to", "2025-05-31T16:00");
    properties1.put("repeats", "MTWR");
    properties1.put("for", "1");
    cal.createEvent(properties1);
  }

  @Test(expected = DateTimeParseException.class)
  public void createEventNoTime() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "model.Event has timeless dates.");
    properties.put("from", "2025-05-31");
    properties.put("to", "2025-05-31");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventInvalidLocation() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Location is not one of physical or online.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("location", "aaaaaa");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventInvalidStatus() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("status", "aaaaaa");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventInvalidRepeats() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Repeats.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("repeats", "MWA");
    properties.put("for", "5");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventInvalidFor() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("repeats", "MTWR");
    properties.put("for", "aaaaaa");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventForWithNoRepeats() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("for", "5");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createEventUntilWithNoRepeats() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("until", "2025-06-30");
    cal.createEvent(properties);
  }

  @Test(expected = DateTimeParseException.class)
  public void createEventInvalidUntil() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("repeats", "MTWR");
    properties.put("until", "aaaaa");
    cal.createEvent(properties);
  }

  @Test(expected = DateTimeParseException.class)
  public void createEventTooEarlyUntil() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "Status is not one of public or private.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    properties.put("repeats", "MTWR");
    properties.put("until", "2025-04-31");
    cal.createEvent(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getScheduleBadTimeRange() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getSchedule("2025-06-01T16:00", "2025-05-30T13:00");
  }

  @Test(expected = DateTimeParseException.class)
  public void getScheduleBadFrom() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getSchedule("aaaaaa", "2025-06-01T16:00");
  }

  @Test(expected = DateTimeParseException.class)
  public void getScheduleBadTo() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getSchedule("2025-05-30T13:00", "aaaa");
  }

  @Test(expected = DateTimeParseException.class)
  public void getScheduleNonDateInput() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getSchedule("aaaaa");
  }

  @Test(expected = DateTimeParseException.class)
  public void getScheduleBadOnHasTime() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getSchedule("2025-05-31T13:00");
  }

  @Test(expected = DateTimeParseException.class)
  public void getStatusBadOn() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getStatus("aaaaaa");
  }

  @Test(expected = DateTimeParseException.class)
  public void getStatusBadOnNoTime() {
    HashMap<String, String> properties0 = new HashMap<>();
    properties0.put("event", "An event.");
    properties0.put("from", "2025-05-31T13:00");
    properties0.put("to", "2025-05-31T16:00");
    cal.createEvent(properties0);

    cal.getStatus("2025-05-31");
  }

  @Test
  public void getStatusBusyInMiddle() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);

    assertEquals("busy", cal.getStatus("2025-05-31T14:30"));
  }

  @Test
  public void getStatusBusyAtStart() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);

    assertEquals("busy", cal.getStatus("2025-05-31T13:00"));
  }

  @Test
  public void getStatusBusyAtEnd() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);

    assertEquals("busy", cal.getStatus("2025-05-31T16:00"));
  }

  @Test
  public void getStatusAvailableDiffTime() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);

    assertEquals("available", cal.getStatus("2025-05-31T17:00"));
  }

  @Test
  public void getStatusAvailableDiffDay() {
    HashMap<String, String> properties = new HashMap<>();
    properties.put("event", "An event.");
    properties.put("from", "2025-05-31T13:00");
    properties.put("to", "2025-05-31T16:00");
    cal.createEvent(properties);

    assertEquals("available", cal.getStatus("2025-05-01T14:30"));
  }

  // create event tests
  @Test
  public void testCreateEventZeroDuration() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original Event");
    properties.put("from", "2025-07-31T10:00");
    properties.put("to", "2025-07-31T10:00");
    cal.createEvent(properties);
    assertEquals("Original Event", cal.getSchedule("2025-07-31")
            .get(0).get("event"));
    assertEquals("2025-07-31T10:00", cal.getSchedule("2025-07-31")
            .get(0).get("from"));
    assertEquals("2025-07-31T10:00", cal.getSchedule("2025-07-31")
            .get(0).get("to"));
  }

  @Test
  public void testCreateEventOnlyRequired() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original Event");
    properties.put("from", "2025-07-31T10:00");
    properties.put("to", "2025-07-31T11:00");
    cal.createEvent(properties);
    assertEquals("Original Event", cal.getSchedule("2025-07-31")
            .get(0).get("event"));
    assertEquals("2025-07-31T10:00", cal.getSchedule("2025-07-31")
            .get(0).get("from"));
    assertEquals("2025-07-31T11:00", cal.getSchedule("2025-07-31")
            .get(0).get("to"));
  }

  @Test
  public void testCreateEventNonRequired() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original Event");
    properties.put("from", "2025-07-31T10:00");
    properties.put("to", "2025-07-31T11:00");
    properties.put("description", "This is a description");
    properties.put("location", "Physical");
    properties.put("status", "Private");
    cal.createEvent(properties);
    assertEquals("Original Event", cal.getSchedule("2025-07-31").get(0).get("event"));
    assertEquals("2025-07-31T10:00", cal.getSchedule("2025-07-31").get(0).get("from"));
    assertEquals("2025-07-31T11:00", cal.getSchedule("2025-07-31").get(0).get("to"));
    assertEquals("This is a description", cal.getSchedule("2025-07-31").get(0).get("description"));
    assertEquals("physical", cal.getSchedule("2025-07-31").get(0).get("location"));
    assertEquals("private", cal.getSchedule("2025-07-31").get(0).get("status"));

  }

  @Test
  public void testCreateEventSeriesFor1Weekday() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-31T10:00");
    properties.put("to", "2025-07-31T11:00");
    properties.put("repeats", "R");
    properties.put("for", "1");
    cal.createEvent(properties);
    assertEquals("2025-07-31T10:00",
            cal.getSchedule("2025-07-31T10:00", "2025-08-31T10:00").get(0).get("from"));
  }

  @Test
  public void testCreateEventSeriesForManyWeekdays() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-28T10:00");
    properties.put("to", "2025-07-28T11:00");
    properties.put("repeats", "MWF");
    properties.put("for", "2");
    cal.createEvent(properties);
    assertEquals("2025-07-28T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(0).get("from"));
    assertEquals("2025-07-30T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(1).get("from"));
  }

  @Test
  public void testCreateEventSeriesForManyWeekdays2Weeks() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-28T10:00");
    properties.put("to", "2025-07-28T11:00");
    properties.put("repeats", "MWF");
    properties.put("for", "6");
    cal.createEvent(properties);
    assertEquals("2025-07-28T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(0).get("from"));
    assertEquals("2025-07-30T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(1).get("from"));
    assertEquals("2025-08-01T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(2).get("from"));
    assertEquals("2025-08-04T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(3).get("from"));
    assertEquals("2025-08-06T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(4).get("from"));
    assertEquals("2025-08-08T10:00",
            cal.getSchedule("2025-07-28T10:00", "2025-08-30T20:00").get(5).get("from"));
  }

  @Test
  public void testCreateEventSeriesUntil1Weekday() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-21T10:00");
    properties.put("to", "2025-07-21T11:00");
    properties.put("repeats", "M");
    properties.put("until", "2025-07-28");
    cal.createEvent(properties);
    assertEquals("2025-07-21T10:00",
            cal.getSchedule("2025-07-21T10:00", "2025-07-29T20:00").get(0).get("from"));
    assertEquals("2025-07-28T10:00",
            cal.getSchedule("2025-07-21T10:00", "2025-07-29T20:00").get(1).get("from"));
  }

  @Test
  public void testCreateEventSeriesUntilManyWeekday() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-22T10:00");
    properties.put("to", "2025-07-22T11:00");
    properties.put("repeats", "TRS");
    properties.put("until", "2025-08-03");
    cal.createEvent(properties);
    assertEquals("2025-07-22T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(0).get("from"));
    assertEquals("2025-07-24T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(1).get("from"));
    assertEquals("2025-07-26T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(2).get("from"));
    assertEquals("2025-07-29T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(3).get("from"));
    assertEquals("2025-07-31T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(4).get("from"));
    assertEquals("2025-08-02T10:00",
            cal.getSchedule("2025-07-22T10:00", "2025-08-03T20:00").get(5).get("from"));

  }

  @Test
  public void testCreateEventSeriesUntilStartNotRepeat() {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("event", "Original event");
    properties.put("from", "2025-07-21T10:00");
    properties.put("to", "2025-07-21T11:00");
    properties.put("repeats", "T");
    properties.put("until", "2025-07-29");
    cal.createEvent(properties);
    assertEquals("2025-07-22T10:00",
            cal.getSchedule("2025-07-21T10:00", "2025-07-29T20:00").get(0).get("from"));
  }

  @Test
  public void testEditBreakSeriesCal2() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "First");
    id.put("from", "2025-05-12T10:00");
    id.put("to", "2025-05-12T11:00");
    seriesCal2.editEvent("events", "subject", id, "Second");

    assertEquals("First", seriesCal2.getSchedule("2025-05-05")
            .get(0).get("event"));
    assertEquals("First", seriesCal2.getSchedule("2025-05-07")
            .get(0).get("event"));
    assertEquals("Second", seriesCal2.getSchedule("2025-05-12")
            .get(0).get("event"));
    assertEquals("Second", seriesCal2.getSchedule("2025-05-14")
            .get(0).get("event"));
    assertEquals("Second", seriesCal2.getSchedule("2025-05-19")
            .get(0).get("event"));
    assertEquals("Second", seriesCal2.getSchedule("2025-05-21")
            .get(0).get("event"));

    // 3. Edit to Third for all events in the series
    HashMap<String, String> id2 = new HashMap<>();
    id2.put("event", "First");
    id2.put("from", "2025-05-05T10:00");
    id2.put("to", "2025-05-05T11:00");
    seriesCal2.editEvent("series", "subject", id2, "Third");

    assertEquals("Third", seriesCal2.getSchedule("2025-05-05")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-07")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-12")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-14")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-19")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-21")
            .get(0).get("event"));

    // 4. Start time on May 12 to 10:30am, this and following
    HashMap<String, String> id3 = new HashMap<>();
    id3.put("event", "Third");
    id3.put("from", "2025-05-12T10:00");
    id3.put("to", "2025-05-12T11:00");
    seriesCal2.editEvent("events", "start", id3,
            "2025-05-12T10:30");

    assertEquals("2025-05-05T10:00", seriesCal2.getSchedule("2025-05-05")
            .get(0).get("from"));
    assertEquals("2025-05-07T10:00", seriesCal2.getSchedule("2025-05-07")
            .get(0).get("from"));
    assertEquals("2025-05-12T10:30", seriesCal2.getSchedule("2025-05-12")
            .get(0).get("from"));
    assertEquals("2025-05-14T10:30", seriesCal2.getSchedule("2025-05-14")
            .get(0).get("from"));
    assertEquals("2025-05-19T10:30", seriesCal2.getSchedule("2025-05-19")
            .get(0).get("from"));
    assertEquals("2025-05-21T10:30", seriesCal2.getSchedule("2025-05-21")
            .get(0).get("from"));

    // 5. edit subject of May 5 as Fourth, all events in this series
    HashMap<String, String> id4 = new HashMap<>();
    id4.put("event", "Third");
    id4.put("from", "2025-05-05T10:00");
    id4.put("to", "2025-05-05T11:00");
    seriesCal2.editEvent("series", "subject", id4,
            "Fourth");
    assertEquals("Fourth", seriesCal2.getSchedule("2025-05-05")
            .get(0).get("event"));
    assertEquals("Fourth", seriesCal2.getSchedule("2025-05-07")
            .get(0).get("event"));
    assertEquals("Fourth", seriesCal2.getSchedule("2025-05-12")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-14")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-19")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-21")
            .get(0).get("event"));

    //6. edit subject of may 12 to be fifth, all series
    HashMap<String, String> id5 = new HashMap<>();
    id5.put("event", "Fourth");
    id5.put("from", "2025-05-12T10:00");
    id5.put("to", "2025-05-12T11:00");
    seriesCal2.editEvent("series", "subject", id5,
            "Fifth");
    assertEquals("Fifth", seriesCal2.getSchedule("2025-05-05")
            .get(0).get("event"));
    assertEquals("Fifth", seriesCal2.getSchedule("2025-05-07")
            .get(0).get("event"));
    assertEquals("Fifth", seriesCal2.getSchedule("2025-05-12")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-14")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-19")
            .get(0).get("event"));
    assertEquals("Third", seriesCal2.getSchedule("2025-05-21")
            .get(0).get("event"));
  }


  @Test(expected = DateTimeParseException.class)
  public void editEventNonspecificSearch() {
    HashMap<String, String> otherEventSameName = new HashMap<>();
    otherEventSameName.put("event", "An event.");
    otherEventSameName.put("from", "2025-06-31T05:00");
    otherEventSameName.put("to", "2025-06-31T06:00");
    cal1.createEvent(otherEventSameName);

    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "An event.");
    cal1.editEvent("event", "location", identification,
            "physical");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editEventIdentifierNotFound() {
    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "aaaaaa");
    cal1.editEvent("event", "location", identification,
            "physical");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editEventNonsenseNewPropertyValue() {

    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "An event.");
    cal1.editEvent("event", "location", identification, "aaaaa");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editEventEndBeforeBegins() {
    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "An event.");
    cal1.editEvent("event", "to", identification,
            "2025-05-01T16:00");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editEventCreatesOverlappingEvents() {
    HashMap<String, String> differentEvent = new HashMap<>();
    differentEvent.put("event", "A different event.");
    differentEvent.put("from", "2025-05-31T13:00");
    differentEvent.put("to", "2025-05-31T06:00");
    cal1.createEvent(differentEvent);

    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "A different event.");
    cal1.editEvent("event", "event", identification, "An event.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void editEventTooSpecific() {
    HashMap<String, String> identification = new HashMap<>();
    identification.put("event", "An event.");
    identification.put("from", "2025-05-31T13:00");
    identification.put("to", "2025-05-31T06:00");
    identification.put("description", "Our event matches all fields but this one.");
    cal1.editEvent("event", "to", identification,
            "2025-05-31T18:00");
  }

  @Test
  public void editEventSubject() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "subject", id, "different subject");

    assertEquals("different subject", cal1.getSchedule("2025-05-31")
            .get(0).get("event"));
  }

  @Test
  public void editEventStart() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "start", id, "2025-05-31T12:00");

    assertEquals("2025-05-31T12:00", cal1.getSchedule("2025-05-31")
            .get(0).get("from"));
  }

  @Test
  public void editEventEnd() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "end", id, "2025-05-31T17:00");

    assertEquals("2025-05-31T17:00", cal1.getSchedule("2025-05-31")
            .get(0).get("to"));
  }

  @Test
  public void editEventDescription() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "description", id,
            "longer description of the event.");

    assertEquals("longer description of the event.", cal1.getSchedule("2025-05-31")
            .get(0).get("description"));
  }

  @Test
  public void editEventLocation() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "location", id, "online");

    assertEquals("online", cal1.getSchedule("2025-05-31")
            .get(0).get("location"));
  }

  @Test
  public void editEventStatus() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "status", id, "public");

    assertEquals("public", cal1.getSchedule("2025-05-31")
            .get(0).get("status"));
  }

  @Test
  public void editEventIsNowSearchableByNewValue() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "subject", id, "find me");

    HashMap<String, String> id2 = new HashMap<>();
    id2.put("event", "find me");
    id2.put("from", "2025-05-31T13:00");
    cal1.editEvent("event", "subject", id2, "found me");

    assertEquals("found me", cal1.getSchedule("2025-05-31")
            .get(0).get("event"));
  }

  /**
   * Check that editing one event in a series does not edit its following events.
   */
  @Test
  public void editEventDoesNotUpdateSeries() {
    HashMap<String, String> series = new HashMap<>();
    series.put("event", "A series.");
    series.put("from", "2025-05-31T13:00");
    series.put("to", "2025-05-31T16:00");
    series.put("repeats", "MTWR");
    series.put("until", "2025-06-10");
    cal1.createEvent(series);

    assertEquals("A series.", cal1.getSchedule("2025-06-02").get(0).get("event"));
    assertEquals("A series.", cal1.getSchedule("2025-06-03").get(0).get("event"));

    HashMap<String, String> id = new HashMap<>();
    id.put("event", "A series.");
    id.put("from", "2025-06-02T13:00");
    id.put("to", "2025-06-02T16:00");
    cal1.editEvent("event", "subject", id,
            "not in series anymore lol");


    assertEquals("not in series anymore lol",
            cal1.getSchedule("2025-06-02").get(0).get("event"));
    assertEquals("A series.", cal1.getSchedule("2025-06-03").get(0).get("event"));
  }

  /**
   * Check that "edit events ___" will work as edit event does when given a single event.
   */
  @Test
  public void editEventsSingleEvent() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    cal1.editEvent("events", "subject", id, "different subject");

    assertEquals("different subject", cal1.getSchedule("2025-05-31")
            .get(0).get("event"));
  }

  /**
   * Check that updating the start time of a partial series using "edit events" changes only the
   * start times of the specified event and those following.
   */
  @Test
  public void editEventsChangeSeriesStartTime() {
    assertEquals("2025-06-02T13:00",
            seriesCal.getSchedule("2025-06-02").get(0).get("from"));
    assertEquals("2025-06-03T13:00",
            seriesCal.getSchedule("2025-06-03").get(0).get("from"));

    HashMap<String, String> id = new HashMap<>();
    id.put("event", "A series.");
    id.put("from", "2025-06-03T13:00");
    id.put("to", "2025-06-03T16:00");
    seriesCal.editEvent("events", "start", id,
            "2025-06-03T08:00");

    assertEquals("2025-06-02T13:00",
            seriesCal.getSchedule("2025-06-02").get(0).get("from"));
    assertEquals("2025-06-03T08:00",
            seriesCal.getSchedule("2025-06-03").get(0).get("from"));
    assertEquals("2025-06-04T08:00",
            seriesCal.getSchedule("2025-06-04").get(0).get("from"));
    assertEquals("2025-06-05T08:00",
            seriesCal.getSchedule("2025-06-05").get(0).get("from"));
  }

  /**
   * Check that "edit series ___" will work as edit event does when given a single event.
   */
  @Test
  public void editSeriesSingleEvent() {
    HashMap<String, String> id = new HashMap<>();
    id.put("event", "An event.");
    id.put("from", "2025-05-31T13:00");
    id.put("to", "2025-05-31T16:00");
    cal1.editEvent("series", "subject", id, "different subject");

    assertEquals("different subject", cal1.getSchedule("2025-05-31")
            .get(0).get("event"));
  }

  /**
   * Check that all events in a series will be updated by "edit series", even when the method
   * is called on an event in the middle of the series.
   */
  @Test
  public void editSeriesChangeSeriesStartTime() {
    assertEquals("2025-06-02T13:00",
            seriesCal.getSchedule("2025-06-02").get(0).get("from"));
    assertEquals("2025-06-03T13:00",
            seriesCal.getSchedule("2025-06-03").get(0).get("from"));

    HashMap<String, String> id = new HashMap<>();
    id.put("event", "A series.");
    id.put("from", "2025-06-03T13:00");
    id.put("to", "2025-06-03T16:00");
    seriesCal.editEvent("series", "start", id,
            "2025-06-03T08:00");

    assertEquals("2025-06-02T08:00",
            seriesCal.getSchedule("2025-06-02").get(0).get("from"));
    assertEquals("2025-06-03T08:00",
            seriesCal.getSchedule("2025-06-03").get(0).get("from"));
    assertEquals("2025-06-04T08:00",
            seriesCal.getSchedule("2025-06-04").get(0).get("from"));
    assertEquals("2025-06-05T08:00",
            seriesCal.getSchedule("2025-06-05").get(0).get("from"));
  }


}