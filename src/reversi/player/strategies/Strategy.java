package reversi.player.strategies;

import reversi.model.Coordinate;

/**
 * Evaluates how good of a choice a potential move is.
 */
public interface Strategy {
  /**
   * Gives a list representing the value of each move given by the array of valid moves in
   * terms of the specific strategy implementation.
   *
   * @param player is the player for which to get the values.
   * @return the array of values of each move.
   */
  int[] moveValues(String player);

  /**
   * Gives the index of the Coordinate that represents the best move for the strategy
   * implementation. Ties are won by the upper-left coordinate. Returns the coordinate -1, -1 if
   * there are no valid moves.
   *
   * @param player is the player for which to get the Coordinate.
   * @return the index representing the best move.
   */
  Coordinate bestMove(String player);
}
