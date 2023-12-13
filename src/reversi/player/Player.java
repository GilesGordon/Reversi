package reversi.player;

import reversi.controller.PlayerActions;
import reversi.model.Coordinate;
import reversi.player.strategies.Strategy;



/**
 * Represents a Player for Reversi game. A player has a TileState, a Score,
 * a boolean that represents if they have passed as their most recent move, as well as a Strategy.
 */
public class Player implements IPlayer {
  private final TileState state;
  private int score;
  private boolean passedLastTurn;
  private PlayerActions features;
  private Strategy strat;

  /**
   * Constructor for a Player.
   * @param state the TileState of the requested Player.
   */
  public Player(TileState state) {
    this.state = state;
    this.score = 0;
    this.passedLastTurn = false;
    this.strat = null;
  }


  @Override
  public String getState() {
    return state.toString();
  }



  @Override
  public int getScore() {
    return score;
  }


  @Override
  public void setScore(int score) {
    this.score = score;
  }


  @Override
  public boolean getPassed() {
    return passedLastTurn;
  }

  @Override
  public void setPassed(boolean passed) {
    this.passedLastTurn = passed;
  }

  // Uses bestMove to determine the "smartest" move
  @Override
  public Coordinate strategicMove() {
    if (strat != null) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException ignored) {
      }
      Coordinate c = this.strat.bestMove(this.getState());
      if (c.q == -1 && c.r == -1) {
        if (features != null) {
          features.requestPass();
        }
      }
      if (features != null) {
        features.requestDoMove(c);
      }
      return c;
    } else {
      return new Coordinate(0, 0);
    }
  }


  @Override
  public void setStrat(Strategy strat) {
    this.strat = strat;
  }

  @Override
  public void addFeatures(PlayerActions features) {
    this.features = features;
  }
}
