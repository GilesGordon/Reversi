package reversi.view.graphic;

import java.awt.Point;
import java.awt.Polygon;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import reversi.model.Coordinate;
import reversi.model.ReadonlyReversiModel;

/**
 * Represents a visual view for a reversi game that has a hexagon board.
 */
public class HexPanel extends APanel {

  public HexPanel(ReadonlyReversiModel model) {
    super(model);
  }

  @Override
  protected Point convertCoordToGame(Coordinate c) {
    int x = (c.q - (model.getSize() - 1))
            * shapeWidth + (c.r - (model.getSize() - 1)) * (shapeWidth / 2);
    int y = (c.r - (model.getSize() - 1)) * (shapeHeight * 3 / 4);
    return new Point(x, y);
  }

  @Override
  protected Ellipse2D getPiece(Point p) {
    int height = shapeHeight / 2;
    // width is based on height assuming board was square. This makes ellipses circles for any
    // square board but also always fits within hex.
    int width = (int) (getPreferredSize().width / ((model.getSize() * 2 - 2) * (.75) + 1)) / 2;
    return new Ellipse2D.Double(p.x - width / 2., p.y - height / 2., width, height);
  }

  @Override
  protected Polygon getShape(Point p) {
    int[] xc = new int[6];
    xc[0] = p.x - shapeWidth / 2;
    xc[1] = p.x;
    xc[2] = p.x + shapeWidth / 2;
    xc[3] = p.x + shapeWidth / 2;
    xc[4] = p.x;
    xc[5] = p.x - shapeWidth / 2;

    int[] yc = new int[6];
    yc[0] = p.y - shapeHeight / 4;
    yc[1] = p.y - shapeHeight / 2;
    yc[2] = p.y - shapeHeight / 4;
    yc[3] = p.y + shapeHeight / 4;
    yc[4] = p.y + shapeHeight / 2;
    yc[5] = p.y + shapeHeight / 4;
    return new Polygon(xc, yc, 6);
  }

  @Override
  protected AffineTransform translationRender() {
    AffineTransform transform = new AffineTransform();
    transform.setToTranslation(getPreferredSize().width / 2., getPreferredSize().height / 2.);
    return transform;
  }

  @Override
  protected void translationClick(Point p) {
    p.translate(-getWidth() / 2, -getHeight() / 2);
  }

  @Override
  protected void setSizes(ReadonlyReversiModel model) {
    this.shapeWidth = getPreferredSize().width / (model.getSize() * 2 - 1);
    this.shapeHeight = (int) (getPreferredSize().height / ((model.getSize() * 2 - 2) * (.75) + 1));
  }

}
