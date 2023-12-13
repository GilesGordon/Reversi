package reversi.provider.view;

import reversi.provider.player.PlayerActionListener;

/**
 * View interface for a GUI implementation. Contains methods that the controller
 * would invoke on the view.
 */
public interface ReversiViewGUI {
  /**
   * Refreshes the view to reflect any changes made to the Reversi model.
   * This includes tiles being selected, placed, or game state changes.
   */
  void refreshView();

  /**
   * Sets the display to be visible.
   */
  void setVisible(boolean show);

  /**
   * Informs the player visually that they cannot place a tale at the selected location.
   *
   * @param e Exception explaining why the move was invalid.
   */
  void invalidMove(Exception e);

  /**
   * Display that the game is over and display the winner or tie.
   */
  void gameOver();

  /**
   * Adds a listener to the view. Handles click and keyboard events.
   *
   * @param listener A PlayerActionListener of the view, likely a controller.
   */
  void addControllerListener(PlayerActionListener listener);
}
