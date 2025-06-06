import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import controller.CalendarController;
import controller.CalendarControllerImpl;
import model.CalendarModel;
import model.CalendarModelImpl;
import view.CalendarView;
import view.CalendarViewImpl;

/**
 * A main class to run the Calendar program.
 */
public class CalendarProgram {

  /**
   * The Main method to run the program.
   * @param args the input command
   */
  public static void main(String[] args) {
    CalendarModel model = new CalendarModelImpl();
    CalendarView view = new CalendarViewImpl(System.out);
    CalendarController controller;

    if (args[0].equals("--mode")) {
      switch ( args[1] ) {
        case "interactive":
          controller = new CalendarControllerImpl(model, new InputStreamReader(System.in), view);
          break;
        case "headless":
          FileReader reader;
          try {
            reader = new FileReader(args[2]);
          } catch (IOException e) {
            throw new IllegalArgumentException("The file entered in headless mode was not found.");
          }
          controller = new CalendarControllerImpl(model, reader, view);
          break;
        default:
          throw new IllegalArgumentException( args[1] + " is not a recognized run mode.");
      }
      controller.control();
    }
  }
}
