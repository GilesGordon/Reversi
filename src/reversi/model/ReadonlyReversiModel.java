package reversi.model;

import java.util.List;

import reversi.player.IPlayer;

/**
 * This is an interface to represent a reversi.model.Board.
 */
public interface ReadonlyReversiModel {

  /**
   * Returns the state at the given coordinates. The coordinate system depends on the game type.
   *
   * @param c the coordinate to set the state at
   * @return the String representing the given state
   * @throws IllegalArgumentException if q or r are out of bounds for coordinate system
   */
  String getStateAt(Coordinate c);

  /**
   * Gives the size of the board. Definition of size depends on class implementation
   *
   * @return an int representing the size of the board.
   */
  int getSize();

  /**
   * Returns the list of coordinate groups that represent a tile that would be a valid
   * move for the given state.
   *
   * @param state is the state of the move to be tested.
   * @return a list of the valid move pairs.
   */
  List<Coordinate> allValidMoves(String state);

  /**
   * A copy of the current game board.
   *
   * @return an array of arrays of Strings representing the current state of the game.
   */
  String[][] getBoardCopy();

  /**
   * Converts a 2d array index to the corresponding coordinate of a hexagonal grid.
   *
   * @param i1 the row of the 2d array.
   * @param i2 the column of the 2d array
   * @return the coordinate of a hexagonal grid corresponding to the array index.
   */
  Coordinate indexToCoord(int i1, int i2);

  /**
   * Gets the score of the given player.
   *
   * @param state the state representing the player to get the score of
   * @return an int representing the score of the player
   */
  int getScore(String state);

  /**
   * Gets the player whose turn it currently is.
   *
   * @return the current player
   */
  String getCurrentPlayer();

  /**
   * Gets a copy of the list of players.
   *
   * @return a copy of the list of players
   */
  IPlayer[] getPlayersCopy();

  /**
   * Checks if the game has ended according to the rules of the model implementation.
   *
   * @return true if the game is over.
   */
  boolean getGameOver();

  /**
   * Provides an account of every color used by players in game.
   * @return an array of the names of the Colors used in the game.
   */
  String[] getColors();

  /**
   * Checks if the player defined by the given state can legally move to the given Coordinate.
   * Returns 0 if false and the number of pieces flipped by the move if true.
   *
   * @param c is the coordinate to move to
   * @param state is the state of the player doing the move
   * @return the number of pieces flipped if the player can move to the given coordinate.
   */
  int isLegalMove(Coordinate c, String state);
}