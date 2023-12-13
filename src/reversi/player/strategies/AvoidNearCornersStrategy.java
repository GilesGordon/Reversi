package reversi.player.strategies;

import java.util.ArrayList;
import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.player.strategies.AStrategy;

/**
 * AvoidNearCornerStrategy evaluates moves based on if they are adjacent to a corner or not.
 */
public class AvoidNearCornersStrategy extends AStrategy {

  /**
   * The constructor for AvoidNearCornersStrategy calls for a model.
   * @param model the model.
   */
  public AvoidNearCornersStrategy(ReadonlyReversiModel model, String type) {
    super(model, type);
  }

  // Asssigns values to each move, Coordinates that are adjacent to a corner receive a -2 score.
  @Override
  public int[] moveValues(String player) {
    List<Coordinate> moves = model.allValidMoves(player);
    int[] values = new int[moves.size()];
    ArrayList<Coordinate> corners = this.getCorners();
    // gets the array of values. A corner adjacent tile receives a -2 score. Other tiles are 0.
    for (int i = 0; i < moves.size(); i++) {
      Coordinate current = moves.get(i);
      // default 0
      values[i] = 0;
      for (Coordinate c : corners) {
        if (Math.abs(current.q - c.q) <= 1 && Math.abs(current.r - c.r) <= 1
                && (current.r - c.r != current.q - c.q)) {
          values[i] = -2;
          break;
        }
      }
    }
    return values;
  }
}