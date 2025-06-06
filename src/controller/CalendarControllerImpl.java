package controller;

import java.util.Scanner;

import model.CalendarModel;
import view.CalendarView;

/**
 * Control a calendar program using text input and a small range of valid commands.
 */
public class CalendarControllerImpl implements CalendarController {

  private final CalendarModel model;
  private final Readable input;
  private final CalendarView view;

  /**
   * Constructs a new CalendarControllerImpl with the given non-null model object, view object,
   * and text-based input stream.
   *
   * @param model The CalendarModel that will handle logical processing and storing of this
   *              calendar.
   * @param input The readable input stream from which text commands are passed to the calendar.
   * @param view  The CalendarView that will display output from this calendar.
   */
  public CalendarControllerImpl(CalendarModel model, Readable input, CalendarView view) {
    if (model == null || input == null || view == null) {
      throw new IllegalArgumentException("The given calendar, input, and view objects may not " +
              "be null.");
    }
    this.model = model;
    this.input = input;
    this.view = view;
  }

  /**
   * Start up the program, then continuously check for commands using a scanner until the program
   * is quit or the input source runs out. If the latter happens, notify the user of the error.
   */
  public void control() {
    Scanner scan = new Scanner(input);
    boolean quit = false;

    view.welcome();

    while (!quit && scan.hasNext()) {

      String nextToken = scan.next();
      switch (nextToken) {
        case "q":
        case "quit":
          quit = true;
          break;
        case "menu":
          view.showMenu();
          break;
        default:
          this.processCommand(nextToken, scan);
      }
    }
    if (!quit) {
      view.write("Did not provide an exit command.");
    }
  }

  /**
   * Check that the given input to this calculator is a valid command, then delegate to the
   * corresponding CalendarCommand object to handle it. Input that throws errors does not kill
   * the program, but displays the error message and then prompts the user again.
   *
   * @param firstToken The first keyword of the arguments, which specifies the type of the
   *                   command.
   * @param scan       The rest of the arguments in this command.
   */
  private void processCommand(String firstToken, Scanner scan) {
    CalendarCommand cmd;

    switch (firstToken) {
      case "create":
        cmd = new CreateEventCmd();
        break;
      case "edit":
        cmd = new EditEventCmd();
        break;
      case "print":
        cmd = new PrintCmd();
        break;
      case "show":
        cmd = new ShowCmd();
        break;
      default:
        throw new IllegalArgumentException("Unrecognized command \"" + firstToken +
                "\" was found. " + "Please input a valid command to the calendar.");
    }

    try {
      cmd.execute(model, scan.nextLine(), view);
    } catch (Exception e) {
      view.write(e.getMessage() + System.lineSeparator());
      view.write("Please try again:");
    }
  }

}
