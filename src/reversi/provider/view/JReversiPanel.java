package reversi.provider.view;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JOptionPane;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModel;


import reversi.provider.model.ReversiModelReadOnly;
import reversi.provider.player.PlayerActionFeatures;
import reversi.provider.player.PlayerActionListener;

/**
 * Draws the GUI of the Reversi model and allow users to click and press keys to
 * play the game. Extends JPanel and makes use of JPanel methods to paint components
 * (such as the hexagons) onto the window.
 */
public class JReversiPanel extends JPanel implements PlayerActionFeatures {
  /**
   * Listeners to inform when certain events such as MouseEvents or KeyboardEvents occur.
   * Not used yet due to controller not being implemented.
   */
  private final List<PlayerActionListener> featuresListeners;

  /**
   * Length of each side of a hexagon tile.
   */
  private static final double SIDE_LENGTH = 5.0;
  /**
   * Space between each tile.
   */
  private static final double GAP_SPACE = 0.05;

  private final ReversiModelReadOnly rmro;

  private Graphics2D g2d;

  private HexaCoordinate selectedTileCoord;

  /**
   * Constructor for JReversiPanel.
   *
   * @param rmro ReversiModel being viewed.
   */
  public JReversiPanel(ReversiModelReadOnly rmro) {
    this.featuresListeners = new ArrayList<>();
    this.rmro = rmro;
    MouseEventsListener mouseListener = new MouseEventsListener();
    KeyboardListener keyListener = new KeyboardListener();
    this.addMouseListener(mouseListener);
    this.setFocusable(true);
    this.requestFocus();
    this.addKeyListener(keyListener);
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener ctrl) {
    this.featuresListeners.add(Objects.requireNonNull(ctrl));
  }

  @Override
  public void passPlayerTurn() {
    for (PlayerActionListener vf : this.featuresListeners) {
      vf.pass();
    }
  }

  @Override
  public void placePlayerTile(HexaCoordinate hc) {
    for (PlayerActionListener vf : this.featuresListeners) {
      vf.placeTile(hc);
    }
  }

  /**
   * Shows in the view that the given move is invalid.
   */
  public void displayIllegalMoveError(Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
            JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Displays that the game is over and shows the winner or tie.
   */
  public void displayGameOver() {
    JLabel winMessage = new JLabel();
    winMessage.setFont(new Font("Arial", Font.BOLD, 30));
    winMessage.setLocation(0, this.getHeight());
    ReversiModel.GameState gs = this.rmro.getWinner();
    String message;
    switch (gs) {
      case BLACKWIN:
        winMessage.setText("Black wins!");
        winMessage.setForeground(Color.BLACK);
        break;
      case WHITEWIN:
        winMessage.setText("White wins!");
        winMessage.setForeground(Color.WHITE);
        break;
      case TIE:
        winMessage.setText("It's a Tie");
        winMessage.setForeground(Color.GRAY);
        break;
      default:
        throw new IllegalStateException("Display game over is being called before the game" +
                " is over.");
    }
    this.add(winMessage);
  }

  @Override
  public Dimension getPreferredSize() {
    // Get size of window based off size of board.
    int pixels = 65 * ((this.rmro.getBoardSize() * 2) + 1);
    return new Dimension(pixels, pixels);
  }

  /**
   * Arbitrary size of the pixels inside the window, similar to a scale.
   *
   * @return a Dimension representing the size inside the window.
   */
  private Dimension getLogicalSize() {
    int numInRow = ((this.rmro.getBoardSize() * 2) + 1);
    double size = SIDE_LENGTH * 2 * numInRow;
    return new Dimension((int) size, (int) size);
  }

  /**
   * Creates a transformation that brings the origin (0,0) to the center of the view and changes
   * the Y axis so that up is positive and down is negative.
   *
   * @return The desired transformation.
   */
  private AffineTransform logicalToPhysicalTransformation() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = this.getLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    ret.scale(1, -1);
    return ret;
  }

  /**
   * Creates a transformation that brings the origin (0,0) to the top left of the view and changes
   * the Y axis so that up is negative and down is positive.
   *
   * @return The desired transformation.
   */
  private AffineTransform physicalToLogicalTransformation() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = this.getLogicalSize();
    ret.scale(1, -1);
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.g2d = (Graphics2D) g.create();

    // Fill background
    Rectangle bounds = this.getBounds();
    this.g2d.draw(bounds);
    this.g2d.fill(bounds);

    // Transform
    this.g2d.transform(this.logicalToPhysicalTransformation());

