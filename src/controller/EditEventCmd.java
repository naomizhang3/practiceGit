package controller;

import java.util.HashMap;
import java.util.Scanner;
import model.CalendarModel;
import view.CalendarView;

/**
 * Command to edit an existing event in a calendar.
 */
public class EditEventCmd extends MultiWordEventCommand {

  /**
   * Parse out the rest of the arguments to this command, allowing quote-enclosed multi-word input,
   * and then pass the result to the model to edit the event.
   * @param model             The CalendarModel to operate on.
   * @param restOfCommand     The rest of the command-line arguments following the keyword of this
   *                          command.
   * @param view              The view object that any displayable output should be sent to.
   */
  public void execute(CalendarModel model, String restOfCommand, CalendarView view) {
    Scanner scan = new Scanner(restOfCommand);
    String eventType = scan.next();
    String propName = scan.next();
    String eventSubject = this.scanForQuotedInput(scan.next(), scan);

    HashMap<String, String> identifiers = new HashMap<>();
    identifiers.put("event", eventSubject);
    String newProperty = null;

    while (scan.hasNext()) {
      String key = scan.next();
      String value;

      if (key.equals("with")) {
        try {
          newProperty = this.scanForQuotedInput(scan.next(), scan);
        } catch (Exception e) {
          throw new IllegalArgumentException("Must specify the new property value to update " +
                  "this event with.");
        }
        break;
      }
      else {
        try {
          value = this.scanForQuotedInput(scan.next(), scan);
        } catch (Exception e) {
          throw new IllegalArgumentException("All details of a command should be written as per " +
                  "the formatting instructions in the user guide.");
        }
        identifiers.put(key, value);
      }
    }

    model.editEvent(eventType, propName, identifiers, newProperty);
  }
}
