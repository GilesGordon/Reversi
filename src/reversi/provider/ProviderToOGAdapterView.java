package reversi.provider;

import java.util.Objects;

import reversi.controller.Features;
import reversi.model.ReadonlyReversiModel;
import reversi.provider.view.ReversiViewGUI;
import reversi.view.graphic.IReversiFrame;

/**
 * This adapter adapts the provider's view interface to our IReversiFrame interface.
 */
public final class ProviderToOGAdapterView implements IReversiFrame {
  private final ReversiViewGUI adaptee;
  private ReadonlyReversiModel model;

  public ProviderToOGAdapterView(ReversiViewGUI adaptee, ReadonlyReversiModel model) {
    this.adaptee = Objects.requireNonNull(adaptee);
    this.model = model;
  }


  /*
  @Override
  public void refreshView() {
    adaptee.rePaint();
  }

  @Override
  public void setVisible(boolean show) {
    // Provider didn't even use this method

  }

  @Override
  public void invalidMove(Exception e) {
    adaptee.displayNotification(e.toString());
  }

  @Override
  public void gameOver() {
    // TODO ...
    adaptee.displayNotification("Game Over! Your score is ...");

  }

  @Override
  public void addControllerListener(PlayerActionListener listener) {
    //Todo adaptor listener to features
    adaptee.addFeatures(listener);

  }

   */


  // TODO These are our interface, does it need to be a two way adapter?
  @Override
  public void rePaint() {
    adaptee.refreshView();

  }

  @Override
  public void addFeatures(Features features) {


    adaptee.addControllerListener(new ProviderFeaturesToActionListener(features, model));

  }

  @Override
  public void displayNotification(String s) {
    if (s.contains("Game Over")) {
      adaptee.gameOver();
    }
    if (s.contains("Cannot perform move at given coordinates.")) {
      String string = "Cannot perform move at given coordinates.";
      adaptee.invalidMove(new IllegalArgumentException(string));
    }

  }

  @Override
  public void addState(String state) {
    // do nothing
  }

  @Override
  public void setVisible(boolean b) {
    adaptee.setVisible(b);
  }

  @Override
  public void displayDoMove() {
    // do nothing
  }

}
