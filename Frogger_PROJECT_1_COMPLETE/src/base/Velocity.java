package base;

/**
 * Represents values for speed in the x and y directions, in units: ms/px
 */
public class Velocity {
	/* immutable */
	private final float horizontal;
	private final float vertical;

	public float getHorizontal() {
		return horizontal;
	}
	
	public float getVertical() {
		return vertical;
	}

  public Velocity(float xSpeed, float ySpeed) {
    horizontal = xSpeed;
    vertical = ySpeed;
  }

  /**
   * Returns the euclidean norm of the Velocity as if it was a euclidean vector
   * @return
   */
	public float getMagnitude() {
		return (float)Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical,2));	
	}

  /**
   * Creates a Velocity object that has both/either x and/or y reversed
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
   * @return A String representing this Velocity in the form (x => xSpeed px/ms, y => ySpeed px/ms)
   */
	@Override
	public String toString() {
		return String.format("(x => %.2f px/ms, y => %.2f px/ms)", horizontal, vertical);

	}
}
