import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import reversi.model.BasicReversiModel;
import reversi.model.Coordinate;
import reversi.model.MockStrategies;
import reversi.player.strategies.AvoidNearCornersStrategy;
import reversi.player.strategies.GetCornersStrategy;
import reversi.player.IPlayer;
import reversi.player.strategies.MostPiecesStrategy;
import reversi.player.Player;
import reversi.player.TileState;

import reversi.view.text.ReversiTextView;

/**
 * This class provides a set of basic examples to show the user the basic functionality of the
 * ReversiModel interface as found in BasicReversi. If you are looking for simple demonstration on
 * the test for Player methods, see testPlayers.
 */
public class TestExamples {


  private BasicReversiModel simpleStarting;

  @Before
  public void init() {

    // This is a Basic, zero arg constructor, it defualts size to 4
    simpleStarting = new BasicReversiModel();

    // This is a Basic constructor with one argument, a board size as 5. A board size is one of
    // the six edges of the board of the board
    BasicReversiModel sizeProvidedStarting = new BasicReversiModel(5);

    // This is a rough copy of the initalStates method from the Model, we use it to make the
    // providedBoard array in the test class that we will provide to the BasicReversiModel
    // constructor with the board as an arg
    int boardSize = 3;
    int maxLength = (boardSize * 2) - 1;
    TileState[][] providedBoard = new TileState[maxLength][];
    for (int countRow = 0; countRow < maxLength; countRow++) {
      TileState[] currentRow;
      if (countRow < boardSize) {
        currentRow = new TileState[boardSize + countRow];
      } else {
        currentRow = new TileState[boardSize + (maxLength - countRow) - 1];
      }
      Arrays.fill(currentRow, TileState.empty);
      providedBoard[countRow] = currentRow;
    }

    // This is a Basic constructor that takes a valid hexagon symmetrical board with a size of 3
    // (the minimum board size). If a valid board is not put here, the constructor will not
    // construct.
    BasicReversiModel hexagonProvidedStarting = new BasicReversiModel(providedBoard);

  }

  private Coordinate makeCordinate(int q, int r) {
    return new Coordinate(q, r);
  }

