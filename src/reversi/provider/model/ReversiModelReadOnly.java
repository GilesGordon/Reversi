package reversi.provider.model;

import java.util.ArrayList;

import reversi.player.Player;

/**
 * Represents a Reversi model that only offers observation methods regarding the state
 * of the Reversi game.
 */
public interface ReversiModelReadOnly {
  /**
   * IsGameOver checks to see if the game is over. The game is over if both players have passed
   * in a row or if there are no more empty tiles.
   *
   * @return if the game has ended or not.
   * @throws IllegalStateException if the game has not started.
   */
  boolean isGameOver();

  /**
   * Returns the current state of the game.
   *
   * @return The current state of the game.
   */
  ReversiModel.GameState getState();

  /**
   * GetBoardSize returns the board size as defined at the start of the game.
   *
   * @return the amount of tiles from the center to the edge, not counting the center.
   * @throws IllegalStateException if the game has not started.
   */
  int getBoardSize();

  /**
   * GetTileStateAt returns the TileState of the tile as determined by the args.
   * The r axis goes diagonal from the bottom right (positive) to the
   * * top left (negative). The q axis goes from the right (positive) to the left (negative).
   *
   * @param coords the q and r axis coordinates of the tile at a HexaCoordinate.
   * @return the TitleState of the tile at the supplied location.
   * @throws IllegalStateException if the game has not started or if
   *                               the coordinates are not a valid coordinate on the board.
   */
  ReversiModel.TileState getTileStateAt(HexaCoordinate coords);

  /**
   * If the game is over, returns the winner state or tie state and updates the model's state
   * accordingly. Otherwise, throws an error if the game is not over.
   *
   * @return State of the game after game is over.
   * @throws IllegalStateException if the game is not over.
   */
  ReversiModel.GameState getWinner();

  /**
   * getBoard returns an unmutatable copy of the board at the current state of the game.
   *
   * @return a copy of the current board.
   * @throws IllegalStateException if the game has not started
   */
  ArrayList<ArrayList<ReversiModel.TileState>> getBoard();

  /**
   * isMoveValid checks if a move is valid at a given HexaCoordinate. Returns false if the move
   * is not valid or if it is not a current players turn.
   *
   * @param coords a HexaCoordinate representing the space to be checked.
   * @return a boolean representing whether the space is a valid space to place a piece for
   *         the current player.
   * @throws IllegalArgumentException if the given coordinates are null.
   * @throws IllegalStateException    if the game has not started.
   **/

  boolean isMoveValid(HexaCoordinate coords);

  /**
   * getScoreForPlayer takes a player and returns their score at the current state of the game.
   *
   * @param player The player whose score is being checked.
   * @return the players score.
   * @throws IllegalStateException    if the game has ended.
   * @throws IllegalArgumentException if the given player is not one of the players in the game
   *                                  or if it is null.
   */
  int getScoreForPlayer(Player player);

  /**
   * anyLegalMoves checks to make sure the current player has any legal moves.
   * Can only be called if it is a current player's turn game state.
   *
   * @return true if the player has any legal moves.
   * @throws IllegalStateException if the game has not started.
   */
  boolean anyLegalMoves();

  /**
   * getNumberOfTilesToFlipAtCoord was made as an observation meathod that would be common for
   * strategy implementation.
   *
   * @param coords the coordinates that you are checking the result for.
   * @return the number of tiles that would be flipped if you placed a tile at that coordinate.
   * @throws IllegalStateException    if the game has not started.
   * @throws IllegalArgumentException if the coordinates are not on the board.
   */
  int getNumberOfTilesToFlipAtCoord(HexaCoordinate coords);

  /**
   * Creates a copy of the current game model exactly at its current state including the
   * layout of the board, the players involved, the passes made, and the state of the game.
   * Can be called at any point in the game.
   *
   * @return A copy of the game model.
   */
  ReversiModel copyGame();

  /**
   * getCorners identifies the coordinates of the corners and compiles them into a list.
   *
   * @return the corner coordinates of the board in an arraylist.
   * @throws IllegalStateException if the game has not started.
   */
  ArrayList<HexaCoordinate> getCorners();

  /**
   * getAllPossibleMoves identifies any possible move that the current player could make.
   *
   * @return a list of any possible move the player could make.
   * @throws IllegalStateException if the game has not started
   */
  ArrayList<HexaCoordinate> getAllPossibleMoves();
}
