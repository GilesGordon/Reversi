package reversi.player.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;

/**
 * CombinedStrategy compares requested strategies run on valid moves to
 * determine the best over all move, considering potentially multiple strategies.
 */
public class CombinedStrategy extends AStrategy {
  ArrayList<Strategy> strats;

  /**
   * Constructor for Combined Strategy.
   * @param model the model
   * @param strats every strategy that the user wants to evaluate on every possible move.
   */
  public CombinedStrategy(ReadonlyReversiModel model, String type , ArrayList<Strategy> strats) {
    super(model, type);
    //strats = new ArrayList<>();
    //strats.addAll(Arrays.asList(strategies));
    this.strats = strats;
  }

  // Runs every strategy from strats on every valid move. Adds the scores from
  // every strategy for each possible move together.
  @Override
  public int[] moveValues(String player) {
    List<Coordinate> moves = model.allValidMoves(player);
    int[] values = new int[moves.size()];
    Arrays.fill(values, 0);
    for (Strategy s : strats) {
      int[] valuesToAdd = s.moveValues(player);
      for (int i = 0; i < values.length; i++) {
        values[i] += valuesToAdd[i];
      }
    }
    return values;
  }
}
