package reversi.player.strategies;

import java.util.ArrayList;
import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;


/**
 * This is an abstract class that provides functionality for using strategies.
 */
public abstract class AStrategy implements Strategy {

  ReadonlyReversiModel model;
  String type;

  public AStrategy(ReadonlyReversiModel model, String type) {
    this.model = model;
    this.type = type;
  }

  public abstract int[] moveValues(String player);

  // Best Move finds the move with the highest value, as determined by the moveValue methods in
  // the subclasses.
  @Override
  public Coordinate bestMove(String player) {
    int[] values = moveValues(player);
    if (values.length == 0) {
      return new Coordinate(-1, -1);
    }
    int highest = values[0];
    List<Coordinate> moves = model.allValidMoves(player);
    Coordinate best = moves.get(0);
    for (int i = 0; i < values.length; i++) {
      Coordinate current = moves.get(i);
      if (values[i] >= highest) {
        if (values[i] > highest) {
          highest = values[i];
          best = current;
        } else {
          if (current.r <= best.r) {
            if (current.r == best.r) {
              if (current.q < best.q) {
                best = current;
              }
            } else {
              best = current;
            }
          }
        }
      }
    }
    return best;
  }

  /**
   * Finds the list of corners in the game.
   * @return the list of corners in the board.
   */
  protected ArrayList<Coordinate> getCorners() {
    int x = model.getSize() - 1;
    ArrayList<Coordinate> corners = new ArrayList<>();
    if (type.equals("square")) {
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          corners.add(new Coordinate(i * x, j * x));
        }
      }
    } else {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          if (i != j) {
            corners.add(new Coordinate(i * x, j * x));
          }
        }
      }
    }
    return corners;
  }
}
