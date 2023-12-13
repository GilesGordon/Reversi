package reversi.controller;





import javax.swing.SwingUtilities;

import reversi.model.Coordinate;

import reversi.model.ReversiModel;
import reversi.player.IPlayer;

import reversi.view.graphic.IReversiFrame;


/**
 * A controller that connects the model and our view, makign the game run.
 */
public class PlayerController implements Features, ModelStatusFeatures, PlayerActions {

  private ReversiModel model;

  private IPlayer p;
  private IReversiFrame view;

  private boolean isPlayersturn;


  /**
   * Constructor for a controller for a game of reversi for one player and one view.
   * @param model the model for the reversi.
   * @param p the Player.
   * @param view the view for the player.
   */
  public PlayerController(ReversiModel model, IPlayer p, IReversiFrame view) {
    this.model = model;
    this.p = p;
    this.view = view;
    this.isPlayersturn = true;
    view.addFeatures(this);
    view.addState(p.getState());
    model.addFeatures(this);
    p.addFeatures(this);
  }

  private void updateTurn() {
    isPlayersturn = model.getCurrentPlayer().equals(p.getState());
  }

  @Override
  public void requestDoMove(Coordinate c) {
    this.updateTurn();
    if (isPlayersturn) {
      try {
        model.doMove(c);
      } catch (IllegalStateException ignored) {

      }
      view.displayDoMove();
    } else {
      view.displayDoMove();
    }
  }

  @Override
  public void requestPass() {
    this.updateTurn();
    if (isPlayersturn) {
      model.passTurn();
      view.rePaint();
    } else {
      view.rePaint();
    }
  }

  @Override
  public void requestDisplayMessage(String s) {
    if (s.equals("Game Over")) {
      view.displayNotification("Game Over! Your final score is " + p.getScore());
    } else {
      if ((p.getState().equals(model.getCurrentPlayer()))) {
        view.rePaint();
        view.displayNotification(s);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (s.equals("It's your turn!")) {
              p.strategicMove();
            }
          }
        });
        view.rePaint();
      } else {
        view.rePaint();
      }
    }

  }

  @Override
  public void requestDisplayBoard() {
    view.rePaint();
  }


}
