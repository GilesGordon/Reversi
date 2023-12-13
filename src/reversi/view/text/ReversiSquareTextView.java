package reversi.view.text;

import reversi.model.Coordinate;

import reversi.model.SquareReversiModel;
import reversi.player.TileState;

/**
 * Represnts a basic text view for a game of Reversi that has a square board.
 */
public class ReversiSquareTextView extends ReversiTextView {

  private final SquareReversiModel model;
  private TileState[][] boardState;

  public ReversiSquareTextView(SquareReversiModel model) {
    super(model);
    this.model = model;
  }

  @Override
  public String toString() {
    StringBuilder display = new StringBuilder();
    int size = model.getSize();

    for (int row = 0; row < size; row++) {
      display.append(" ");
      for (int q = 0; q < size; q++) {
        Coordinate currCoord = new Coordinate(row, q);
        display.append(stateToDisplay(model.getStateAt(currCoord)));

        if (q < this.getEndIndex(row, size) - 1) {
          display.append(" ");
        }
      }
      display.append("\n");
    }
    return display.toString();
  }





}
