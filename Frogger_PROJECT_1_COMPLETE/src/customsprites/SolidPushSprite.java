package customsprites;

import base.MovingSprite;
import base.PassengerSupport;
import base.Sprite;
import base.Velocity;
import core.Level;
import java.util.List;
import utilities.Position;

/**
 * Represents a Sprite that pushes any other Sprite it makes contact with in its direction of
 * motion.
 */
public class SolidPushSprite extends MovingSprite {

  public SolidPushSprite(
      Level spawnLevel, String Name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnLevel, Name, imageSrc, centerPos, velocity);
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
      float pushX = sprite.getLocation().getX() + getMovementVelocity().getHorizontal() * delta;
      sprite.setLocation(new Position(pushX, getLocation().getY()));
    }
    super.onTimeTick(delta);
  }
}
