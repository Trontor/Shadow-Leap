package customsprites;

import base.MovingSprite;
import base.Rideable;
import base.Sprite;
import base.Velocity;
import core.World;
import java.util.List;

import utilities.Position;

public class SolidPushSprite extends MovingSprite {

  public SolidPushSprite(
      World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos, velocity);
  }

  @Override
  public void onTimeTick(int delta) {
    List<Sprite> intersectingSprites =
        getWorld().spriteManager.getIntersectingSprites(this, s -> s instanceof Rideable);
    for (Sprite sprite : intersectingSprites) {
      float pushX = sprite.getPosition().getX() + getMovementVelocity().getHorizontal() * delta;
      sprite.setLocation(new Position(pushX, getPosition().getY()));
    }
    super.onTimeTick(delta);
  }
}
