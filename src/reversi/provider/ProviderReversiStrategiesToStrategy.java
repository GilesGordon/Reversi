package reversi.provider;

import java.util.ArrayList;
import java.util.List;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.player.strategies.Strategy;
import reversi.provider.model.HexaCoordinate;
import reversi.provider.player.strategies.ReversiStrategies;

/**
 * This adapter adapts the Provider's ReversiStrategies interface to our Strategy interface.
 */
public class ProviderReversiStrategiesToStrategy implements Strategy {

  private ReversiStrategies stratToConvert;
  private ReadonlyReversiModel model;

  public ProviderReversiStrategiesToStrategy(ReversiStrategies stratToConvert,
                                             ReadonlyReversiModel model) {
    this.stratToConvert = stratToConvert;
    this.model = model;
  }

  // a value of 2 is given to a move chosen by the provider's strategy and 0 otherwise.
  @Override
  public int[] moveValues(String player) {
    //all moves
    ArrayList<HexaCoordinate> convertedMoves = convertedMoves();
    //all moves chosen by strategy
    ArrayList<HexaCoordinate> stratMoves =
            stratToConvert.chooseMove(new ProviderRORMtoRMR(model), convertedMoves);
    int[] values = new int[convertedMoves.size()];
    for (int i = 0; i < convertedMoves.size(); i++) {
      if (stratMoves.contains(convertedMoves.get(i))) {
        values[i] = 2;
      } else {
        values[i] = 0;
      }
    }
    return new int[0];
  }

  //
  @Override
  public Coordinate bestMove(String player) {
    ArrayList<HexaCoordinate> convertedMoves = convertedMoves();
    convertedMoves = stratToConvert.chooseMove(new ProviderRORMtoRMR(model), convertedMoves);
    if (convertedMoves.isEmpty()) {
      return new Coordinate(-1, -1);
    } else {
      return convertHexToCoord(convertedMoves.get(0));
    }
  }

  //gets all the possible moves in hexacoords
  private ArrayList<HexaCoordinate> convertedMoves() {
    List<Coordinate> validMoves = model.allValidMoves(model.getCurrentPlayer());
    ArrayList<HexaCoordinate> convertedMoves = new ArrayList<>();
    for (Coordinate c : validMoves) {
      convertedMoves.add(this.covertCoordToHex(c));
    }
    //TODO finish model adapter methods (ie. get corners and all possible moves)
    return convertedMoves;
  }

  private HexaCoordinate covertCoordToHex(Coordinate c) {
    return new HexaCoordinate(c.r - model.getSize() + 1, c.q - model.getSize() + 1);
  }

  private Coordinate convertHexToCoord(HexaCoordinate c) {
    return new Coordinate(c.getQ() + model.getSize() - 1, c.getR() + model.getSize() - 1);
  }


}

