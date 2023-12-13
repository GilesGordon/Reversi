package reversi.view.graphic;

import java.awt.Color;
import java.awt.geom.Dimension2D;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


import reversi.controller.Features;

import reversi.model.ReadonlyReversiModel;



import static javax.swing.JOptionPane.showMessageDialog;


/**
 * Represents the frame of our Game view.
 */
public class ReversiFrame extends JFrame implements IReversiFrame {

  private APanel panel;
  private JLayeredPane lpane;

  /**
   * Constructor for ReversiFrame.
   * @param model the model the frame will display.
   */
  public ReversiFrame(ReadonlyReversiModel model, String type, int offset) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if (type.equals("square")) {
      this.panel = new SquarePanel(model);
    } else {
      this.panel = new HexPanel(model);
    }
    panel.setBackground(Color.DARK_GRAY);
    Dimension2D d = panel.getPreferredSize();
    lpane = new JLayeredPane();
    panel.setBounds(0, 0, (int)d.getWidth(), (int)d.getHeight());
    lpane.setPreferredSize(panel.getPreferredSize());
    lpane.add(panel, JLayeredPane.DEFAULT_LAYER);
    this.add(lpane);
    this.pack();
    lpane.setBackground(Color.BLUE);
    lpane.repaint();
    this.setLayout(null);
    this.setLocation(offset, 0);
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    this.panel.addFeatures(features);
  }


  @Override
  public void displayNotification(String s) {
    showMessageDialog(this, s);
  }

  @Override
  public void addState(String state) {
    panel.addState(state);
  }

  /**
   * Adds a panel to the ReversiFrame to correctly display the ReversiBoard.
   * @param panel the panel to connect to the ReversiFrame
   */
  public void addPanel(JPanel panel) {
    Dimension2D d = this.panel.getPreferredSize();
    panel.setBounds(0, 0, (int)d.getWidth(), (int)d.getHeight());
    panel.setBackground(Color.BLUE);
    panel.setOpaque(false);
    lpane.add(panel, JLayeredPane.PALETTE_LAYER);
  }

  public void setDecorator(IDecorator decorator) {
    this.panel.setDeco(decorator);
  }

  @Override
  public void rePaint() {
    lpane.repaint();
  }

  @Override
  public void displayDoMove() {
    panel.doMoveRefresh();
  }
}
