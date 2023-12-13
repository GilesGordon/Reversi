package reversi.provider.player;

import reversi.provider.model.HexaCoordinate;

/**
 * Actions that a Player may take to advance the state of the Reversi game by notifying
 * listeners (PlayerActionListener) of the interface.
 */
public interface PlayerActionFeatures {
  /**
   * Adds a listener to the player actions. Handles players making a move in the game of reversi.
   *
   * @param listener A PlayerActionListener of the player actions, likely a controller.
   */
  void addPlayerActionListener(reversi.provider.player.PlayerActionListener listener);

  /**
   * Publishes to all the listeners that this player would like to pass their turn.
   */
  void passPlayerTurn();

  /**
   * Publishes to all the listeners that this player would like to place a tile at a given
   * location.
   *
   * @param hc The HexaCoordinate that the tile should be placed at.
   */
  void placePlayerTile(HexaCoordinate hc);
}
