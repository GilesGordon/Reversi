package reversi.provider;


import java.util.Objects;

import reversi.controller.Features;
import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;
import reversi.provider.model.HexaCoordinate;
import reversi.provider.player.PlayerActionListener;

/**
 * Adapts our Features interface to the Providers' PlayerActionListener.
 */
public class ProviderFeaturesToActionListener implements PlayerActionListener {
  private final Features adaptee;
  private final ReadonlyReversiModel model;

  public ProviderFeaturesToActionListener(Features adaptee, ReadonlyReversiModel model) {
    this.adaptee = Objects.requireNonNull(adaptee);
    this.model = model;
  }


  @Override
  public void placeTile(HexaCoordinate hc) {
    adaptee.requestDoMove(new Coordinate(hc.getQ() + model.getSize() - 1, hc.getR() +
            model.getSize() - 1));
  }

  @Override
  public void pass() {
    adaptee.requestPass();
  }
}

