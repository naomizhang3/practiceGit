package controller;

import java.util.Scanner;

import model.CalendarModel;
import view.CalendarView;

/**
 * Command to show the user's status at a given date and time.
 */
public class ShowCmd implements CalendarCommand {

  /**
   * Execute this command using the given calendar, checking that it is formatted correctly, and
   * have the given view object print the result.
   * @param model             The CalendarModel to operate on.
   * @param restOfCommand     The rest of the command-line arguments following the keyword of this
   *                          command.
   * @param view              The view object that any displayable output should be sent to.
   */
  public void execute(CalendarModel model, String restOfCommand, CalendarView view) {
    Scanner scan = new Scanner(restOfCommand);
    try {
      if (scan.next().equalsIgnoreCase("status") && scan.next().equalsIgnoreCase("on")) {
        view.write(model.getStatus(scan.next()) + System.lineSeparator());
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Show status command must be formatted as specified " +
              "in the user guide, as 'show status on <dateTime>'.");
    }
  }
}
