package base;

import utilities.Position;
import core.*;

/**
 * A Sprite that can move at a specified velocity and wrap around the screen
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class MovingSprite extends Sprite implements TimeSupport {
  /* constant used to ensure the MovingSprite is just in bounds while minimising its visibility */
  private final float RESPAWN_PADDING = 0.47f;
  private Velocity movementVelocity;

  public MovingSprite(
      World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos);
    movementVelocity = velocity;
  }

  /**
   * Gets the velocity that the MovingSprite is travelling at
   *
   * @return Velocity object specifying x and y speed
   */
  public Velocity getMovementVelocity() {
    return movementVelocity;
  }

  /**
   * Sets the velocity that the MovingSprite will travel at
   *
   * @param movementVelocity The Velocity to change to
   */
  public void setMovementVelocity(Velocity movementVelocity) {
    this.movementVelocity = movementVelocity;
  }

  /**
   * Gets the position of the closest horizontal edge of the screen to a given point
   *
   * @param xVal The point to find the closest edge to
   * @return The x coordinate of the closest horizontal edge of the scren
   */
  private float getClosestWall(float xVal) {
    float leftDiff = Math.abs(xVal - 0);
    float rightDiff = Math.abs(xVal - App.SCREEN_WIDTH);
    return leftDiff < rightDiff ? 0 : App.SCREEN_WIDTH;
  }

  /**
   * Identifies the closest horizontal edge of the app window
   *
   * @param yVal The y coordinate to find closest edge to
   * @return The y coordinate of the closest edge (0 or Screen Height)
   */
  private float getClosestCeiling(float yVal) {
    float bottomDiff = Math.abs(yVal - 0);
    float topDiff = Math.abs(yVal - App.SCREEN_HEIGHT);
    return bottomDiff < topDiff ? 0 : App.SCREEN_HEIGHT;
  }

  /**
   * Uses modulo arithmetic and error management to respawn the object at the appropriate point
   * (regardless of velocity - horizontal, diagonal, vertical, e.t.c).
   */
  public void respawn() {
    /* Variables to hold transformations to find new spawn location */
    float roundedX = centerPosition.getX();
    float roundedY = centerPosition.getY();
    /* Truth values to determine whether object has over-stepped vertical
     * or horizontal boundaries
     */
    boolean horizontalExtend = roundedX < 0 || roundedX > App.SCREEN_WIDTH;
    boolean verticalExtend = roundedY < 0 || roundedY > App.SCREEN_HEIGHT;
    /* performs loop arithmetic with error-fixed values p(new) = (p(old) + v) % (height/width)*/
    /* math.stackexchange.com/questions/2907303/finding-opposite-edge-wraparound-location-given-vector-and-location */
    float newX = roundedX + movementVelocity.getHorizontal();
    float newY = roundedY + movementVelocity.getVertical();
    newX = (newX % App.SCREEN_WIDTH) + (newX < 0 ? App.SCREEN_WIDTH : 0);
    newY = (newY % App.SCREEN_HEIGHT) + (newY < 0 ? App.SCREEN_HEIGHT : 0);
    /* performs further error-adjustment that may have resulted in bugged modulo arithmetic
     * also respawns bus as close to out-of-bounds as possible to enforce a smooth animation
     * */
    if (horizontalExtend) {
      float widthPadding = getWidth() * RESPAWN_PADDING;
      newX = getClosestWall(newX);
      if (newX == 0) {
        newX -= widthPadding;
      } else {
        newX += widthPadding;
      }
    }
    if (verticalExtend) {
      float heightPadding = getHeight() * RESPAWN_PADDING;
      newY = getClosestCeiling(newY);
      if (newY == 0) {
        newY -= heightPadding;
      } else {
        newY += heightPadding;
      }
    }
    /* returns, logs, and sets the new position */
    Position newPos = new Position(newX, newY);
    setLocation(newPos);
  }

  public void onTimeTick(int delta) {
    /* not necessary but reduces unnecessary calculations since this is called frequently*/
    if (movementVelocity.getMagnitude() == 0) {
      return;
    }
    if (outOfBounds()) {
      respawn();
      return;
    }
    float xDelta = movementVelocity.getHorizontal() * delta;
    float yDelta = movementVelocity.getVertical() * delta;
    float newX = centerPosition.getX() + xDelta;
    float newY = centerPosition.getY() + yDelta;
    setLocation(new Position(newX, newY));
  }
}
