package reversi.provider;


import java.util.ArrayList;

import java.util.List;

import reversi.model.BasicReversiModel;
import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.player.IPlayer;
import reversi.player.Player;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * This adapter adapts our read only Reversi Interface to the Provider's read only Interface.
 */
public class ProviderRORMtoRMR extends BasicReversiModel implements ReversiModelReadOnly {

  private ReadonlyReversiModel model;

  public ProviderRORMtoRMR(ReadonlyReversiModel model) {
    this.model = model;
  }

  //TODO see no reason why i have to implement this all tedious but easy so will do if necessary
  @Override
  public boolean isGameOver() {
    return model.getGameOver();
  }


  @Override
  public ReversiModel.GameState getState() {
    if (model.getCurrentPlayer().equals("black")) {
      if (!model.getGameOver()) {
        return ReversiModel.GameState.BLACKTURN;
      } else {
        return ReversiModel.GameState.BLACKWIN;
      }
    }
    if (model.getCurrentPlayer().equals("white")) {
      if (!model.getGameOver()) {
        return ReversiModel.GameState.WHITETURN;
      } else {
        return ReversiModel.GameState.WHITEWIN;
      }
    } else {
      return ReversiModel.GameState.NOTSTARTED;
    }
  }


  @Override
  public int getBoardSize() {
    return model.getSize() - 1;
  }

  @Override
  public ReversiModel.TileState getTileStateAt(HexaCoordinate coords) {
    String s = model.getStateAt(this.convertCoordinate(coords));
    if (s.equals("white")) {
      return ReversiModel.TileState.WHITE;
    }
    if (s.equals("black")) {
      return ReversiModel.TileState.BLACK;
    } else {
      return ReversiModel.TileState.EMPTY;
    }
  }

  @Override
  public ReversiModel.GameState getWinner() {
    IPlayer[] players = model.getPlayersCopy();

    String winningPlayer = "";
    if (!getGameOver()) {
      throw new IllegalArgumentException("game is not over");
    }


    int highestScore = 0;


    for (IPlayer p : players) {
      if (p.getScore() > highestScore) {
        highestScore = p.getScore();
        winningPlayer = p.getState();
      } else if (highestScore == p.getScore()) {
        return ReversiModel.GameState.TIE;
      }
    }


    if (winningPlayer.equals("black")) {
      return ReversiModel.GameState.BLACKWIN;

    }
    if (winningPlayer.equals("white")) {
      return ReversiModel.GameState.WHITEWIN;
    } else {
      return ReversiModel.GameState.NOTSTARTED;
    }

  }

  @Override
  public ArrayList<ArrayList<ReversiModel.TileState>> getBoard() {
    int boardSize = this.getSize();
    String[][] list = model.getBoardCopy();
    ArrayList<ArrayList<ReversiModel.TileState>> result =
            new ArrayList<ArrayList<ReversiModel.TileState>>();
    for (int i = 0; i < boardSize * 2 - 1; i++) {
      String[] slay = list[i];
      int length = slay.length;
      ArrayList<ReversiModel.TileState> cur = new ArrayList<>();
      for (String item : slay) {
        if (item.equals("white")) {
          cur.add(ReversiModel.TileState.WHITE);
        }
        if (item.equals("black")) {
          cur.add(ReversiModel.TileState.BLACK);
        } else {
          cur.add(ReversiModel.TileState.EMPTY);
        }
      }
      result.add(cur);
    }
    return result;
  }


  @Override
  public boolean isMoveValid(HexaCoordinate coords) {
    int result = model.isLegalMove(this.convertCoordinate(coords), model.getCurrentPlayer());
    return result != 0;
  }

  @Override
  public int getScoreForPlayer(Player player) {

    return this.getScore(this.getCurrentPlayer());
  }

  @Override
  public boolean anyLegalMoves() {
    if (isGameOver()) {
      throw new IllegalArgumentException("Can't call anyLegalMoves when game is already over.");
    }
    List<Coordinate> allMoves = this.allValidMoves(getCurrentPlayer());
    return !allMoves.isEmpty();
  }

  @Override
  public int getNumberOfTilesToFlipAtCoord(HexaCoordinate coords) {
    Coordinate c = this.convertCoordinate(coords);
    return isLegalMove(c, this.getCurrentPlayer());
  }


  @Override
  public ReversiModel copyGame() {
    // IDT WE can do this tbh might have to delete miniMAX cus like they dindn't have to
    // do that and they used a model instance
    return null; // for now, but

  }

  @Override
  public ArrayList<HexaCoordinate> getCorners() {
    int x = model.getSize() - 1;
    ArrayList<HexaCoordinate> corners = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i != j) {
          Coordinate c = new Coordinate(i * x, j * x);
          HexaCoordinate hc = this.convertToHexaCoordinate(c);
          corners.add(hc);
        }
      }
    }

    return corners;
  }

  @Override
  public ArrayList<HexaCoordinate> getAllPossibleMoves() {
    List<Coordinate> ogList = this.allValidMoves(this.getCurrentPlayer());
    ArrayList<HexaCoordinate> newList = new ArrayList<>();
    for (Coordinate c : ogList) {
      HexaCoordinate hc = this.convertToHexaCoordinate(c);
      newList.add(hc);
    }
    return newList;
  }

  private Coordinate convertCoordinate(HexaCoordinate h) {
    int hQ = h.getQ();
    int hR = h.getR();
    int size = this.getBoardSize();
    hR = hR + size;
    hQ = hQ + size;
    return new Coordinate(hQ, hR);
  }

  private HexaCoordinate convertToHexaCoordinate(Coordinate c) {
    int q = c.q;
    int r = c.r;
    int size = this.getBoardSize();
    r = r - size;
    q = q - size;
    return new HexaCoordinate(r, q);
  }


}
