import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import reversi.model.Coordinate;
import reversi.model.MockStrategies;
import reversi.player.strategies.AvoidNearCornersStrategy;
import reversi.player.strategies.CombinedStrategy;
import reversi.player.strategies.GetCornersStrategy;

import reversi.player.strategies.MostPiecesStrategy;
import reversi.player.strategies.Strategy;


/**
 * Provides tests to test the game strategies.
 */
public class TestStrategies {

  private Coordinate coord2flips;

  private Coordinate coord3flips;

  private MockStrategies mock;
  private MostPiecesStrategy mostPiecesStrategy;
  private GetCornersStrategy getCornersStrategy;
  private AvoidNearCornersStrategy avoidNearCornersStrategy;
  private CombinedStrategy combinedStrategy;

  @Before
  public void init() {
    // 1 piece flips
    Coordinate coord1flip = new Coordinate(4, 0);
    // 2 Pieces flip, this is also a corner
    this.coord2flips = new Coordinate(0, 3);
    // 3 Pieces flip
    this.coord3flips = new Coordinate(6,1 );

    List<Coordinate> passingBlackEmpty = new ArrayList<Coordinate>();
    List<Coordinate> passingWhite3Moves = new ArrayList<Coordinate>();
    passingWhite3Moves.add(coord1flip);
    passingWhite3Moves.add(coord2flips);
    passingWhite3Moves.add(coord3flips);

    this.mock = new MockStrategies(passingBlackEmpty, passingWhite3Moves);

    // THis makes it so wehn 4,0 is called only 1 piece will flip
    mock.setStateAt(new Coordinate(4, 1), "black");

    // THis makes it so wehn 0,3 is called only 2 pieces will flip
    mock.setStateAt(new Coordinate(3, 3), "white");
    mock.setStateAt(new Coordinate(2, 3), "black");
    mock.setStateAt(new Coordinate(1, 3), "black");

    // This makes it so when 6,1 is called 3 pieces wil flip
    mock.setStateAt(new Coordinate(4, 3), "black");
    mock.setStateAt(new Coordinate(5, 2), "black");
    mock.setStateAt(new Coordinate(3, 4), "black");
    mock.setStateAt(new Coordinate(2, 5), "white");

    this.mostPiecesStrategy = new MostPiecesStrategy(mock, "hex");
    this.getCornersStrategy = new GetCornersStrategy(mock, "hex");
    this.avoidNearCornersStrategy = new AvoidNearCornersStrategy(mock, "hex");
    ArrayList<Strategy> list = new ArrayList<Strategy>();
    list.add(mostPiecesStrategy);
    list.add(getCornersStrategy);
    list.add(avoidNearCornersStrategy);
    this.combinedStrategy = new CombinedStrategy(mock, "hex", list);




  }


  @Test
  public void TestMostPiecesStrategyMock() {
    Assert.assertEquals(mock.getPlayersCopy()[1].strategicMove(),
            coord3flips);

  }

  // FOR MOST PEICES
  @Test
  public void testMoveValuesUsingMock() {

    int[] values = new int[3];
    values[0] = 1;
    values[1] = 2;
    values[2] = 3;

    int[] result = mostPiecesStrategy.moveValues("white");

    Assert.assertEquals(result[0], values[0]);
    Assert.assertEquals(result[1], values[1]);
    Assert.assertEquals(result[2], values[2]);

  }

  // FOR Get Corners Strategy, will reutrn coordFlip2 with highest val becasue that is a corner
  @Test
  public void testMoveValuesUsingMockGetCornersStrategey() {

    int[] values = new int[3];
    values[0] = 0;
    values[1] = 2;
    values[2] = 0;

    int[] result = getCornersStrategy.moveValues("white");

    Assert.assertEquals(result[0], values[0]);
    Assert.assertEquals(result[1], values[1]);
    Assert.assertEquals(result[2], values[2]);

  }

  // For Avoid near corners, becasue coord1Flip (4, 0) and coord3Flip (6, 1) are near corners,
  // they returns -2, evetyhing else returns 0;
  @Test
  public void testMoveValuesUsingMockAvoidNearCorners() {

    int[] values = new int[3];
    values[0] = -2;
    values[1] = 0;
    values[2] = -2;

    int[] result = avoidNearCornersStrategy.moveValues("white");

    Assert.assertEquals(result[0], values[0]);
    Assert.assertEquals(result[1], values[1]);
    Assert.assertEquals(result[2], values[2]);

  }

  // FOR Combined Strategies what this does is that it adds the scores from
  // both of the three tests above for MostPieces and Corner as well as Avoid Near Corner,
  // to find the best overall move.
  @Test
  public void testMoveValuesUsingMockCombinedStrategey() {

    int[] values = new int[3];
    values[0] = -1;
    values[1] = 4;
    values[2] = 1;

    int[] result = combinedStrategy.moveValues("white");

    Assert.assertEquals(result[0], values[0]);
    Assert.assertEquals(result[1], values[1]);
    Assert.assertEquals(result[2], values[2]);

  }

  // Tests Get Corners Strategy Using A Mock, returns coord2Flips because this is
  // the corner, and gets the higest value as seen the test for moveValues for getCorners
  @Test
  public void TestGetCornersStrategyMock() {
    mock.getPlayersCopy()[1].setStrat(getCornersStrategy);
    Assert.assertEquals(mock.getPlayersCopy()[1].strategicMove(),
            coord2flips);

  }

  // Tests Avoid Near Corners Strategy Using A Mock, coord2Flips is the only one which
  // is not adjacent to a corner (because it is a corner itself)
  @Test
  public void TestAvoidCornersStrategyMock() {
    mock.getPlayersCopy()[1].setStrat(avoidNearCornersStrategy);
    Assert.assertEquals(mock.getPlayersCopy()[1].strategicMove(),
            coord2flips);

  }


}
