package reversi.view.graphic;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;


import javax.swing.JPanel;


import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Decorates the view with a hint that lets the user know how many tiles a move would make
 * once they select a tile, but before they call do move.
 */
public class HintDecorator extends JPanel implements IDecorator  {
  private boolean hintsOn;
  private Point p;
  private int score;
  private boolean selected;

  /**
   * Constructs HintDecorator.
   * @param parentFrame the main frame of the game that HintDecorator is decorating
   * @param startingHints are hints initally turned on at runTime
   */
  public HintDecorator(ReversiFrame parentFrame, boolean startingHints) {
    this.hintsOn = startingHints;
    parentFrame.setDecorator(this);
    p = new Point(0, 0);
    score = 0;
    selected = false;
  }

  @Override
  public void toggleHintsTurnedOn() {
    if (!hintsOn) {
      hintsOn = true;
      showMessageDialog(this, "Hints are now turned on!");
    } else {
      hintsOn = false;
      showMessageDialog(this, "Hints are now turned off!");
    }
  }

  // does not call repaint because that is the main panel triggers this repaint
  @Override
  public void displayHint(Point p, int score, boolean selected) {
    this.p = p;
    this.score = score;
    this.selected = selected;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics = (Graphics2D) g.create();
    graphics.setColor(Color.RED);
    graphics.setFont(new Font("Arial", Font.BOLD, 24));
    if (hintsOn && selected) {
      String text = String.valueOf(score);
      graphics.drawString(text, p.x, p.y);
      this.setPreferredSize(this.getPreferredSize());
    }
  }

}



