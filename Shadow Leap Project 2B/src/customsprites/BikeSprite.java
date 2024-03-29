package customsprites;

import base.Obstacle;
import utilities.Velocity;
import core.Level;
import utilities.Position;

public class BikeSprite extends Obstacle {

  /** The left bound that the bike reverses at */
  private static final int REVERSE_MIN_BOUND = 24;
  /** The right bound that the bike reverses at */
  private static final int REVERSE_MAX_BOUND = 1000;

  /**
   * Initialises a new Sprite object
   *
   * @param spawnLevel The level to spawn the Bike Sprite on
   * @param obstacleName The level to spawn the Bike Sprite on
   * @param imageSrc The path to the image to represent the sprite
   * @param centerPos The location to spawn the Bike Sprite at
   * @param speedInfo The movement velocity to move the Bike Sprite at
   */
  public BikeSprite(
      Level spawnLevel,
      String obstacleName,
      String imageSrc,
      Position centerPos,
      Velocity speedInfo) {
    super(spawnLevel, obstacleName, imageSrc, centerPos, speedInfo);
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
