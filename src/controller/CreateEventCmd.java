package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.CalendarModel;
import view.CalendarView;

/**
 * Command to create an event in a calendar.
 */
public class CreateEventCmd extends MultiWordEventCommand {

  /**
   * Parse out the rest of the arguments to this command, allowing for quote-enclosed multi-word
   * arguments, and pass them to the model to create the specified event.
   * @param model             The CalendarModel to operate on.
   * @param restOfCommand     The rest of the command-line arguments following the keyword of this
   *                          command.
   * @param view              The view object that any displayable output should be sent to.
   */
  public void execute(CalendarModel model, String restOfCommand, CalendarView view) {
    Scanner scan = new Scanner(restOfCommand);
    Map<String, String> properties = new HashMap<>();

    while (scan.hasNext()) {
      String key = scan.next().toLowerCase();
      String value;

      if (!scan.hasNext() && !key.equals("times")) {
        throw new IllegalArgumentException("No value provided for this key: " + key);
      }

      value = scanForQuotedInput(scan.next(), scan);
      properties.put(key, value);
    }

    model.createEvent(properties);
  }


}
