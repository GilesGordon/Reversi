import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reversi.model.BasicReversiModel;
import reversi.model.Coordinate;
import reversi.player.IPlayer;



/**
 * Tests for the IPlayer interface.
 */
public class TestPlayer {
  private IPlayer simplePlayer;
  private BasicReversiModel simpleStarting;


  @Before
  public void init() {
    this.simpleStarting = new BasicReversiModel();
    this.simplePlayer = simpleStarting.getPlayersCopy()[0];
  }

  private Coordinate makeCordinate(int q, int r) {

    return new Coordinate(q, r);
  }


  @Test
  public void testGetState() {
    Assert.assertEquals(simplePlayer.getState(), "black");
  }

  //Returns the Players Starting Score
  @Test
  public void testGetScore() {
    Assert.assertEquals(simplePlayer.getScore(), 0);
  }

  //Returns the Players Score after a move
  @Test
  public void testGetScore1() {
    simpleStarting.doMove(makeCordinate(4,1));
    Assert.assertEquals(simplePlayer.getScore(), 5);
  }

  @Test
  public void testSetScore() {
    simplePlayer.setScore(18);
    Assert.assertEquals(simplePlayer.getScore(), 18);

  }

  @Test
  public void testGetPassedBeforePass() {
    Assert.assertFalse(simplePlayer.getPassed());

  }

  @Test
  public void testGetPassedAfterPass() {
    simpleStarting.passTurn();
    Assert.assertTrue(simplePlayer.getPassed());


  }

  @Test
  public void testSetPassedBeforePass() {
    simplePlayer.setPassed(true);
    Assert.assertTrue(simplePlayer.getPassed());


  }

  @Test
  public void testSetPassedSAfterPass() {
    simpleStarting.passTurn();
    simplePlayer.setPassed(true);
    Assert.assertTrue(simplePlayer.getPassed());


  }




}
