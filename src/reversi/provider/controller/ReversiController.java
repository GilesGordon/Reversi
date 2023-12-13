package reversi.provider.controller;

import reversi.provider.model.ModelStatusListener;
import reversi.provider.player.PlayerActionListener;

/**
 * Controls a game of Reversi by managing the model and view.
 * Implements PlayerActionListener and ModelStatusListener to show that the
 * controller must listen to both PlayerActions and ModelStatus updates to properly
 * upkeep the state of the game.
 */
public interface ReversiController extends PlayerActionListener, ModelStatusListener {
}
