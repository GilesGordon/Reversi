package reversi.view.graphic;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;



import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;

/**
 * Represents the panel view for the board of a Reversi game of square size.
 */
public class SquarePanel extends APanel {

  public SquarePanel(ReadonlyReversiModel model) {
    super(model);
  }

  protected Point convertCoordToGame(Coordinate c) {
    int x = c.q * shapeWidth + shapeWidth / 2;
    int y = c.r * shapeHeight + shapeHeight / 2;
    return new Point(x, y);
  }

  @Override
  protected Ellipse2D getPiece(Point p) {
    int height = shapeHeight / 2;
    int width = shapeWidth / 2;
    return new Ellipse2D.Double(p.x - width / 2., p.y - height / 2., width, height);
  }

  @Override
  protected Polygon getShape(Point p) {
    int[] xc = new int[4];
    xc[0] = p.x - shapeWidth / 2;
    xc[1] = p.x + shapeWidth / 2;
    xc[2] = p.x + shapeWidth / 2;
    xc[3] = p.x - shapeWidth / 2;

    int[] yc = new int[4];
    yc[0] = p.y - shapeHeight / 2;
    yc[1] = p.y - shapeHeight / 2;
    yc[2] = p.y + shapeHeight / 2;
    yc[3] = p.y + shapeHeight / 2;
    return new Polygon(xc, yc, 4);
  }

  @Override
  protected AffineTransform translationRender() {
    return new AffineTransform();
  }

  @Override
  protected void translationClick(Point p) {
    // do nothing
  }

  @Override
  protected void setSizes(ReadonlyReversiModel model) {
    this.shapeWidth = getPreferredSize().width / model.getSize();
    this.shapeHeight = getPreferredSize().height / model.getSize();
  }
}