  // This is a basic example of how to use getStateAt. The Constructor constructs the hexagon
  // to have a Tile State of "empty" at 3,0. We use getState to return this Tile State from
  // this given cordinate
  @Test
  public void testGetStateAt() {
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "empty");
  }

  // This is a basic Example of how to use  setState at. In this example there is a
  // TileState of "empty" at 3,0, but then we use setState to set that state at that coordinate to
  // "black"
  @Test
  public void testSetStateAt() {
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "empty");
    simpleStarting.setStateAt(makeCordinate(3, 0), "black");
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "black");

  }


  // This is an example of how to use getSize. In this example it returns the boardSize of a
  // no arg constructor basicReversiModel, which id 4
  @Test
  public void testgetSize() {
    Assert.assertEquals(4, simpleStarting.getSize());
  }


  // This Basic doMove test shows how to use the Do move method by having placing a "black"
  // tileState at 2,2. In a diagonal line, 2,3 is white and 2,4 is black.
  @Test
  public void testDoMove() {
    // This is a View of the Board after Construction Before doMove
    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(2, 2));

    // This is a View of the Board after Construction after doMove
    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // This tests that all three in a diagonal row are not black
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 2))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 4))));

  }


  // Tests if the board is displayed properly.
  @Test
  public void testVisuals() {
    BasicReversiModel basicStarting1 = new BasicReversiModel(4);
    ReversiTextView view1 = new ReversiTextView(basicStarting1);
    String expected = "   _ _ _ _\n  _ _ _ _ _\n _ _ X O _ _\n_ _ O _ X _ _\n _ _ X O _ _\n" +
            "  _ _ _ _ _\n   _ _ _ _\n";
    Assert.assertEquals(view1.toString(), expected);
  }



  // Tests if all valid moves are given.
  @Test
  public void testAllValidMoves() {
    List<Coordinate> coords = simpleStarting.allValidMoves("white");
    String coordString = "";
    for (Coordinate x : coords) {
      coordString += x.q + " ";
      coordString += x.r + " ";
    }
    Assert.assertEquals("4 1 2 2 5 2 1 4 4 4 2 5 ", coordString);

    coords = simpleStarting.allValidMoves("black");
    coordString = "";
    for (Coordinate x : coords) {
      coordString += x.q + " ";
      coordString += x.r + " ";
    }
    Assert.assertEquals("4 1 2 2 5 2 1 4 4 4 2 5 ", coordString);
  }

  @Test
  public void testGetCopy() {
    // checks that the boards are the same.
    String[][] copy = simpleStarting.getBoardCopy();
    for (int x = 0; x < copy.length; x++) {
      for (int y = 0; y < copy[x].length; y++) {
        Assert.assertEquals(copy[x][y],
                simpleStarting.getStateAt(simpleStarting.indexToCoord(x, y)));
      }
    }
    // checks that mutating the board doesn't mutate the copy.
    simpleStarting.setStateAt(simpleStarting.indexToCoord(0, 0), "black");
    Assert.assertNotEquals(copy[0][0],
            simpleStarting.getStateAt(simpleStarting.indexToCoord(0, 0)));
  }

  @Test
  public void testGetScore() {
    Assert.assertEquals(simpleStarting.getScore("white"), 3);
    Assert.assertEquals(simpleStarting.getScore("black"), 3);
    simpleStarting.setStateAt(new Coordinate(3, 0), "white");
    Assert.assertEquals(simpleStarting.getScore("white"), 4);
  }

  @Test
  public void testMostPiecesStrategy() {
    Assert.assertEquals(simpleStarting.getPlayersCopy()[0].strategicMove(),
            new Coordinate(4, 1));
    simpleStarting.setStateAt(new Coordinate(4, 4), "white");
    Assert.assertEquals(simpleStarting.getPlayersCopy()[0].strategicMove(),
            new Coordinate(5, 4));
  }

  @Test
  public void testAvoidNearCornersStrategy() {
    IPlayer[] players = {new Player(TileState.black), new Player(TileState.white)};
    BasicReversiModel game = new BasicReversiModel(players);
    players[0].setStrat(new MostPiecesStrategy(game, "hex"));
    players[1].setStrat(new MostPiecesStrategy(game, "hex"));
    game.setStateAt(new Coordinate(4, 1), "black");
    game.setStateAt(new Coordinate(3, 3), "black");
    Assert.assertEquals(game.getPlayersCopy()[0].strategicMove(),
            new Coordinate(5, 1));
    players[0].setStrat(new AvoidNearCornersStrategy(game, "hex"));
    Assert.assertEquals(game.getPlayersCopy()[0].strategicMove(),
            new Coordinate(2, 2));
  }

  @Test
  public void testGetCornersStrategy() {
    IPlayer[] players = {new Player(TileState.black), new Player(TileState.white)};
    BasicReversiModel game = new BasicReversiModel(players);
    game.passTurn();
    players[0].setStrat(new MostPiecesStrategy(game, "hex"));
    players[1].setStrat(new MostPiecesStrategy(game, "hex"));
    game.setStateAt(new Coordinate(3, 3), "white");
    game.setStateAt(new Coordinate(5, 3), "black");
    Assert.assertEquals(game.getPlayersCopy()[0].strategicMove(),
            new Coordinate(5, 1));
    players[1].setStrat(new GetCornersStrategy(game, "hex"));
    Assert.assertEquals(game.getPlayersCopy()[1].strategicMove(),
            new Coordinate(6, 3));
  }


  @Test
  public void TestMostPiecesStrategyUsingMock() {
    Coordinate coord1 = new Coordinate(2, 2);
    Coordinate coord2 = new Coordinate(0, 4);

    List<Coordinate> passingBlack = new ArrayList<Coordinate>();
    List<Coordinate> passingWhite = new ArrayList<Coordinate>();
    passingWhite.add(coord1);
    passingWhite.add(coord2);

    MockStrategies mock = new MockStrategies(passingBlack, passingWhite);
    mock.setStateAt(new Coordinate(1, 4), "black");
    Assert.assertEquals(mock.getPlayersCopy()[1].strategicMove(),
            new Coordinate(0, 4));

  }

  // Tests getBoardCopy using a board size of 3

  @Test
  public void TestGetBoardCopy() {
    BasicReversiModel superSimple = new BasicReversiModel(3);
    String[][] result = new String[5][];
    String[] line1 = new String[3];
    line1[0] = "empty";
    line1[1] = "empty";
    line1[2] = "empty";
    result[0] = line1;

    String[] line2 = new String[4];
    line2[0] = "empty";
    line2[1] = "black";
    line2[2] = "white";
    line2[3] = "empty";
    result[1] = line2;

    String[] line3 = new String[5];
    line3[0] = "empty";
    line3[1] = "white";
    line3[2] = "empty";
    line3[3] = "black";
    line3[4] = "empty";
    result[2] = line3;
    result[3] = line2;
    result[4] = line1;




    Assert.assertEquals(superSimple.getBoardCopy(), result);

  }

  //TODO test passing, getCurrentPlayer, player methods,
  //TODO test getPlayersCopy, test doMove adjusts scores and sets passed to false

  // Shows how to use indexToCoord for the cetner (3,3) coordinate
  @Test
  public void testIndexToCoordCenter() {
    Coordinate expected = new Coordinate(3, 3);

    Assert.assertEquals(simpleStarting.indexToCoord(3, 3), expected);
  }

  // Shows how to use get score, returns the starting score for whtie, which is 3
  @Test
  public void testGetScoreStartingWhite() {
    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.white)), 3);
  }

  //Shows how to use getCurrentPlayer, at start of game, the current player is "black"
  @Test
  public void testGetCurrentPlayerStartOfGame() {
    Assert.assertEquals(simpleStarting.getCurrentPlayer(), "black");
  }

  // Shows how to use getPlayersCopy
  @Test
  public void testGetPlayersCopy() {
    IPlayer[] expected = new IPlayer[2];
    expected[0] = new Player(TileState.black);
    expected[1] = new Player(TileState.white);
    expected[0].setStrat(new MostPiecesStrategy(simpleStarting, "hex"));
    expected[1].setStrat(new MostPiecesStrategy(simpleStarting, "hex"));
    IPlayer[] actual = simpleStarting.getPlayersCopy();


    Assert.assertEquals(actual[0].getState(), expected[0].getState());
    Assert.assertEquals(actual[1].getState(), expected[1].getState());
  }

  // Shows how to use gameOver, in the example below both players have passed, so the game is over
  @Test
  public void testGameOver0() {
    simpleStarting.passTurn();
    simpleStarting.passTurn();



    Assert.assertTrue(simpleStarting.getGameOver());
  }

  // Shows how to use getColor
  @Test
  public void testGetColor() {
    String[] expected = new String[2];
    expected[0] = "black";
    expected[1] = "white";
    Assert.assertEquals(simpleStarting.getColors(), expected);
  }
}
