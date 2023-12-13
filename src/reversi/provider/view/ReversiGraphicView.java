package reversi.provider.view;

import javax.swing.JFrame;

import reversi.provider.model.ReversiModelReadOnly;

import reversi.provider.player.PlayerActionListener;

/**
 * Implementation of a GUI Reversi view.
 * Makes use of Java's swing library using panels and frames to generate the GUI view.
 */
public class ReversiGraphicView extends JFrame implements ReversiViewGUI {
  private final JReversiPanel panel;

  /**
   * Constructor for a ReversiGraphicView.
   *
   * @param rmro The model being rendered graphically.
   * @param playerName The name of the player that the view pertains to.
   */
  public ReversiGraphicView(ReversiModelReadOnly rmro, String playerName) {
    super(playerName);
    this.panel = new JReversiPanel(rmro);
    this.add(panel);
    this.pack();
  }

  @Override
  public void addControllerListener(PlayerActionListener listener) {
    this.panel.addPlayerActionListener(listener);
  }

  @Override
  public void refreshView() {
    this.repaint();
  }

  @Override
  public void invalidMove(Exception e) {
    this.panel.displayIllegalMoveError(e);
  }

  @Override
  public void gameOver() {
    this.panel.displayGameOver();
    this.revalidate();
  }
}
