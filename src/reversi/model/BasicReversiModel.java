package reversi.model;


import java.util.Arrays;




import reversi.player.IPlayer;

import reversi.player.TileState;


/**
 * This represents a basic reversi game, in which the board is hexagonal and symmetrical. This
 * version of the game is implemented with a hexagonal board, so each tile is referenced using axial
 * coordinates. In this system, the 'r' axis increases from top-left to bottom right, and the 'q'
 * axis increases to the right. Coordinate 0, 0 is the hypothetical tile if the board was a complete
 * parallelogram with the top-left and bottom-right corners filled with tiles, however, there is no
 * 0, 0 coordinate with the hexagonal board.
 */
public class BasicReversiModel extends AReversiModel {

  /**
   * Default constructor for the Reversi Model that returns a board size of 4.
   */
  public BasicReversiModel() {
    super();
  }

  /**
   * Basic size 4 board constructor with custom players.
   */
  public BasicReversiModel(IPlayer[] players) {
    super(players);
  }

  /**
   * Constructor that takes in a board size and produces a board of that size.
   */
  public BasicReversiModel(int boardSize) throws IllegalArgumentException {
    super(boardSize);
  }

  /**
   * Basic size 4 board constructor with custom players.
   */
  public BasicReversiModel(IPlayer[] players, int boardSize) throws IllegalArgumentException {
    super(players, boardSize);
  }


  /**
   * Constructor that takes a board with custom initial states and makes sure it's a valid shape.
   */
  public BasicReversiModel(TileState[][] boardState) throws IllegalArgumentException {
    super(boardState);
  }

  // Validates Shape of the TileState Argument provided in Constructor to ensure that the Shape
  // of the provided boardState is hexagonal and symmetrical. (class invariant)
  @Override
  protected boolean validateShape(TileState[][] boardState) {
    int potentialBoardSize = boardState[0].length;
    int maxLength = (potentialBoardSize * 2) - 1;
    // countRow starts at 1 and not 0, because potentialBoardSize already is the 0 row in array
    for (int countRow = 1; countRow < maxLength; countRow++) {
      if (countRow < potentialBoardSize) {
        if (boardState[countRow].length != potentialBoardSize + countRow) {
          return false;
        }
      } else {
        if (boardState[countRow].length != potentialBoardSize + (maxLength - countRow) - 1) {
          return false;
        }
      }
    }
    return true;
  }

  // helper method for the constructor that initializes the board as empty and then places the
  // starting pieces.
  @Override
  protected void initialStates(int boardSize) {
    int maxLength = (boardSize * 2) - 1;
    boardState = new TileState[maxLength][];
    for (int countRow = 0; countRow < maxLength; countRow++) {
      TileState[] currentRow;
      if (countRow < boardSize) {
        currentRow = new TileState[boardSize + countRow];
      } else {
        currentRow = new TileState[boardSize + (maxLength - countRow) - 1];
      }
      Arrays.fill(currentRow, TileState.empty);
      boardState[countRow] = currentRow;
    }
    startingPieces(boardSize);
  }

  // places the starting pieces for a hexagonal board.
  @Override
  protected void startingPieces(int boardSize) {
    boardState[boardSize - 2][boardSize - 2] = TileState.black;
    boardState[boardSize - 2][boardSize - 1] = TileState.white;
    boardState[boardSize - 1][boardSize - 2] = TileState.white;
    boardState[boardSize - 1][boardSize] = TileState.black;
    boardState[boardSize][boardSize - 2] = TileState.black;
    boardState[boardSize][boardSize - 1] = TileState.white;
  }

  @Override
  public void setStateAt(Coordinate c, String state) {
    if (!(state.equals("empty") || state.equals("black") || state.equals("white"))) {
      throw new IllegalArgumentException("setting an invalid state");
    }
    if (this.checkOutOfBounds(c)) {
      throw new IllegalArgumentException("Provided cooridnates are invalid");
    }
    int max = 0;
    if (size - 1 - c.r > max) {
      max = size - 1 - c.r;
    }
    this.boardState[c.r][c.q - max] = TileState.valueOf(state);
  }

  @Override
  public String getStateAt(Coordinate c) {
    if (this.checkOutOfBounds(c)) {
      throw new IllegalArgumentException("Provided coordinates are invalid");
    }
    int max = 0;
    if (size - 1 - c.r > max) {
      max = size - 1 - c.r;
    }
    return this.boardState[c.r][c.q - max].toString();
  }

  // Makes sure that the Coordinates provided fit within the bounds of the current boardState
  @Override
  protected boolean checkOutOfBounds(Coordinate c) {
    if (c.r > (size * 2) - 2) {
      return true;
    }
    if (c.r < 0) {
      return true;
    }
    int bottomQ = 0;
    int topQ = (this.size * 2) - 2;
    if (size - 1 - c.r > 0) {
      bottomQ = size - 1 - c.r;
    }
    if (c.r >= size) {
      topQ = topQ - (c.r - (size - 1));
    }
    return c.q < bottomQ || c.q > topQ;
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
          // does the move in all six directions
          if (i != j) {
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
            + checkDirection(c, 1, -1, state) + checkDirection(c, -1, 1, state);
  }

  @Override
  public Coordinate indexToCoord(int i1, int i2) {
    int max = 0;
    if (size - 1 - i1 > max) {
      max = size - 1 - i1;
    }
    return new Coordinate(i2 + max, i1);
  }
}


