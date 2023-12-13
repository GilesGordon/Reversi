package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A class implemented as part of the strategy design pattern. It helps a reversi.player.Player
 * object decide what is the best move. This strategy will try two strategies, if the first is
 * possible (it does not return an empty list) it will try the second strategy on the remaining
 * possible moves. If that is possible it returns the list of possible moves that satisfy both
 * strategies. Otherwise, it returns the result of the first strategy.
 * If the first strategy is not possible then it tries to run the second strategy. After that it
 * returns the result of the second strategy.
 */
public class TryTwo implements ReversiStrategies {
  private final ReversiStrategies first;
  private final ReversiStrategies second;

  /**
   * Constructor for TryTwo.
   * @param first the first method that to be tried.
   * @param second the second method to be tried.
   */
  public TryTwo(ReversiStrategies first, ReversiStrategies second) {
    this.first = first;
    this.second = second;

  }

  @Override
  public ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                              ArrayList<HexaCoordinate> possibleCoords) {
    ArrayList<HexaCoordinate> optionsAfterFirst = first.chooseMove(model, possibleCoords);
    ArrayList<HexaCoordinate> optionsAfterSecond = new ArrayList<>();
    if (!optionsAfterFirst.isEmpty()) {
      optionsAfterSecond = second.chooseMove(model, optionsAfterFirst);
      if (optionsAfterSecond.isEmpty()) {
        return optionsAfterFirst;
      } else {
        return optionsAfterSecond;
      }
    } else {
      optionsAfterSecond = second.chooseMove(model, possibleCoords);
      if (optionsAfterSecond.isEmpty()) {
        return optionsAfterFirst;
      } else {
        return optionsAfterSecond;
      }
    }
  }
}
