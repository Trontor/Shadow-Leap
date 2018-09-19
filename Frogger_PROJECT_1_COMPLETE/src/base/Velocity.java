package base;

/** Represents values for speed in the x and y directions, in units: ms/px */
public class Velocity {
  /** The scalar speed in the horizontal direction */
  private final float horizontal;
  /** The scalar speed in the vertical direction */
  private final float vertical;

  public Velocity(float xSpeed, float ySpeed) {
    horizontal = xSpeed;
    vertical = ySpeed;
  }

  /**
   * Returns the scalar speed in the horizontal direction
   *
   * @return Floating point value of horizontal speed in px/ms
   */
  public float getHorizontal() {
    return horizontal;
  }

  /**
   * Returns the scalar speed in the verical direction
   *
   * @return Floating point value of horizontal speed in px/ms
   */
  public float getVertical() {
    return vertical;
  }

  /**
   * Returns the euclidean norm of the Velocity as if it was a euclidean vector
   *
   * @return Floating point rerpesentation of the magnitude
   */
  public float getMagnitude() {
    return (float) Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
  }

  /**
   * Creates a Velocity object that has both/either x and/or y reversed
   *
   * @param oppositeX True to flip the x speed, else False
   * @param oppositeY True to flip the y speed, else False
   * @return A new Velocity object that obeys the specified parameters
   */
  public Velocity getOppositeVelocity(boolean oppositeX, boolean oppositeY) {
    int xChange = oppositeX ? -1 : 1;
    int yChange = oppositeY ? -1 : 1;
    return new Velocity(xChange * horizontal, yChange * vertical);
  }

  /**
   * A human readable description of the velocity object
   *
   * @return A String representing this Velocity in the form (x => xSpeed px/ms, y => ySpeed px/ms)
   */
  @Override
  public String toString() {
    return String.format("(x => %.2f px/ms, y => %.2f px/ms)", horizontal, vertical);
  }
}
