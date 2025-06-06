import java.util.List;
import java.util.Map;

import model.CalendarModel;

/**
 * A Mock Calendar Model class used for testing.
 */
public class MockCalendar implements CalendarModel {
  final StringBuilder log;

  public MockCalendar(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void createEvent(Map<String, String> properties) {
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      log.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }
  }

  @Override
  public void editEvent(String eventType, String property, Map<String, String> identifier,
                        String newPropertyValue) {
    log.append("property: ").append(property).append("\n");
    for (Map.Entry<String, String> e : identifier.entrySet()) {
      log.append("identifiers: ").append(e.getKey()).append(" - ")
              .append(e.getValue()).append("\n");
    }
    log.append("newPropValue: ").append(newPropertyValue).append("\n");

  }

  @Override
  public List<Map<String, String>> getSchedule(String start, String end) {
    log.append("start: ").append(start).append("\n");
    log.append("end: ").append(end).append("\n");
    return List.of();
  }

  @Override
  public List<Map<String, String>> getSchedule(String day) {
    log.append("day: ").append(day).append("\n");
    return List.of();
  }

  @Override
  public String getStatus(String dateTime) {
    log.append("specified dateTime: ").append(dateTime).append("\n");
    return "";
  }

}
