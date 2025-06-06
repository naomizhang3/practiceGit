package controller;

import model.CalendarModel;
import view.CalendarView;

/**
 * Specifies the sole operation of a command class for Calendars.
 */
interface CalendarCommand {

  /**
   * Execute this command on the given calendar using its public methods.
   * @param model             The CalendarModel to operate on.
   * @param restOfCommand     The rest of the command-line arguments following the keyword of this
   *                          command.
   * @param view              The view object that any displayable output should be sent to.
   */
  void execute(CalendarModel model, String restOfCommand, CalendarView view);
}
