package reversi.player;

import reversi.controller.PlayerActions;
import reversi.model.Coordinate;
import reversi.player.strategies.Strategy;

/**
 * Repersents a Player in Reversi and provides methods for the model, as well as others,
 * to access a Player.
 */
public interface IPlayer {

  /**
   * Provides the TileState of a player.
   * @return the TileState of a player represented as a string.
   */
  String getState();

  /**
   * Provides the current score a Player is at.
   * @return a Player's current score.
   */
  int getScore();

  /**
   * Sets a Player's current score.
   * @param score the Player's new score.
   */
  void setScore(int score);

  /**
   * Provides information on if a Player has passed this round.
   * @return true if the Player's most recent move was to pass.
   */
  boolean getPassed();

  /**
   * Provides information to a Player regarding if they have passed.
   * @param passed boolean that represents if a Player has passed.
   */
  void setPassed(boolean passed);

  /**
   * Provides information on the "smartest" move for a Player to make, using their set strategy.
   * @return Coordinate that represents the "smartest" {@doMove} for the Player to make.
   */
  Coordinate strategicMove() ;

  /**
   * Sets a Player's current Strategy.
   * @param strat the strategy the user wants to set.
   */
  void setStrat(Strategy strat);

  void addFeatures(PlayerActions features);


}


