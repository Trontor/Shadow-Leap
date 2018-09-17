package customsprites;

import base.MovingSprite;
import base.Rideable;
import base.Sprite;
import base.Velocity;
import core.World;
import java.util.List;

import utilities.Position;

/**
 * Represents a Sprite that pushes any other Sprite it makes contact with in its direction of
 * motion.
 */
public class SolidPushSprite extends MovingSprite {

  public SolidPushSprite(
      World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos, velocity);
  }

  /**
   * Override method to handle pushing logic
   * @param delta The time (in ms) since the last update
   */
  @Override
  public void onTimeTick(int delta) {
    List<Sprite> intersectingSprites =
        getWorld().spriteManager.getIntersectingSprites(this, s -> s instanceof Rideable);
    for (Sprite sprite : intersectingSprites) {
      float pushX = sprite.getLocation().getX() + getMovementVelocity().getHorizontal() * delta;
      sprite.setLocation(new Position(pushX, getLocation().getY()));
    }
    super.onTimeTick(delta);
  }
}
