package reversi.provider.view;

import java.io.IOException;

/**
 * Depicts a visual view of the game of Reversi in some way.
 * Renders each tile of the board as either a black tile, white tile, or empty tile, in some way.
 */
public interface ReversiView {
  /**
   * Renders a model of Reversi as a textual output.
   *
   * @throws IOException if the rendering fails.
   */
  void render() throws IOException;
}
