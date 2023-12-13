package reversi.player.strategies;

import java.util.ArrayList;
import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.player.strategies.AStrategy;

/**
 * GetCornersStrategy evaluates possible moves, giving credit to them if they are a corner.
 */
public class GetCornersStrategy extends AStrategy {

  /**
   * Constructor for GetCornersStrategy.
   * @param model the model the strategy evaluates.
   */
  public GetCornersStrategy(ReadonlyReversiModel model, String type) {
    super(model, type);
  }

  // Gives moves that end in a corner 2 points.
  @Override
  public int[] moveValues(String player) {
    List<Coordinate> moves = model.allValidMoves(player);
    int[] values = new int[moves.size()];
    ArrayList<Coordinate> corners = this.getCorners();
    // gets the array of values. A corner receives a score of 2. Other tiles are 0.
    for (int i = 0; i < moves.size(); i++) {
      Coordinate current = moves.get(i);
      // default 0
      values[i] = 0;
      for (Coordinate c : corners) {
        if (current.equals(c)) {
          values[i] = 2;
          break;
        }
      }
    }
    return values;
  }
}
