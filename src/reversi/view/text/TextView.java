package reversi.view.text;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Reversi game.
 */
public interface TextView {

  /**
   * Renders the Reversi state as text.
   */
  void render() throws IOException;
}
