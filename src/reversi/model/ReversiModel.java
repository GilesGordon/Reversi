package reversi.model;



import reversi.controller.ModelStatusFeatures;

/**
 * This is an interface to represent a reversi.model.Board.
 */
public interface ReversiModel extends ReadonlyReversiModel {

  /**
   * Sets the state at the given coordinates. The coordinate system depends on the game type.
   *
   * @param c the coordinate to set the state at
   * @param state is the state to set at the given coordinate
   * @throws IllegalArgumentException if q or r are out of bounds for coordinate system or
   *                                  if the state is invalid.
   */
  void setStateAt(Coordinate c, String state);

  /**
   * Does the move at the specified location and updates the game board accordingly. Move rules
   * are based on game implementation.
   *
   * @param c the coordinate to set the state at
   * @throws IllegalArgumentException if q or r are out of bounds for coordinate system or
   *                                  if the state is invalid.
   */
  void doMove(Coordinate c);

  /**
   * Sets the players passed status to true and makes the next player the current player.
   */
  void passTurn();

  void addFeatures(ModelStatusFeatures features);

  void startGame();
}


