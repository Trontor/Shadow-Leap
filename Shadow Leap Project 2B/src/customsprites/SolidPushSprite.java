package customsprites;

import base.MovingSprite;
import base.PassengerSupport;
import base.Sprite;
import utilities.Velocity;
import core.Level;
import utilities.BoundingBox;
import utilities.Position;

import java.util.List;

/**
 * Represents a Sprite that pushes any other Sprite it makes contact with in its direction of
 * motion.
 */
public class SolidPushSprite extends MovingSprite {

  /**
   * Initialises a new Moving Sprite object
   *
   * @param spawnLevel The level to spawn the sprite on
   * @param name The name of the Sprite
   * @param imageSrc The path to the image to represent the sprite
   * @param centerPos The location to spawn the Sprite at
   * @param velocity The movement velocity to move the Sprite at
   */
  public SolidPushSprite(
      Level spawnLevel, String name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnLevel, name, imageSrc, centerPos, velocity);
    super.setHitBox(new BoundingBox(getImage(), getLocation(), true));
  }

  /**
   * Override method to handle pushing logic
   *
   * @param delta The time (in ms) since the last update
   */
  @Override
  public void onTimeTick(int delta) {
    List<Sprite> intersectingSprites =
        getLevel()
            .getSpriteManager()
            .getIntersectingSprites(this, s -> s instanceof PassengerSupport);
    for (Sprite sprite : intersectingSprites) {
      if (sprite.getLocation().getX() < this.getBottomLeftAnchor().getX()) continue;
      float pushX = sprite.getLocation().getX() + getMovementVelocity().getHorizontal() * delta;
      sprite.setLocation(new Position(pushX, getLocation().getY()));
    }
    super.onTimeTick(delta);
  }
}
