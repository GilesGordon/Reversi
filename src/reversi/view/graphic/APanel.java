package reversi.view.graphic;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Objects;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.BasicStroke;


import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.MouseEvent;

import reversi.controller.Features;
import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Represents the panel of our Game view. Key and Mouse listeners are found as well.
 */
public abstract class APanel extends JPanel {

  // NOTE: As is, this implements the Features interface,
  // but as soon as the controller is implemented
  // the controller, it will implement this instead.

  protected final ReadonlyReversiModel model;
  protected int shapeWidth;
  protected int shapeHeight;
  protected Point clicked;
  protected Coordinate selectedTile;
  final String[] colorsInGame;
  protected IDecorator decoratorHint;
  protected String state;
  protected boolean selected;


  /**
   * Constructor for APanel.
   *
   * @param model the model the view displays.
   */
  public APanel(ReadonlyReversiModel model) {
    this.model = Objects.requireNonNull(model);
    this.addMouseListener(new MouseEventsListener());
    this.setFocusable(true);  // Make sure the panel is focused to receive key events
    setSizes(model);
    this.clicked = new Point(getPreferredSize().width, getPreferredSize().height);
    this.selectedTile = null;
    this.colorsInGame = model.getColors();
    this.state = null;
    this.selected = false;
  }

  protected abstract void setSizes(ReadonlyReversiModel model);

  protected class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      clicked = e.getPoint();
      translationClick(clicked);

      selected = false;
      String[][] copy = model.getBoardCopy();
      for (int row = 0; row < copy.length; row++) {
        for (int col = 0; col < copy[row].length; col++) {
          Point p = convertCoordToGame(model.indexToCoord(row, col));
          Polygon shape = getShape(p);
          if (shape.contains(clicked)) {
            Coordinate currentCoord = model.indexToCoord(row, col);
            if (!currentCoord.equals(selectedTile)) {
              selected = true;
              selectedTile = model.indexToCoord(row, col);
            }
          }
        }
      }
      int score = 0;
      if (selectedTile != null) {
        score = model.isLegalMove(selectedTile, state);
        Point p = convertCoordToGame(selectedTile);
        p.translate((int)translationRender().getTranslateX(),
                (int)translationRender().getTranslateY());
        decoratorHint.displayHint(p, score, selected);
      }
      if (!selected) {
        selectedTile = null;
      }
      APanel.this.repaint();
    }
  }

  protected void addFeatures(Features features) {


    this.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
          if (e.getKeyChar() == 'p') {
            features.requestPass();
          } else if (e.getKeyChar() == 'h') {
            APanel.this.decoratorHint.toggleHintsTurnedOn();
          }
        }

        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_D) {
            features.requestDoMove(selectedTile);
          }
        }

        @Override
        public void keyReleased(KeyEvent e) {
          // does nothing for now
        }
      }
    );
  }

  protected void toggleHintsTurnedOn() {
    displayToPanel("Error, cannot call for hints when hints were diabled at runtime.");
  }

  protected void displayToPanel(String s) {
    showMessageDialog(this, s);
  }


  /**
   * This method tells Swing what the "natural" size should be
   * for this panel.  Here, we set it to 700x700 pixels.
   *
   * @return Our preferred *physical* size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(700, 700);
  }


  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g2d = (Graphics2D) graphics.create();

    //boolean selected = false;
    g2d.transform(translationRender());
    String[][] copy = model.getBoardCopy();
    for (int row = 0; row < copy.length; row++) {
      for (int col = 0; col < copy[row].length; col++) {
        Point p = convertCoordToGame(model.indexToCoord(row, col));
        Polygon shape = getShape(p);

        // dark gray outline
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawPolygon(shape);

        // fill
        g2d.setColor(Color.LIGHT_GRAY);
        Coordinate currentCoord = model.indexToCoord(row, col);
        if (currentCoord.equals(selectedTile)) {
          g2d.setColor(Color.CYAN);
        }
        g2d.fill(shape);

        //draw pieces
        for (String s : colorsInGame) {
          if (copy[row][col].equals(s)) {
            g2d.setColor(this.getColor(s));
            g2d.setStroke(new BasicStroke(1));
            Ellipse2D piece = getPiece(p);
            g2d.draw(piece);
            g2d.fill(piece);
          }
        }
      }
    }
  }

  protected abstract AffineTransform translationRender();

  protected abstract void translationClick(Point p);

  private Color getColor(String color) {
    switch (color) {
      case "black":
        return Color.black;
      case "white":
        return Color.white;
      default:
        return new Color(0, 0, 0, 0);
    }
  }

  protected abstract Point convertCoordToGame(Coordinate c);

  protected abstract Ellipse2D getPiece(Point p);

  protected abstract Polygon getShape(Point p);

  /**
   * Repaints the panel after do move is called.
   */
  public void doMoveRefresh() {
    this.selectedTile = null;
    this.clicked = new Point(-1, -1);
    this.selected = false;
    int score = 0;
    APanel.this.decoratorHint.displayHint(new Point(0, 0), score, false);
    APanel.this.repaint();
  }

  public void setDeco(IDecorator panel) {
    this.decoratorHint = panel;
  }

  public void addState(String state) {
    this.state = state;
  }

}
