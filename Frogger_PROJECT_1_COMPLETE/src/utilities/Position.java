package utilities;

import java.util.List;

/** Represents a location in the rectangular coordinates, specified in floating point precision. */
public class Position {
  /* immutable */
  private final float x;
  private final float y;

  public Position() {
    /* defaults are 0 */
    x = y = 0;
  }

  public Position(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  /**
   * Returns the euclidean distance to another Position object
   *
   * @param compare The position object to get the distance to
   * @return The euclidean distance
   */
  public float distanceTo(Position compare) {
    float deltaX = compare.getX() - this.getX();
    float deltaY = compare.getY() - this.getY();
    return (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
  }

  /**
   * Returns the closest point from a list of points to the current Position object
   *
   * @param list A list of points to check the closest distance to
   * @return The Position object that is nearest to this Position
   */
  public Position getClosest(List<Position> list) {
    if (list == null || list.size() == 0) {
      return null;
    }
    Position closest = list.get(0);
    System.out.print("|| Curr = " + this + " || Size= " + list.size() + " || ");
    System.out.print(" || " + closest + " || ");
    for (int i = 1; i < list.size(); i++) {
      Position curr = list.get(i);
      System.out.print(" || " + curr + " || ");
      if (distanceTo(curr) < distanceTo(closest)) closest = curr;
    }
    System.out.println("Closest = " + closest);
    return closest;
  }

  /**
   * Checks the equality between two objects
   *
   * @param obj the object to check equality to
   * @return False if not Position or x/y do not match, True if x/y match
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Position)) return false;
    Position p = ((Position) obj);
    return p.getX() == x && p.getY() == y;
  }

  /**
   * Converts the Position object to a human readable String
   *
   * @return The x and y values of this Position in the form (x, y)
   */
  @Override
  public String toString() {
    return String.format("(%.2f, %.2f)", x, y);
  }
}
