package customsprites;

import base.Obstacle;
import base.Velocity;
import core.World;

import utilities.Position;

public class BikeSprite extends Obstacle {

  /* Characteristics representing the bike's movement boundaries */
  private final int REVERSE_MIN_BOUND = 24;
  private final int REVERSE_MAX_BOUND = 1000;

  public BikeSprite(
      World spawnWorld,
      String obstacleName,
      String imageSrc,
      Position centerPos,
      Velocity speedInfo) {
    super(spawnWorld, obstacleName, imageSrc, centerPos, speedInfo);
    if (speedInfo.getHorizontal() < 0) {
      reverseImage();
    }
  }

  /** Changes the current image representation to a horizontally flipped one */
  private void reverseImage() {
    this.setImage(getImage().getFlippedCopy(true, false));
  }

  /**
   * Override handles the logic to keep the bike within specified boundaries
   *
   * @param delta The time in milliseconds since the last update
   */
  @Override
  public void onTimeTick(int delta) {
    float xApprox = getLocation().getX();
    if (xApprox <= REVERSE_MIN_BOUND || xApprox >= REVERSE_MAX_BOUND) {
      setMovementVelocity(getMovementVelocity().getOppositeVelocity(true, false));
      reverseImage();
      /* snaps the x value to the nearest boundary to maintain consistency */
      float snapX = xApprox <= REVERSE_MIN_BOUND ? REVERSE_MIN_BOUND : REVERSE_MAX_BOUND;
      Position resetPosition = new Position(snapX, getLocation().getY());
      setLocation(resetPosition);
    }
    super.onTimeTick(delta);
  }
}
