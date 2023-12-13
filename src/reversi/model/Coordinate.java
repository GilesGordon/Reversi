package reversi.model;

import java.util.Objects;

/**
 * Represents a basic 2D coordinate, meaning is dependent on the context in which it's used.
 */
public class Coordinate {

  public final int q;
  public final int r;

  /**
   * Is a two argument constructor for the Coordinate, allows users to input their desires
   * coordinate values.
   * @param q is the q value for the Coordinate.
   * @param r is the r value for the Coordinate.
   */
  public Coordinate(int q, int r) {
    this.q = q;
    this.r = r;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Coordinate)) {
      return false;
    }
    Coordinate c = (Coordinate) o;
    return c.q == this.q && c.r == this.r;
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r);
  }


}
