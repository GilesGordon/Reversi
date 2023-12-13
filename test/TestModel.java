import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import reversi.model.BasicReversiModel;
import reversi.model.Coordinate;
import reversi.model.SquareReversiModel;
import reversi.player.IPlayer;

import reversi.player.Player;
import reversi.player.TileState;
import reversi.player.strategies.MostPiecesStrategy;
import reversi.view.text.ReversiSquareTextView;
import reversi.view.text.ReversiTextView;

/**
 * A testing class for the Reversi model.
 */
public class TestModel {

  private BasicReversiModel simpleStarting;
  private SquareReversiModel simpleSquare;

  private SquareReversiModel simpleSquare6;


  @Before
  public void init() {
    Player p1 = new Player(TileState.black);
    Player p2 = new Player(TileState.white);
    IPlayer[] players = new IPlayer[]{p1, p2};


    simpleStarting = new BasicReversiModel();
    simpleSquare = new SquareReversiModel(players, 4);
    simpleSquare6 = new SquareReversiModel(players, 6);
  }

  private Coordinate makeCordinate(int q, int r) {

    return new Coordinate(q, r);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalBoard() {
    BasicReversiModel basicStarting = new BasicReversiModel(2);
  }

  @Test
  public void testBoardConstructor() {
    BasicReversiModel basicStarting = new BasicReversiModel(4);
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(3, 0)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(6, 0)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(0, 3)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(6, 3)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(0, 6)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(3, 6)), "empty");

    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(3, 5)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(6, 2)), "empty");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(0, 5)), "empty");

    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(3, 2)), "black");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(4, 2)), "white");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(2, 3)), "white");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(4, 3)), "black");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(2, 4)), "black");
    Assert.assertEquals(basicStarting.getStateAt(makeCordinate(3, 4)), "white");
  }



  @Test
  public void testNonhexagonSquareConstrcutorErrorsSmall() {
    // Makes a Bad Square array
    TileState[][] badBoardState = new TileState[4][4];
    //ReversiModel expectsThrow = new BasicReversiModel(badBoardState);
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversiModel(badBoardState));
  }

  @Test
  public void testNonHexagonSqaureConstrcutorErrorsLarge() {
    // Makes a Bad Square array
    TileState[][] badBoardState = new TileState[8][8];
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversiModel(badBoardState));
  }

  @Test
  public void testNonSymetricalHexagonGoodTopHalfBadBottomHalf() {
    // Makes a Non Symmetrical Hegaon with a good Top Half and a Bad Bottom Half
    int boardSize = 4;
    int maxLength = (boardSize * 2) - 1;
    TileState[][] badBoardState;
    badBoardState = new TileState[maxLength][];
    for (int countRow = 0; countRow < maxLength; countRow++) {
      TileState[] currentRow;
      if (countRow < boardSize) {
        currentRow = new TileState[boardSize + countRow];
      } else {
        currentRow = new TileState[4];
      }
      Arrays.fill(currentRow, TileState.empty);
      badBoardState[countRow] = currentRow;
    }
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversiModel(badBoardState));
  }

  @Test
  public void testNonSymetricalHexagonBadTopHalfGoodBottomHalf() {
    // Makes a Non Symmetrical Hegaon with a Bad Top Half and a Good Bottom Half
    int boardSize = 4;
    int maxLength = (boardSize * 2) - 1;
    TileState[][] badBoardState;
    badBoardState = new TileState[maxLength][];
    for (int countRow = 0; countRow < maxLength; countRow++) {
      TileState[] currentRow;
      if (countRow < boardSize) {
        currentRow = new TileState[4];
      } else {
        currentRow = new TileState[boardSize + (maxLength - countRow) - 1];
      }
      Arrays.fill(currentRow, TileState.empty);
      badBoardState[countRow] = currentRow;
    }
    Assert.assertThrows(IllegalArgumentException.class, () -> new BasicReversiModel(badBoardState));
  }

  // Note; There is a test in the Examples Class that shows that a symmetrical board does not throw

  @Test
  public void testGoodSymetricalHexagonGoodTopHalfGoodBottomHalf() {
    int boardSize = 4;
    int maxLength = (boardSize * 2) - 1;
    TileState[][] goodBoardState;
    goodBoardState = new TileState[maxLength][];
    for (int countRow = 0; countRow < maxLength; countRow++) {
      TileState[] currentRow;
      if (countRow < boardSize) {
        currentRow = new TileState[boardSize + countRow];
      } else {
        currentRow = new TileState[boardSize + (maxLength - countRow) - 1];
      }
      Arrays.fill(currentRow, TileState.empty);
      goodBoardState[countRow] = currentRow;
    }
    // Does Not Throw
    new BasicReversiModel(goodBoardState);
    //gets here if no exception thrown
    Assert.assertTrue(true);
  }

  @Test
  public void testSetStateAt() {
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "empty");
    simpleStarting.setStateAt(makeCordinate(3, 0), "black");
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "black");

  }

  @Test
  public void testGetStateAt() {
    Assert.assertEquals(simpleStarting.getStateAt(makeCordinate(3, 0)), "empty");
  }

  /*
  @Test
  public void testCheckBoundsBad() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.doMove(makeCordinate(8, 7)));
    // doMove calls setState and set State throws this message
    Assert.assertEquals(execpetion.getMessage(), "Cannot perform move at given coordinates.");

  }

   */

  // R Is bigger than Allowed, q = 3 is found in every row so the error is R
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder1() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(3, 7), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  // Q Is bigger than Allowed TOP HALF, top value for Q with R 2 is 6
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder2() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(7, 2), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  // Q Is smaller than Allowed TOP HALF, smallest value for Q with R 3 is 2
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder3() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(1, 1), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  // Q Is smaller than Allowed Bottom HALF, smallest value for Q for all of Bottom Half is 0
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder4() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(-1, 5), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  // Q Is Greater than Allowed Bottom HALF, Largest value for Q for all R = 5 is 4
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder5() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(5, 5), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }


  // R Is smaller than Allowed, q = 3 is found in every row so the error is R
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder6() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(3, -1), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  // Q Is Greater than Allowed Bottom HALF, Largest value for Q for all R = 5 is 4
  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorder7() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleStarting.setStateAt(makeCordinate(4, -1), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  @Test
  public void testgetSize() {
    Assert.assertEquals(4, simpleStarting.getSize());
  }

  // We are Editing inital State
  // To make sure 4 In a  Line switches correctly when adding 1 to R
  @Test
  public void testDoMove1() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(2, 3), "black");
    simpleStarting.setStateAt(makeCordinate(2, 5), "white");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());
    simpleStarting.passTurn();
    simpleStarting.doMove(makeCordinate(2, 2));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole line of chnaged Values to be white now
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 2))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 4))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 5))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(2, 6))));

  }

  // We are Editing inital State
  // To make sure 4 In a Line switches correctly when adding 1 to Q
  @Test
  public void testDoMove2() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(3, 3), "white");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(1, 3));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole line of chnaged Values to be white now
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(1, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(4, 3))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(5, 3))));

  }

  // We are Editing inital State
  // To make sure 4 In a Line switches correctly when adding 1 to Q and -1 from R
  @Test
  public void testDoMove3() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(3, 2), "white");
    simpleStarting.setStateAt(makeCordinate(4, 1), "black");

    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(1, 4));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole  line of chnaged Values to be white now
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(1, 4))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 2))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(4, 1))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(5, 0))));

  }

  // We are Editing inital State
  // To make sure 4 In a  Line switches correctly when subtracting 1 to R
  @Test
  public void testDoMove4() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(2, 3), "black");
    simpleStarting.setStateAt(makeCordinate(2, 2), "white");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());
    simpleStarting.passTurn();
    simpleStarting.doMove(makeCordinate(2, 5));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole  line of chnaged Values to be white now
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 2))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 4))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 5))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(2, 6))));

  }

  // We are Editing inital State
  // To make sure 4 In a  Line switches correctly when subtracting 1 to Q
  @Test
  public void testDoMove5() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(3, 3), "black");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());
    simpleStarting.passTurn();
    simpleStarting.doMove(makeCordinate(5, 3));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole  line of chnaged Values to be white now
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(3, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(4, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(5, 3))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(6, 3))));

  }

  // We are Editing inital State
  // To make sure 4 In a Line switches correctly when subtracting 1 to Q and +1 from R
  @Test
  public void testDoMove6() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(3, 2), "white");
    simpleStarting.setStateAt(makeCordinate(1, 4), "black");

    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(4, 1));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests whole  line of chnaged Values to be white now
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(1, 4))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 2))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(4, 1))));

    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleStarting.getStateAt(makeCordinate(5, 0))));

  }

  // We are Editing inital State
  // For when the Tile is adjacent to different lines, 1 with Q +1, and 1 with R +1
  @Test
  public void testDoMoveComplex1() {
    //change some tiles
    simpleStarting.setStateAt(makeCordinate(2, 5), "white");
    simpleStarting.setStateAt(makeCordinate(2, 3), "black");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.passTurn();
    simpleStarting.doMove(makeCordinate(2, 2));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests First Line ,where Q + 1
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 2))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(3, 2))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(4, 2))));

    // Tests Second line where R +1
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 3))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 4))));
    Assert.assertEquals("white", (simpleStarting.getStateAt(makeCordinate(2, 5))));


  }

  // Testing a Full with Incrementing +1 on Q values, Tests to make sure that the edges
  // work with do move
  @Test
  public void testDoMoveFullDiagonal() {
    // change tiles
    simpleStarting.setStateAt(makeCordinate(3, 1), "white");
    simpleStarting.setStateAt(makeCordinate(3, 2), "white");
    simpleStarting.setStateAt(makeCordinate(3, 3), "white");
    // 3, 4 already is white
    simpleStarting.setStateAt(makeCordinate(3, 5), "white");
    simpleStarting.setStateAt(makeCordinate(3, 6), "black");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(3, 0));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests Full Diagonal
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 0))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 1))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 2))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 3))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 4))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 5))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 6))));
  }

  // Testing doMove on a line on the full top Row, with Q values indexing by +1
  @Test
  public void testDoMoveFullTopRow() {
    // change tiles
    simpleStarting.setStateAt(makeCordinate(4, 0), "white");
    simpleStarting.setStateAt(makeCordinate(5, 0), "white");
    simpleStarting.setStateAt(makeCordinate(6, 0), "black");


    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    simpleStarting.doMove(makeCordinate(3, 0));

    ReversiTextView view2 = new ReversiTextView(simpleStarting);
    System.out.println(view2.toString());

    // Tests Full Diagonal
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(3, 0))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(4, 0))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(5, 0))));
    Assert.assertEquals("black", (simpleStarting.getStateAt(makeCordinate(6, 0))));


  }


  // Testing invalid doMove for top cordinate
  @Test(expected = IllegalArgumentException.class)
  public void testInvlaidDoMoveFullTopRow() {

    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    // Tests INvalid Move
    simpleStarting.doMove(makeCordinate(3, 0));





  }



  /*
  // Testing invalid doMove for center cordinate
  @Test(expected = IllegalArgumentException.class)
  public void testInvlaidDoMoveFullTopRow2() {

    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    // Tests INvalid Move
    simpleStarting.doMove(makeCordinate(3, 3));

  }

   */

  /*
  // Testing invalid doMove for side coordinate
  @Test(expected = IllegalArgumentException.class)
  public void testInvlaidDoMoveFullTopRow3() {

    ReversiTextView view1 = new ReversiTextView(simpleStarting);
    System.out.println(view1.toString());

    // Tests INvalid Move
    simpleStarting.doMove(makeCordinate(5, 3));

  }

   */

  // Test for isLegalMove is Out of Bounds
  @Test
  public void testIsLegalMoveOutOfBounds() {
    Assert.assertEquals(simpleStarting.isLegalMove(makeCordinate(0, 7), "black"),
            0);
  }

  // Test for isLegalMove if Cell Trying to move to is already filled in
  @Test
  public void testIsLegalAlreadyFilledIn() {
    simpleStarting.setStateAt(makeCordinate(2,2), String.valueOf(TileState.black));
    Assert.assertEquals(simpleStarting.isLegalMove(makeCordinate(2, 2), "white"),
            0);
  }

  // Test for isLegalMove, shoudl flip 2 black to white
  @Test
  public void testIsLegalSimple() {
    // Marks this cell black as well
    simpleStarting.setStateAt(makeCordinate(2,2), String.valueOf(TileState.black));
    Assert.assertEquals(simpleStarting.isLegalMove(makeCordinate(1, 2), "white"),
            2);
  }

  // Test for indexToCoord For edge bottom left corner Move
  @Test
  public void testIndexToCoordSimpleEdge() {
    Coordinate expected = new Coordinate(3, 0);

    Assert.assertEquals(simpleStarting.indexToCoord(0, 0), expected);
  }

  // Test for indexToCoord For edge top right corner Move
  @Test
  public void testIndexToCoordSimpleEdgeTop() {
    Coordinate expected = new Coordinate(3, 6);

    Assert.assertEquals(simpleStarting.indexToCoord(6, 3), expected);
  }



  //Test starting board for white
  @Test
  public void testGetScoreStartingWhite() {
    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.white)), 3);
  }

  //Test starting board for black
  @Test
  public void testGetScoreStartingBlack() {
    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.black)), 3);
  }

  //Test starting board for empty this returns 31
  @Test
  public void testGetScoreStartingEmpty() {
    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.empty)), 31);
  }

  //Test enhanced board for white, there are no black
  @Test
  public void testGetScoreEnhacnedWhiteNoBlack() {
    simpleStarting.setStateAt(makeCordinate(3,2), String.valueOf(TileState.white));
    simpleStarting.setStateAt(makeCordinate(2,4), String.valueOf(TileState.white));
    simpleStarting.setStateAt(makeCordinate(4,3), String.valueOf(TileState.white));

    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.white)), 6);
    Assert.assertEquals(simpleStarting.getScore(String.valueOf(TileState.black)), 0);
  }

  //TODO test more getSCore with edge cases in refrence to if game is over


  //Test Current player start of game
  @Test
  public void testGetCurrentPlayerStartOfGame() {
    Assert.assertEquals(simpleStarting.getCurrentPlayer(), "black");
  }

  //Test Current player after first player passes
  @Test
  public void testGetCurrentPlayerAfterPass() {
    simpleStarting.passTurn();
    Assert.assertEquals(simpleStarting.getCurrentPlayer(), "white");
  }

  //Test Current player after first player passes, second player does move
  @Test
  public void testGetCurrentPlayerAfterPassDoMove() {
    simpleStarting.passTurn();
    simpleStarting.doMove(makeCordinate(2, 2));
    Assert.assertEquals(simpleStarting.getCurrentPlayer(), "black");
  }


  @Test
  public void testGetPlayersCopy() {
    IPlayer[] expected = new IPlayer[2];
    expected[0] = new Player(TileState.black);
    expected[1] = new Player(TileState.white);
    expected[0].setStrat(new MostPiecesStrategy(simpleStarting, "black"));
    expected[1].setStrat(new MostPiecesStrategy(simpleStarting, "white"));
    IPlayer[] actual = simpleStarting.getPlayersCopy();


    Assert.assertEquals(actual[0].getState(), expected[0].getState());
    Assert.assertEquals(actual[1].getState(), expected[1].getState());
  }



  // test game over by passing in model
  @Test
  public void testGameOver0() {
    simpleStarting.passTurn();
    simpleStarting.passTurn();


    Assert.assertTrue(simpleStarting.getGameOver());
  }

  // test game not over by only passing once in mode
  @Test
  public void testGameOver1() {
    simpleStarting.passTurn();



    Assert.assertFalse(simpleStarting.getGameOver());
  }

  // Test get color
  @Test
  public void testGetColor() {
    String[] expected = new String[2];
    expected[0] = "black";
    expected[1] = "white";
    Assert.assertEquals(simpleStarting.getColors(), expected);
  }

  @Test
  public void testSquareConstructor() {

    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(0, 0)), "empty");

    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(0, 3)), "empty");

    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(1, 1)), "black");
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(1, 2)), "white");
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(2,1 )), "white");
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(2,2)), "black");



    ReversiSquareTextView view = new ReversiSquareTextView(simpleSquare);
    System.out.println(view.toString());


  }

  @Test
  public void testSquareConstructorFor6Size() {

    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(0, 0)), "empty");

    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(0, 5)), "empty");

    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(2, 2)), "black");
    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(2, 3)), "white");
    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(3,2 )), "white");
    Assert.assertEquals(simpleSquare6.getStateAt(makeCordinate(3,3)), "black");



    ReversiSquareTextView view = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view.toString());


  }


  @Test
  public void testSetStateAtSquare() {
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(3, 0)), "empty");
    simpleSquare.setStateAt(makeCordinate(3, 0), "black");
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(3, 0)), "black");

  }

  @Test
  public void testCheckBoundsBadCloseTestingRightOnBorderSquare() {
    IllegalArgumentException execpetion = Assert.assertThrows(IllegalArgumentException.class,
        () -> simpleSquare.setStateAt(makeCordinate(3, 7), "black"));
    // Set State at calls check Bounds and then throws this message
    Assert.assertEquals(execpetion.getMessage(), "Provided cooridnates are invalid");

  }

  @Test
  public void testGetStateAtSquare() {
    Assert.assertEquals(simpleSquare.getStateAt(makeCordinate(3, 0)), "empty");
  }

  @Test
  public void testgetSizeSquare() {
    Assert.assertEquals(4, simpleSquare.getSize());
  }



  @Test
  public void testDoMove1Square() {


    //change some tiles



    ReversiTextView view1 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view1.toString());

    simpleSquare6.doMove(makeCordinate(3, 1));

    ReversiTextView view2 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view2.toString());

    // Tests whole line of chnaged Values to be black now
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(3, 1))));
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(3, 2))));
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(3, 3))));


    //Tests the following value to make sure still empty
    Assert.assertEquals("empty", (simpleSquare6.getStateAt(makeCordinate(3, 4))));

  }

  @Test
  public void testDoMove2Diagonal() {


    //change some tiles



    ReversiTextView view1 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view1.toString());

    simpleSquare6.setStateAt(makeCordinate(1, 1), "white");
    simpleSquare6.doMove(makeCordinate(0, 0));

    ReversiTextView view2 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view2.toString());

    // Tests whole line of chnaged Values to be black now
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(0, 0))));
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(1, 1))));
    Assert.assertEquals("black", (simpleSquare6.getStateAt(makeCordinate(2, 2))));



  }

  @Test
  public void testDoMove3FUllDiagonal() {

    ReversiTextView view1 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view1.toString());

    //change some tiles
    simpleSquare6.setStateAt(makeCordinate(0, 0), "white");
    simpleSquare6.setStateAt(makeCordinate(1, 1), "black");
    simpleSquare6.setStateAt(makeCordinate(4, 4), "black");
    simpleSquare6.passTurn();

    simpleSquare6.doMove(makeCordinate(5, 5));








    ReversiTextView view2 = new ReversiSquareTextView(simpleSquare6);
    System.out.println(view2.toString());

    // Tests whole line of chnaged Values to be black now
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(0, 0))));
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(1, 1))));
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(2, 2))));
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(3, 3))));
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(4, 4))));
    Assert.assertEquals("white", (simpleSquare6.getStateAt(makeCordinate(5, 5))));



  }

  @Test
  public void testIsLegalMoveOutOfBoundsSquare() {
    Assert.assertEquals(simpleSquare6.isLegalMove(makeCordinate(0, 6), "black"),
            0);
  }

  @Test
  public void testGetCurrentPlayerStartOfGameSquare() {
    Assert.assertEquals(simpleSquare6.getCurrentPlayer(), "black");
  }

  //Test Current player after first player passes, second player does move
  @Test
  public void testGetCurrentPlayerAfterPassDoMoveSquare() {
    simpleSquare6.passTurn();
    simpleSquare6.doMove(makeCordinate(2, 1));
    Assert.assertEquals(simpleSquare6.getCurrentPlayer(), "black");
  }

  @Test
  public void testGetPlayersCopySquare() {
    IPlayer[] expected = new IPlayer[2];
    expected[0] = new Player(TileState.black);
    expected[1] = new Player(TileState.white);
    expected[0].setStrat(new MostPiecesStrategy(simpleSquare6, "hex"));
    expected[1].setStrat(new MostPiecesStrategy(simpleSquare6, "hex"));
    IPlayer[] actual = simpleSquare6.getPlayersCopy();


    Assert.assertEquals(actual[0].getState(), expected[0].getState());
    Assert.assertEquals(actual[1].getState(), expected[1].getState());
  }


  @Test
  public void testSquareViewStartOfGame() {

    ReversiTextView view1 = new ReversiSquareTextView(simpleSquare);
    System.out.println(view1.toString());

    Assert.assertEquals(view1.toString(), " _ _ _ _\n" +
            " _ O X _\n" +
            " _ X O _\n" +
            " _ _ _ _\n");

  }

  @Test
  public void testSquareViewMiddleOfGame() {

    ReversiTextView view1 = new ReversiSquareTextView(simpleSquare6);




    //change some tiles
    simpleSquare6.setStateAt(makeCordinate(0, 0), "white");
    simpleSquare6.setStateAt(makeCordinate(1, 1), "black");
    simpleSquare6.setStateAt(makeCordinate(4, 4), "black");
    simpleSquare6.passTurn();

    simpleSquare6.doMove(makeCordinate(5, 5));

    System.out.println(view1.toString());

    Assert.assertEquals(view1.toString(), " X _ _ _ _ _\n" +
            " _ X _ _ _ _\n" +
            " _ _ X X _ _\n" +
            " _ _ X X _ _\n" +
            " _ _ _ _ X _\n" +
            " _ _ _ _ _ X\n");

  }




  




}


