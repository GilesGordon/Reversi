package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A class implemented as part of the strategy design pattern. It helps a reversi.player.Player
 * object decide what is the best move. This strategy will prioritize corners from a list of
 * possible moves.
 */
public class TakeCornersStrat implements ReversiStrategies {



  @Override
  public ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                              ArrayList<HexaCoordinate> possibleCoords) {
    // Get the corners of the model
    ArrayList<HexaCoordinate> cornerCoords = model.getCorners();

    // Returning list of valid strategic coords
    ArrayList<HexaCoordinate> coordsOfCornerMoves = new ArrayList<>();

    // For every possible move, if it's a corner, add it to returning list
    for (HexaCoordinate possibility : possibleCoords) {
      if (cornerCoords.contains(possibility)) {
        coordsOfCornerMoves.add(possibility);
      }
    }
    return coordsOfCornerMoves;
  }
}
