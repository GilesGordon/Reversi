package reversi.provider.model;

import reversi.player.Player;

/**
 * A reversi.model.ModelStatusFeatures is part of the observer pattern, and details all the
 * relevant things the Model might do that the listeners (controller) would want to know about.
 * This namely includes telling the listeners that it is a given player's turn or that the
 * game is over.
 */
public interface ModelStatusFeatures {
  /**
   * Adds a listener to the view that will be notified regarding certain events by the
   * ModelStatusFeatures implementation.
   *
   * @param listener A ModelStatusListener of the view, likely a controller.
   */
  void addModelStatusListener(ModelStatusListener listener);

  /**
   * Publishes to all the listeners that the game is over.
   */
  void notifyGameOver();

  /**
   * Publishes to all the listeners that there has been a turn change due to a player taking
   * a turn in the game. It notifies the listeners whose turn it now currently is. A turn can
   * either be passing or placing a tile.
   *
   * @param p the Player whose turn it is.
   */
  void notifyTurnChange(Player p);
}
