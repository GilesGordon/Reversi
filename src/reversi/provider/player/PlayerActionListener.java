package reversi.provider.player;

import reversi.provider.model.HexaCoordinate;

/**
 * A listener to a PlayActionFeature that supports communication from the publisher to
 * further the state of the Reversi game.
 */
public interface PlayerActionListener {
  /**
   * Mutate the model to place a tile at a given location for the current Player's turn.
   * Makes sure that the turn is being enacted for the right Player.
   *
   * @param hc The HexaCoordinate the Player wants to place a tile.
   */
  void placeTile(HexaCoordinate hc);

  /**
   * Mutate the model to pass the turn for the current Player's turn.
   * Makes sure that the turn is being enacted for the right Player.
   */
  void pass();
}
