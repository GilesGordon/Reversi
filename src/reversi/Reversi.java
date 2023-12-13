package reversi;

import java.util.ArrayList;

import reversi.controller.PlayerController;
import reversi.model.BasicReversiModel;
import reversi.model.ReversiModel;
import reversi.model.SquareReversiModel;
import reversi.player.IPlayer;
import reversi.player.Player;


import reversi.player.strategies.AvoidNearCornersStrategy;
import reversi.player.strategies.CombinedStrategy;
import reversi.player.strategies.GetCornersStrategy;
import reversi.player.strategies.MostPiecesStrategy;
import reversi.player.strategies.Strategy;
import reversi.provider.player.strategies.AvoidNextToCorners;
import reversi.provider.player.strategies.MaxCaptureStrat;
import reversi.provider.player.strategies.ReversiStrategies;
import reversi.provider.player.strategies.TakeCornersStrat;
import reversi.view.graphic.HintDecorator;
import reversi.view.graphic.ReversiFrame;

import static reversi.player.TileState.black;
import static reversi.player.TileState.white;


/**
 * Creates a game of reversi.
 */
public final class Reversi {
  /**
   * Runs a game of reversi.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {


    IPlayer player1 = new Player(black);
    IPlayer player2 = new Player(white);
    IPlayer[] players = {player1, player2};
    int size = 4;
    boolean square = false;
    String type = "";

    ArrayList<String> p1Args = new ArrayList<>();
    ArrayList<String> p2Args = new ArrayList<>();

    for (String arg : args) {
      if (arg.equals("p1AvoidNearCorners") || arg.equals("p1Combined") ||
              arg.equals("p1GetCorners") || arg.equals("p1MostPieces") || arg.equals("p1Hint")) {
        p1Args.add(arg);
      }
      if (arg.equals("p2AvoidNearCorners") || arg.equals("p2Combined") ||
              arg.equals("p2GetCorners") || arg.equals("p2MostPieces") || arg.equals("p2Hint")) {
        p2Args.add(arg);
      }
      try {
        size = Integer.parseInt(arg);
      }
      catch (NumberFormatException ignored) {
        // defaults to 4
      }
      if (arg.equals("square")) {
        type = "square";
      }
    }

    // create model
    ReversiModel model;
    if (type.equals("square")) {
      if (size > 3 && size % 2 == 0) {
        model = new SquareReversiModel(players, size);
      } else {
        model = new SquareReversiModel(players, 4);
      }
    } else {
      if (size >= 3) {
        model = new BasicReversiModel(players, size);
      } else {
        model = new BasicReversiModel(players, 4);
      }
    }

    //default human
    player1.setStrat(null);
    player2.setStrat(null);


    //set player 1 strategy
    argHelperOriginalStrats(player1, p1Args, model, type);

    //set player 2 strategy
    argHelperOriginalStrats(player2, p2Args, model, type);

    ReversiFrame viewModel1 = new ReversiFrame(model, type, 0);

    boolean p1Hints = false;
    if (p1Args.contains("p1Hint")) {
      p1Hints = true;
    }
    HintDecorator panelDecoPlayer1 = new HintDecorator(viewModel1, p1Hints);
    // adds it for packing and layering
    viewModel1.addPanel(panelDecoPlayer1);

    ReversiFrame viewModel2 = new ReversiFrame(model, type, 700);

    boolean p2Hints = false;
    if (p2Args.contains("p2Hint")) {
      p2Hints = true;
    }
    HintDecorator panelDecoPlayer2 = new HintDecorator(viewModel2, p2Hints);
    // adds it for packing and layering
    viewModel2.addPanel(panelDecoPlayer2);


    PlayerController controller1 = new PlayerController(model, player1, viewModel1);
    PlayerController controller2 = new PlayerController(model, player2, viewModel2);

    viewModel1.setVisible(true);
    viewModel2.setVisible(true);

    model.startGame();
  }

  private static void argHelperOriginalStrats(IPlayer player, ArrayList<String> pArgs,
                                              ReversiModel model, String type) {
    if (!pArgs.isEmpty()) {
      String beginning;
      if (player.getState().equals("black")) {
        beginning = "p1";
      } else {
        beginning = "p2";
      }
      if (pArgs.contains(beginning + "Combined")) {
        ArrayList<Strategy> combinedArgs = new ArrayList<>();
        if (pArgs.contains(beginning + "AvoidNearCorners")) {
          combinedArgs.add(new AvoidNearCornersStrategy(model, type));
        }
        if (pArgs.contains(beginning + "GetCorners")) {
          combinedArgs.add(new GetCornersStrategy(model, type));
        }
        if (pArgs.contains(beginning + "MostPieces")) {
          combinedArgs.add(new MostPiecesStrategy(model, type));
        }
        if (!combinedArgs.isEmpty()) {
          player.setStrat(new CombinedStrategy(model, type, combinedArgs));
        }
      } else {
        if (pArgs.get(0).equals(beginning + "AvoidNearCorners")) {
          player.setStrat(new AvoidNearCornersStrategy(model, type));
        }
        if (pArgs.get(0).equals(beginning + "GetCorners")) {
          player.setStrat(new GetCornersStrategy(model, type));
        }
        if (pArgs.get(0).equals(beginning + "MostPieces")) {
          player.setStrat(new MostPiecesStrategy(model, type));
        }
      }
    }
  }

  private static ReversiStrategies p2Helper(String arg) {
    switch (arg) {
      case "p2AvoidNextToCorners":
        return new AvoidNextToCorners();
      case "p2MaxCapture":
        return new MaxCaptureStrat();
      case "p2TakeCorners":
        return new TakeCornersStrat();
      default:
        throw new IllegalArgumentException("invalid arg");
    }
  }
}