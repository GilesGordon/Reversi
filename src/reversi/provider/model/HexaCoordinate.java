package reversi.provider.model;

/**
 * Represents a point on a Hexagonal Coordinate System. Note that this point can change.
 * The r axis goes diagonal from the bottom right (positive) to the
 * top left (negative). The q axis goes from the right (positive) to the left (negative).
 * The origin (0,0) is at the center of the board.
 */
public class HexaCoordinate {
  private int r;
  private int q;

  /**
   * Constructor for HexaCoordinates.
   *
   * @param r R coordinate.
   * @param q Q coordinate.
   */
  public HexaCoordinate(int r, int q) {
    this.r = r;
    this.q = q;
  }

  /**
   * Observes the Q coordinate of the HexaCoordinate.
   *
   * @return the Q coordinate.
   */
  public int getQ() {
    return q;
  }

  /**
   * Observes the R coordinate of the HexaCoordinate.
   *
   * @return the R coordinate.
   */
  public int getR() {
    return r;
  }

  /**
   * Sets the location of the coordinate to those of the given coordinate.
   *
   * @param coords The given coordates being copied.
   * @throws IllegalArgumentException if the given argument is null.
   */
  public void setLocation(HexaCoordinate coords) {
    if (coords == null) {
      throw new IllegalArgumentException("Cannot take null input");
    }

    this.r = coords.getR();
    this.q = coords.getQ();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof HexaCoordinate)) {
      return false;
    }

    HexaCoordinate other = (HexaCoordinate) o;

    return (this.getQ() == other.getQ()) && (this.getR() == other.getR());
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 5 * this.q;
    hash = 7 * this.r;
    return hash;
  }
}
