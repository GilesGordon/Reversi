package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A class implemented as part of the strategy design pattern. It helps a reversi.player.Player
 * object decide what is the best move. This specific strategy finds the move that will flip the
 * highest number of enemy pieces from a list of possible moves and returns the best options.
 */
public class MaxCaptureStrat implements ReversiStrategies {



  @Override
  public ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                              ArrayList<HexaCoordinate> possibleCoordinates) {
    // Track the highest yield and the HexaCoordinate(s) where this yield can be achieved
    int highestYield = 0;
    ArrayList<HexaCoordinate> coordsOfHighestYield = new ArrayList<>();

    // Cycle through all valid moves
    for (HexaCoordinate possibility : possibleCoordinates) {
      int yieldAtPossibility = model.getNumberOfTilesToFlipAtCoord(possibility);

      // If the move results in a greater yield, replace the current highest yield
      if (yieldAtPossibility > highestYield) {
        coordsOfHighestYield = new ArrayList<>();
        coordsOfHighestYield.add(possibility);
        highestYield = yieldAtPossibility;
      } else if (yieldAtPossibility == highestYield) {
        // Else if the move resutls in just as high of a yield, add it
        coordsOfHighestYield.add(possibility);
      }
    }
    return coordsOfHighestYield;
  }
}
