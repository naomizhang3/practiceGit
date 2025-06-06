package controller;

import java.util.Scanner;

import model.CalendarModel;
import view.CalendarView;

/**
 * Command to print the user's schedule for a specified range to the output.
 */
public class PrintCmd implements CalendarCommand {

  /**
   * Execute this command using the given calendar, searching the rest of the arguments for the
   * time range to find a schedule for, and then have the given view object display the result.
   *
   * @param model         The CalendarModel to operate on.
   * @param restOfCommand The rest of the command-line arguments following the keyword of this
   *                      command.
   * @param view          The view object that any displayable output should be sent to.
   */
  public void execute(CalendarModel model, String restOfCommand, CalendarView view) {
    Scanner scan = new Scanner(restOfCommand);
    if (!scan.next().equalsIgnoreCase("events")) {
      throw new IllegalArgumentException("Print commands must begin with the phrase 'print "
              + "events'.");
    }

    switch (scan.next().toLowerCase()) {
      case "on":
        view.formatSchedule(model.getSchedule(scan.next()));
        return;
      case "from":
        String from = scan.next();
        if (scan.next().equals("to")) {
          String to = scan.next();
          view.formatSchedule(model.getSchedule(from, to));
          return;
        }
        break;
      default:
        throw new IllegalArgumentException("Must specify a date or a time range for which to "
                + "show schedule.");

    }
  }
}
