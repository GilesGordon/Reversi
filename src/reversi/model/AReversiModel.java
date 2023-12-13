package reversi.model;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import reversi.controller.ModelStatusFeatures;
import reversi.player.IPlayer;
import reversi.player.Player;
import reversi.player.TileState;
import reversi.player.strategies.MostPiecesStrategy;

/**
 * This is an abstract class that provides basic functionality that cna be used by different game
 * board implementations of a Reversi game.
 */
public abstract class AReversiModel implements ReversiModel {

  // an array of arrays containing the state of each tile of the game.
  protected TileState[][] boardState;
  // Size of one side of the board
  protected int size;
  //the players of the game
  protected IPlayer[] players;
  //the index of the player array representing the current player.
  protected int currentPlayer;

  private List<ModelStatusFeatures> features = new ArrayList<>();

  protected boolean isGameOver = false;


  /**
   * Default constructor for the Reversi Model that returns a board size of 4.
   */
  public AReversiModel() {
    this.initialStates(4);
    this.size = 4;
    initializePlayers();
  }

  /**
   * Basic size 4 board constructor with custom players.
   */
  public AReversiModel(IPlayer[] players) {
    this.players = Objects.requireNonNull(players);
    if (players.length != 2) {
      throw new IllegalArgumentException("must have two players");
    }
    this.initialStates(4);
    this.size = 4;
    currentPlayer = 0;
  }

  /**
   * Constructor that takes in a board size and produces a board of that size.
   */
  public AReversiModel(int boardSize) throws IllegalArgumentException {
    if (boardSize < 3) {
      throw new IllegalArgumentException("Size must be three or greater");
    }
    this.size = boardSize;
    this.initialStates(boardSize);
    initializePlayers();
  }

  /**
   * Constructor that takes a board with custom initial states and makes sure it's a valid shape.
   */
  public AReversiModel(TileState[][] boardState) throws IllegalArgumentException {
    // add method to validate shape.
    if (boardState[0].length < 3) {
      throw new IllegalArgumentException("Size must be three or greater");
    }
    if (!this.validateShape(boardState)) {
      throw new IllegalArgumentException("Provided invalid shape");
    }
    this.size = boardState[0].length;
    this.boardState = boardState;
    initializePlayers();
  }

  /**
   * Basic size 4 board constructor with custom players.
   */
  public AReversiModel(IPlayer[] players, int boardSize) throws IllegalArgumentException {
    this.players = Objects.requireNonNull(players);
    if (players.length != 2) {
      throw new IllegalArgumentException("must have two players");
    }
    if (boardSize < 3) {
      throw new IllegalArgumentException("Size must be three or greater");
    }
    this.size = boardSize;
    this.initialStates(boardSize);
    this.currentPlayer = 0;
  }

  // method to start the game with two players
  private void initializePlayers() {
    players = new IPlayer[2];
    players[0] = new Player(TileState.black);
    players[1] = new Player(TileState.white);
    players[0].setStrat(new MostPiecesStrategy(this, "hex"));
    players[1].setStrat(new MostPiecesStrategy(this, "hex"));
    currentPlayer = 0;
  }

  // Validates Shape of the TileState Argument based on the model
  protected abstract boolean validateShape(TileState[][] boardState);

  // helper method for the constructor that initializes the board as empty and then places the
  // starting pieces.
  protected abstract void initialStates(int boardSize);

  // places the starting pieces for a board.
  protected abstract void startingPieces(int boardSize);

  /**
   * This method sets the state at the given coordinates.
   *
   * @param c     the coordinate to set the state at
   * @param state the state to be set
   */
  @Override
  public abstract void setStateAt(Coordinate c, String state);

  /**
   * This method gets the state at the given coordinates.
   *
   * @param c the coordinate to set the state at
   * @return the String representing the given tile state
   */
  @Override
  public abstract String getStateAt(Coordinate c);

  // Makes sure that the Coordinates provided fit within the bounds of the current boardState
  protected abstract boolean checkOutOfBounds(Coordinate c);

  /**
   * Gives the board size, representing the length of onne edge of the board.
   *
   * @return the int representing the size
   */
  @Override
  public int getSize() {
    return this.size;
  }

  /**
   * Do move places the tile if there's a valid move and flips any lines of the opposite state tile
   * to the given state.
   *
   * @param c     the coordinate to set the state at
   */
  @Override
  public abstract void doMove(Coordinate c);

  //Overrides Pass Turn
  @Override
  public void passTurn() {
    if (!isGameOver) {
      players[currentPlayer].setPassed(true);
      nextPlayer();
    }
    this.gameOver();
    if (isGameOver) {
      this.modelRequestDisplayMessage("Game Over");
    }
  }

  // Changes currentPlayer to represent next Player
  protected void nextPlayer() {
    if (currentPlayer == players.length - 1) {
      currentPlayer = 0;
    } else {
      currentPlayer++;
    }
    this.modelRequestDisplayMessage("It's your turn!");
  }

  // resets the players scores to their proper numbers
  protected void adjustScores() {
    for (IPlayer p : players) {
      int score = 0;
      for (int row = 0; row < boardState.length; row++) {
        for (int col = 0; col < boardState[row].length; col++) {
          if (p.getState().equals(getStateAt(indexToCoord(row, col)))) {
            score++;
          }
        }
      }
      p.setScore(score);
    }
  }