    // Number of steps from center of board to edge
    int n = this.rmro.getBoardSize();
    // Loop through each coordinate based off given Hexacoordinate loop instructions.
    for (int q = -n; q <= n; q++) {
      for (int r = Integer.max(-n, -q - n); r <= Integer.min(n, -q + n); r++) {
        // Coordinate at current iteration
        HexaCoordinate coord = new HexaCoordinate(r, q);
        // Draw the hexagon at that coordinate
        this.drawHexagonAtHexacoord(this.g2d, coord);
      }
    }
  }

  /**
   * Converts the given Hexacoordinate to an XY window coordinate for the center of the
   * Hexagon and draws it on the given Graphics2D object. Colors in the tile and potentailly a
   * circle in the hexagon depending on the state of the tile and the selected tile.
   *
   * @param g2d   Graphics2D object that the hexagon is being drawn on.
   * @param coord Hexacoordinates of the hexagon to be drawn.
   */
  private void drawHexagonAtHexacoord(Graphics2D g2d, HexaCoordinate coord) {
    Point2D.Double xyCoord = this.getHexagonCenterCoord(coord);
    double x = xyCoord.x;
    double y = xyCoord.y;

    // Default background tile color
    Color tileColor = Color.GRAY;

    ReversiModel.TileState state = this.rmro.getTileStateAt(coord);

    // If this tile is selected, change the color to blue
    if (this.selectedTileCoord != null && this.selectedTileCoord.equals(coord)) {
      tileColor = Color.BLUE;
    }

    // Draw the tile and the token in the tile if present
    if (state.equals(ReversiModel.TileState.BLACK)) {
      this.drawHexagon(g2d, x, y, tileColor, true, Color.BLACK);
    } else if (state.equals(ReversiModel.TileState.WHITE)) {
      this.drawHexagon(g2d, x, y, tileColor, true, Color.WHITE);
    } else {
      this.drawHexagon(g2d, x, y, tileColor, false, Color.black);
    }
  }

  /**
   * Converts a Hexacoordinate on a board of Reversi to an XY coordinate.
   * Uses logical coordinates.
   *
   * @param coord Hexacoordinates being converted.
   * @return A Point consisting of the new X and Y coordinates.
   */
  private Point.Double getHexagonCenterCoord(HexaCoordinate coord) {
    int q = coord.getQ();
    int r = coord.getR();

    // Amount to offset x based off how many rows up or down the iteration is at (R).
    // sqrt(3)/2 is half the width of a hexagon from the center to the left or right edge
    // Move that many halves down left or right depending on how many rows up or down you are
    double xOffset = r * (Math.sqrt(3) / 2 + GAP_SPACE / 2) * SIDE_LENGTH;

    // X coordinate to place hexagon
    // q = num cols to the left or right
    // (sqrt(3) + gap_space) * SIDE_LENGTH = width of each hexagon + filler space
    // offset accounts for stacking each row of hexagons on top of each other so that the points
    // are half a tile off from the row above/below
    double widthFactor = Math.sqrt(3);
    double x = (q * (widthFactor + GAP_SPACE) * SIDE_LENGTH) + xOffset;


    // Y coordinate to place hexagon
    // 3.0/2.0 is the vertical distance between the centers of two hexagons on top
    // of each other where the top/bottom points reach the bottom of the sides of the hexagon
    // - Add 0.05 for gap space
    // - Multiply by r - number of rows up or down. Negate r since Hexacoordinate system's negative
    //   r direction goes up.
    // - Multiply by SIDE_LENGTH for scale
    double y = (3.0 / 2.0 + GAP_SPACE) * (-1 * r) * SIDE_LENGTH;

    return new Point.Double(x, y);
  }

  /**
   * Draws a Hexagon where the center of the hexagon is at the given coordinates.
   *
   * @param g2d             Graphics2D object being drawn on.
   * @param x               x-coordinate of the center of the hexagon.
   * @param y               y-coordinate of the center of the hexagon.
   * @param colorBackground Color of the background (hexagon).
   * @param innerCircle     Whether there is an inner circle. True means there is.
   * @param colorCircle     Color of the inner circle.
   */
  private void drawHexagon(Graphics2D g2d, double x, double y, Color colorBackground,
                           boolean innerCircle, Color colorCircle) {
    // Old settings to reset back to
    AffineTransform oldTransform = g2d.getTransform();
    Color oldColor = g2d.getColor();
    Stroke oldStroke = g2d.getStroke();

    // Change settings
    g2d.translate(x, y);
    g2d.setColor(colorBackground);
    g2d.setStroke(new BasicStroke(0.1f));

    // Make hexagon background
    Path2D.Double hexagon = this.makeHexagon();
    g2d.draw(hexagon);
    g2d.fill(hexagon);

    // Create and fill a circle inside the hexagon if necessary
    if (innerCircle) {
      g2d.setColor(colorCircle);

      Shape circle = new Ellipse2D.Double(-SIDE_LENGTH / 2, -SIDE_LENGTH / 2,
              SIDE_LENGTH, SIDE_LENGTH);
      g2d.draw(circle);
      g2d.fill(circle);
    }

    // Reset settings
    g2d.setTransform(oldTransform);
    g2d.setColor(oldColor);
    g2d.setStroke(oldStroke);
  }

  /**
   * Creates a hexagon shape using coordinates and geometry based off a perfect hexagon
   * being six equilateral triangles.
   *
   * @return A hexagon shape.
   */
  private Path2D.Double makeHexagon() {
    Path2D.Double hexagon = new Path2D.Double();
    // Top center
    hexagon.moveTo(0, SIDE_LENGTH);
    // Top right
    hexagon.lineTo(SIDE_LENGTH * Math.sqrt(3) / 2, SIDE_LENGTH / 2);
    // Bottom right
    hexagon.lineTo(SIDE_LENGTH * Math.sqrt(3) / 2, -SIDE_LENGTH / 2);
    // Bottom center
    hexagon.lineTo(0, -SIDE_LENGTH);
    // Bottom left
    hexagon.lineTo(-SIDE_LENGTH * Math.sqrt(3) / 2, -SIDE_LENGTH / 2);
    // Top left
    hexagon.lineTo(-SIDE_LENGTH * Math.sqrt(3) / 2, SIDE_LENGTH / 2);
    // Top center
    hexagon.closePath();

    return hexagon;
  }

  private HexaCoordinate coordInHexagon(Point coordXY, HexaCoordinate coordHex)
          throws IllegalStateException {
    // Old settings to reset back to
    AffineTransform oldTransform = this.g2d.getTransform();
    // Physical point clicked converted to logical coordinate
    Point2D logicalClickCoord = physicalToLogicalTransformation().transform(coordXY, null);
    // Transform the graphic object so the given Hexacoord is at the center of the window
    Point2D.Double newCenter = this.getHexagonCenterCoord(coordHex);
    this.g2d.translate(newCenter.x, newCenter.y);

    double xClick = logicalClickCoord.getX() - newCenter.x;
    double yClick = logicalClickCoord.getY() - newCenter.y;
    boolean inside = false;

    if (Math.abs(xClick) <= (SIDE_LENGTH) * (Math.sqrt(3) + GAP_SPACE) / 2
            && Math.abs(yClick) <= (SIDE_LENGTH)) {
      if (xClick < 0) {
        // Check left side
        if (yClick > (SIDE_LENGTH) / 2) {
          // Check top left
          // greatest value yClick CAN be to be in the hexagon
          double upperY = (xClick / Math.sqrt(3)) + (SIDE_LENGTH);
          if (yClick <= upperY) {
            inside = true;
          }
        } else if (yClick > (-SIDE_LENGTH) / 2) {
          // Check middle left
          inside = true;
        } else {
          // Check bottom left
          // lowest value yClick CAN be to be in the hexagon
          double lowerY = (-xClick / Math.sqrt(3)) - (SIDE_LENGTH);
          if (yClick >= lowerY) {
            inside = true;
          }
        }
      } else {
        // Check right side
        if (yClick > (SIDE_LENGTH) / 2) {
          // Check top right
          // greatest value yClick CAN be to be in the hexagon
          double upperY = (-xClick / Math.sqrt(3)) + (SIDE_LENGTH);
          if (yClick <= upperY) {
            inside = true;
          }
        } else if (yClick > (-SIDE_LENGTH) / 2) {
          // Check middle right
          inside = true;
        } else {
          // Check bottom right
          // lowest value yClick CAN be to be in the hexagon
          double lowerY = (xClick / Math.sqrt(3)) - (SIDE_LENGTH);
          if (yClick >= lowerY) {
            inside = true;
          }
        }
      }
    }
    // Reset stats & check if the coord is inside
    return this.coordInHexHelper(oldTransform, inside, coordHex);
  }

  private HexaCoordinate coordInHexHelper(AffineTransform old, boolean ret, HexaCoordinate coord)
          throws IllegalStateException {
    this.g2d.transform(old);
    if (ret) {
      return coord;
    } else {
      throw new IllegalStateException();
    }
  }

  /**
   * Class for mouse events on the panel, specifically handling mouse up to signal a release
   * of a click on a given tile.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseReleased(MouseEvent e) {
      HexaCoordinate clickedHex = null;
      // Number of steps from center of board to edge
      int n = rmro.getBoardSize();
      // Loop through each coordinate based off given Hexacoordinate loop instructions.
      for (int q = -n; q <= n; q++) {
        for (int r = Integer.max(-n, -q - n); r <= Integer.min(n, -q + n); r++) {
          try {
            clickedHex = coordInHexagon(e.getPoint(), new HexaCoordinate(r, q));
          } catch (IllegalStateException exc) {
            // Do nothing
          }
        }
      }
      if (clickedHex != null) {
        int q = clickedHex.getQ();
        int r = clickedHex.getR();
        HexaCoordinate newSelected = new HexaCoordinate(r, q);
        if (selectedTileCoord != null && selectedTileCoord.equals(newSelected)) {
          selectedTileCoord = null;
        } else {
          selectedTileCoord = newSelected;
        }
        repaint();
      } else {
        // Deselect the sell
        selectedTileCoord = null;
        repaint();
      }
    }
  }

  /**
   * Class for key events on the panel, specifically handling key presses to place a tile.
   */
  private class KeyboardListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 80) {
        // Player presses P to pass turn
        // Update the listeners to pass turn
        passPlayerTurn();
      } else if (e.getKeyCode() == 10) {
        // Player presses Enter to place tile on selected coordinate
        // Update the listeners to place tile
        placePlayerTile(selectedTileCoord);
      }
    }
  }
}