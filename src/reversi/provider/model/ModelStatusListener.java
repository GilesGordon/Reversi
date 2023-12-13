package reversi.provider.model;

import reversi.player.Player;

/**
 * A reversi.model.ModelStatusListener is the listener interface that corresponds with
 * ModelStatusFeatures. It allows the controller to listen to the model.
 */
public interface ModelStatusListener {
  /**
   * This allows the listener to do everything that it needs to do when the game ends such
   * as displaying a game over message.
   */
  void gameOver();

  /**
   * This sends a request to the corresponding player to take their turn.
   * The code should always check that the player passed
   * corresponds to the player associated with the controller before telling the player to take
   * their turn. Note that just because the listener tells the Player to take their turn does
   * not mean that the turn will immediately be taken.
   *
   * @param p the Player who needs to take their turn.
   */
  void notifyPlayerToTakeTurn(Player p);
}