  /**
   * Gives the list of coordinate pairs that represent a tile that would be a valid
   * move for the given state.
   *
   * @param state is the state of the move to be tested.
   * @return a list of the valid move pairs.
   */
  @Override
  public List<Coordinate> allValidMoves(String state) {
    List<Coordinate> coords = new ArrayList<>();
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        if (isLegalMove(indexToCoord(i, j), state) > 0) {
          Coordinate tile = indexToCoord(i, j);
          coords.add(tile);
        }
      }
    }
    return coords;
  }

  // Gives A copy of the board in the form of A String[][]
  @Override
  public String[][] getBoardCopy() {
    String[][] copy = new String[boardState.length][];
    for (int i = 0; i < boardState.length; i++) {
      copy[i] = new String[boardState[i].length];
      for (int j = 0; j < boardState[i].length; j++) {
        copy[i][j] = boardState[i][j].toString();
      }
    }
    return copy;
  }

  // helper for doMove that flips the specified number of disks in the specified direction,
  // skipping the starting disk.
  protected void flipInDirection(Coordinate c, int qDir, int rDir, int numToFlip) {
    int currentQ = c.q + qDir;
    int currentR = c.r + rDir;
    Coordinate currentCoord = new Coordinate(currentQ, currentR);
    for (int i = 0; i < numToFlip; i++) {
      String oppositeState = oppositeState(getStateAt(currentCoord));
      setStateAt(currentCoord, oppositeState);
      currentQ = currentQ + qDir;
      currentR = currentR + rDir;
      currentCoord = new Coordinate(currentQ, currentR);
    }
  }

  // Returns the state opposite of the given state
  private String oppositeState(String state) {
    if (state.equals("white")) {
      return "black";
    } else {
      return "white";
    }
  }

  // Takes the tile coordinates, direction, and state, and returns the number flipped,
  // returning 0 if it is not valid.
  protected int checkDirection(Coordinate c, int qInc, int rInc, String state) {
    int currentQ = c.q + qInc;
    int currentR = c.r + rInc;
    if (checkOutOfBounds(new Coordinate(currentQ, currentR))) {
      return 0;
    }
    int countTiles = 0;
    String otherState = oppositeState(state);
    while (getStateAt(new Coordinate(currentQ, currentR)).equals(otherState)) {
      countTiles++;
      currentQ = currentQ + qInc;
      currentR = currentR + rInc;
      if (checkOutOfBounds(new Coordinate(currentQ, currentR))) {
        return 0;
      }
    }
    if (getStateAt(new Coordinate(currentQ, currentR)).equals(state)) {
      return countTiles;
    } else {
      return 0;
    }
  }


  // Checks if the specific move is legal according to the rules specified in do move. returns 0 if
  // false and the number of pieces flipped if true.
  @Override
  public abstract int isLegalMove(Coordinate c, String state);

  // Converts the index of an array to a Coordinate
  @Override
  public abstract Coordinate indexToCoord(int i1, int i2);

  // Returns the score, by how many spots on the boardState have the given state
  @Override
  public int getScore(String state) {
    int score = 0;
    for (int row = 0; row < boardState.length; row++) {
      for (int col = 0; col < boardState[row].length; col++) {
        if (getStateAt(indexToCoord(row, col)).equals(state)) {
          score++;
        }
      }
    }
    return score;
  }

  //Returns the current Player (aka whoose turn it is)
  @Override
  public String getCurrentPlayer() {
    return players[currentPlayer].getState();
  }


  // Returns a copy of the current players
  @Override
  public IPlayer[] getPlayersCopy() {
    IPlayer[] copy = new IPlayer[players.length];
    System.arraycopy(players, 0, copy, 0, players.length);
    return copy;
  }

  @Override
  public boolean getGameOver() {
    return isGameOver;
  }

  // Uses if the both players have true for getPassed to determine if the game is over

  // Updates Model Stus regarding isGameOver and passes if there are no valid moves for a player
  protected void gameOver() {
    int bothPassed = 0;
    for (IPlayer p : players) {
      if (p.getPassed()) {
        bothPassed++;
        if (bothPassed == 2) {
          this.isGameOver = true;
        }
      }
    }
    if (allValidMoves(getCurrentPlayer()).isEmpty()) {
      players[currentPlayer].setPassed(true);
      nextPlayer();
    }
  }

  // For now, returns just "black" and "white"
  @Override
  public String[] getColors() {
    String[] colorsInGame = new String[players.length];
    for (int i = 0; i < players.length; i++) {
      colorsInGame[i] = players[i].getState();
    }
    return colorsInGame;
  }

  @Override
  public void addFeatures(ModelStatusFeatures f) {
    this.features.add(f);
  }

  protected void modelRequestDisplayMessage(String s) {
    for (ModelStatusFeatures f: features) {
      f.requestDisplayMessage(s);
    }
  }

  protected void modelRequestDisplayBoard() {
    for (ModelStatusFeatures f: features) {
      f.requestDisplayBoard();
    }
  }




  @Override
  public void startGame() {

    this.modelRequestDisplayMessage("It's your turn!");

  }

}

