package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A class made as part of the strategy design pattern. This specific class avoids returning any
 * output that is one spot away from the edge of the board.
 */
public class AvoidNextToCorners implements ReversiStrategies {



  @Override
  public ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                              ArrayList<HexaCoordinate> possibleCoords) {
    // Get the corners of the board
    ArrayList<HexaCoordinate> cornerCoords = model.getCorners();

    // Instantiate returning list starting with all possible moves
    ArrayList<HexaCoordinate> coordsOfHighestYield = new ArrayList<>(possibleCoords);

    // For every possible move
    for (HexaCoordinate possibility : possibleCoords) {

      // For every corner
      for (HexaCoordinate corner : cornerCoords) {

        // For every adjacent coordinate
        for (int qOffset = -1; qOffset <= 1; qOffset++) {
          for (int rOffSet = -1; rOffSet <= 1; rOffSet++) {
            if (qOffset != rOffSet) {
              if (possibility.equals(new HexaCoordinate(corner.getR() + rOffSet,
                      corner.getQ() + qOffset))) {
                coordsOfHighestYield.remove(possibility);
              }
            }
          }
        }
      }
    }
    return coordsOfHighestYield;
  }
}
