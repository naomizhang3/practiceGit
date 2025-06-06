package controller;

import java.util.Scanner;

/**
 * Specifies calendar commands that must be able to parse multi-word input enclosed in double
 * quotes, and provides an operation to check for such inputs.
 */
abstract class MultiWordEventCommand implements CalendarCommand {

  /**
   * Returns a full quoted string value, starting from the first token.
   * @param first a String containing the word that has already been scanned
   * @param scan  the scanner to continue scanning for multiple words
   */
  protected String scanForQuotedInput(String first, Scanner scan) {
    if (!first.startsWith("\"")) {
      return first;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(first);

    // ends with quote as well means just one word, like "hi"
    if (first.endsWith("\"") && first.length() > 1) {
      return sb.substring(1, sb.length() - 1);
    }

    while (scan.hasNext()) {
      String next = scan.next();
      sb.append(" ").append(next);
      if (next.endsWith("\"")) {
        return sb.substring(1, sb.length() - 1);
      }
    }

    throw new IllegalArgumentException("Never closed an open quote " + sb);
  }
}
