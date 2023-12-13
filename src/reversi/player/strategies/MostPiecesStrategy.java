package reversi.player.strategies;

import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.player.strategies.AStrategy;

/**
 * MostPiecesStrategy evaluates possible moves based on how many tiles the move flips.
 */
public class MostPiecesStrategy extends AStrategy {

  /**
   * Constructor for MostPiecesStrategy.
   * @param model the model the strategy evaluates on.
   */
  public MostPiecesStrategy(ReadonlyReversiModel model, String type) {
    super(model, type);
  }

  // Every move in the array gets a value, the value is the amount of pieces the move flips.
  @Override
  public int[] moveValues(String player) {
    List<Coordinate> moves = model.allValidMoves(player);
    int[] values = new int[moves.size()];
    for (int i = 0; i < moves.size(); i++) {
      values[i] = model.isLegalMove(moves.get(i), player);
    }
    return values;
  }
}
