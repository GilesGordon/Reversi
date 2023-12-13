package reversi.model;


import java.util.Arrays;

import reversi.player.IPlayer;

import reversi.player.TileState;


/**
 * Represents a square board version of a game of Reversi.
 */
public class SquareReversiModel extends AReversiModel {

  /**
   * Basic size 4 board constructor with custom players.
   */
  public SquareReversiModel(IPlayer[] players, int boardSize) throws IllegalArgumentException {
    super(players, boardSize);
  }

  @Override
  protected boolean validateShape(TileState[][] boardState) {
    return true;
  }

  // helper method for the constructor that initializes the board as empty and then places the
  // starting pieces.
  @Override
  protected void initialStates(int boardSize) {
    if (boardSize % 2 != 0) {
      throw new IllegalArgumentException("not even board");
    }
    boardState = new TileState[boardSize][];
    for (int countRow = 0; countRow < boardSize; countRow++) {
      TileState[] currentRow = new TileState[boardSize];
      Arrays.fill(currentRow, TileState.empty);
      boardState[countRow] = currentRow;
    }
    startingPieces(boardSize);
  }

  // places the starting pieces for a hexagonal board.
  @Override
  protected void startingPieces(int boardSize) {
    boardState[boardSize / 2 - 1][boardSize / 2 - 1] = TileState.black;
    boardState[boardSize / 2 - 1][boardSize / 2] = TileState.white;
    boardState[boardSize / 2][boardSize / 2 - 1] = TileState.white;
    boardState[boardSize / 2][boardSize / 2] = TileState.black;
  }

  @Override
  public void setStateAt(Coordinate c, String state) {
    if (!(state.equals("empty") || state.equals("black") || state.equals("white"))) {
      throw new IllegalArgumentException("setting an invalid state");
    }
    if (this.checkOutOfBounds(c)) {
      throw new IllegalArgumentException("Provided cooridnates are invalid");
    }
    this.boardState[c.q][c.r] = TileState.valueOf(state);
  }

  @Override
  public String getStateAt(Coordinate c) {
    if (this.checkOutOfBounds(c)) {
      throw new IllegalArgumentException("Provided coordinates are invalid");
    }
    return this.boardState[c.q][c.r].toString();
  }

  // Makes sure that the Coordinates provided fit within the bounds of the current boardState
  @Override
  protected boolean checkOutOfBounds(Coordinate c) {
    return c.q < 0 || c.q > size - 1 || c.r < 0 || c.r > size - 1;
  }

  @Override
  public void doMove(Coordinate c) {
    this.gameOver();
    if (isGameOver) {
      this.modelRequestDisplayMessage("Game Over");
    }
    if ((isLegalMove(c, getCurrentPlayer()) > 0) &&  (!isGameOver)) {
      setStateAt(c, getCurrentPlayer());
      for (int i = -1; i < 2; i++) {
        for (int j = -1; j < 2; j++) {
          // does the move in all eight directions
          if (!(i == 0 && j == 0)) {
            flipInDirection(c, i, j, checkDirection(c, i, j, getCurrentPlayer()));
          }
        }
      }
      adjustScores();
      players[currentPlayer].setPassed(false);
      nextPlayer();

    } else {
      this.modelRequestDisplayMessage("Cannot perform move at given coordinates.");
      throw new IllegalArgumentException("Cannot perform move at given coordinates.");
    }
    this.gameOver();
    if (isGameOver) {
      this.modelRequestDisplayMessage("Game Over");
    }
  }

  @Override
  public int isLegalMove(Coordinate c, String state) {
    if (this.checkOutOfBounds(c)) {
      return 0;
    }
    if (!(getStateAt(c).equals(TileState.empty.toString()))) {
      return 0;
    }
    return checkDirection(c, 1, 0, state) + checkDirection(c, 0, 1, state)
            + checkDirection(c, -1, 0, state) + checkDirection(c, 0, -1, state)
            + checkDirection(c, 1, -1, state) + checkDirection(c, -1, 1, state)
            + checkDirection(c, 1, 1, state) + checkDirection(c, -1, -1, state);
  }

  @Override
  public Coordinate indexToCoord(int i1, int i2) {
    return new Coordinate(i1, i2);
  }

}
