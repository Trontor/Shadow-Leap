package utilities;

import java.util.List;

/** Represents a location in the rectangular coordinates, specified in floating point precision. */
public class Position {
  /** Gets the origin used for this position class */
  private static final float ORIGIN = 0;
  /** The x (horizontal) coordinate */
  private final float x;
  /** The y (vertical) coordinate */
  private final float y;

  /** Initialises an instance of the Position class */
  public Position() {
    x = y = ORIGIN;
  }

  /**
   * Initialises an instance of the Position class with specified coordinates
   *
   * @param x The x coordinate to initialise the Position with
   * @param y The y coordinate to initialise the Position with
   */
  public Position(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the x coordinate
   *
   * @return Floating point value of the x coordinate
   */
  public float getX() {
    return x;
  }

  /**
   * Gets the y coordinate
   *
   * @return Floating point value of the y coordinate
   */
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
    for (int i = 1; i < list.size(); i++) {
      Position curr = list.get(i);
      if (distanceTo(curr) < distanceTo(closest)) closest = curr;
    }
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
