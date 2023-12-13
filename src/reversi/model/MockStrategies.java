package reversi.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Allows for the user to test the strategies of Reversi without having to filter through every
 * single possible move in a natural game. Using the mock, they can input the moves they want to
 * test against the strategies.
 */
public class MockStrategies extends BasicReversiModel {

  List<Coordinate> validMovesBlack;
  List<Coordinate> validMovesWhite;

  /**
   * This is a Zero Arg constructor for testing, this returns our defualt board size, with a deafult
   * game starting point.
   */
  public MockStrategies() {

    // Because user did not input validMoves, this just calls the valid moves from BasicReversi
    BasicReversiModel mock = new BasicReversiModel();


    // validMoves is the same as the BasicRevesi Valid Moves
    this.validMovesBlack = mock.allValidMoves("black");
    this.validMovesWhite = mock.allValidMoves("white");

  }



  /**
   * This constructor allows the user to input their own List of Valid Moves
   * they want the strategy to test.
   * @param validMovesBlack the moves that the player black pieces could do.
   * @param validMovesWhite the moves that the player with white pieces could do.
   */
  public MockStrategies(List<Coordinate> validMovesBlack, List<Coordinate> validMovesWhite) {
    this.validMovesBlack = validMovesBlack;
    this.validMovesWhite = validMovesWhite;

  }


  // We Override allValidMoves so for strategy testing the strategies test based on the coordinates
  // the user provided
  @Override
  public List<Coordinate> allValidMoves(String state) {
    if (state.equals("black")) {
      return this.validMovesBlack;
    } else if (state.equals("white")) {
      return this.validMovesWhite;
    } else {
      return new ArrayList<Coordinate>();
    }
  }





}

