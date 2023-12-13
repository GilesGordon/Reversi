package reversi.view.text;

import java.io.IOException;

import reversi.model.Coordinate;
import reversi.model.ReversiModel;

/**
 * A class for displaying a text-based view of reversi that is constructed with a model and renders
 * it as text with the render method.
 */
public class ReversiTextView implements TextView {

  private final ReversiModel model;

  /**
   * A constructor for the view that takes a reversi game state.
   */
  public ReversiTextView(ReversiModel model) {
    this.model = model;
  }

  /**
   * Renders the reversi state as text.
   */
  @Override
  public void render() throws IOException {
    System.out.print(this.toString());
  }

  /**
   * Produces the model as a string.
   */
  public String toString() {
    StringBuilder display = new StringBuilder();
    int maxLength = model.getSize() * 2 - 1;

    for (int row = 0; row < maxLength; row++) {
      display.append(" ".repeat(Math.max(0, this.getLeadingSpaces(row, maxLength))));
      for (int q = this.getStartIndex(row, maxLength); q < this.getEndIndex(row, maxLength); q++) {
        Coordinate currCoord = new Coordinate(row, q);
        display.append(stateToDisplay(model.getStateAt(currCoord)));

        if (q < this.getEndIndex(row, maxLength) - 1) {
          display.append(" ");
        }
      }
      display.append("\n");
    }
    return display.toString();
  }



  protected int getLeadingSpaces(int row, int maxLength) {
    return Math.abs(model.getSize() - row - 1);
  }

  // Returns 0 for the start index if the row is greater than size of model
  public int getStartIndex(int row, int maxLength) {
    return row < model.getSize() ? model.getSize() - 1 - row : 0;
  }

  protected int getEndIndex(int row, int maxLength) {
    return row < model.getSize() ? maxLength : maxLength + model.getSize() - row - 1;
  }

  // converts the state to a displayable symbol.
  protected String stateToDisplay(String state) {
    switch (state) {
      case "empty":
        return "_";
      case "white":
        return "X";
      case "black":
        return "O";
      default:
        throw new IllegalArgumentException("invalid state");
    }
  }
}
