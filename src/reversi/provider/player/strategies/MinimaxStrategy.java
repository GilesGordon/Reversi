package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A class implemented as part of the strategy design pattern. This strategy returns a list
 * of viable moves (HexaCoordinates) where the next player's turn's most viable strategy
 * is minimized, thus making an assumption on the next player's strategy.
 */
public class MinimaxStrategy implements ReversiStrategies {
  private final ReversiStrategies expectedStrategy;

  /**
   * Constructor for a minMaxStrategy that guesses what the opponent's strategy is to
   * decide optimal moves.
   *
   * @param expectedStrategy Assumption of opponent's strategy.
   */
  public MinimaxStrategy(ReversiStrategies expectedStrategy) {
    this.expectedStrategy = expectedStrategy;
  }

  @Override
  public ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                              ArrayList<HexaCoordinate> possibleCoords) {
    ArrayList<HexaCoordinate> optimalMoves = new ArrayList<>();
    // The fewest optimal moves is initialized to the highest possible number, the number of
    // cells on the board
    int fewestOptimalEnemyMoves = model.getBoard().size();

    // For each possible move
    for (HexaCoordinate possibility : possibleCoords) {
      // Copy the game
      ReversiModel tempModel = model.copyGame();

      // Place the tile there
      tempModel.placeTile(possibility);

      // Get all the possible moves for the next player's turn
      ArrayList<HexaCoordinate> enemyPossibilities = tempModel.getAllPossibleMoves();

      // Based off our assumption of the opponent's strategy, get all the optimal
      // moves they  can make
      ArrayList<HexaCoordinate> opportunities = this.expectedStrategy.chooseMove(tempModel,
              enemyPossibilities);

      if (fewestOptimalEnemyMoves > opportunities.size()) {
        fewestOptimalEnemyMoves = opportunities.size();
        optimalMoves = new ArrayList<>();
        optimalMoves.add(possibility);
      } else if (fewestOptimalEnemyMoves == opportunities.size()) {
        optimalMoves.add(possibility);
      }
    }
    return optimalMoves;
  }
}
