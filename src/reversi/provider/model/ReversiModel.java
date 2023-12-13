package reversi.provider.model;

import reversi.player.Player;

/**
 * A reversi.model.ReversiModel is an interface which implements all methods needed for reversi.
 * This includes methods that mutate the state of the game.
 * This includes starting the game, passing a turn, or placing a tile for a turn.
 */
public interface ReversiModel extends ReversiModelReadOnly, ModelStatusFeatures {

  /**
   * A GameState is one of NOTSTARTED, WHITETURN, BLACKTURN, WHITETURN, WHITEWIN, TIE. IT
   * represents the state of the game.
   * > NOTSTARTED the game has not started yet.
   * > WHITETURN- it is whites turn.
   * > BLACKTURN- it is blacks turn.
   * > WHITEWIN- white has won the game.
   * > BLACKWIN- black has won the game.
   * > TIE - the game is over and there is no winner.
   */
  enum GameState {
    NOTSTARTED,
    WHITETURN,
    BLACKTURN,
    WHITEWIN,
    BLACKWIN,
    TIE
  }

  /**
   * A TileState defines what kind of piece is on a tile, it is one of WHITE, BLACK, EMPTY.
   * > WHITE- this space has a white piece on it.
   * > BLACK- this space has a black piece on it.
   * > EMPTY- this space has no piece on it.
   */
  enum TileState {
    WHITE,
    BLACK,
    EMPTY
  }

  /**
   * StartGame starts the game with the given gridSize, this game starts with two players.
   * The first player controls black and the second controls white.
   *
   * @param gridSize the amount of tiles between the center and the edge, this value cannot be
   *                 below 1.
   * @param player1  the object that will be making decisions for player 1.
   * @param player2  the object that will be making decisions for player 2.
   * @throws IllegalStateException    if the game has already started.
   * @throws IllegalArgumentException if either player is null or if both players are the same
   *                                  object.
   */
  void startGame(int gridSize, Player player1, Player player2);

  /**
   * Pass allows the current player to pass their turn to the other person. Note that if both
   * players pass in a row then the game ends, updating the gameState appropriately.
   *
   * @throws IllegalStateException if the game has not started or if the game has ended.
   */
  void pass();

  /**
   * PlaceTile places a piece at the given coordinates. You can only place
   * tiles in such a way that it would result in a line of the opponents pieces being between
   * two of your own pieces. Resets streak of passes in a row.
   *
   * @param coords The HexaCoordinate where the tile is to be placed.
   * @throws IllegalStateException if the game has not started, if the game has ended, or
   *                               the given coordinate is invalid (such as there already
   *                               being a tile there or not allowed by game rules).
   */
  void placeTile(HexaCoordinate coords);
}
