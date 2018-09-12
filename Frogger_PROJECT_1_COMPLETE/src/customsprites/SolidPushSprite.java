package customsprites;

import base.Driver;
import base.MovingSprite;
import base.Rideable;
import base.Sprite;
import base.Velocity;
import core.World;
import java.util.List;
import java.util.stream.Collectors;
import utilities.Position;

public class SolidPushSprite extends MovingSprite {

  public SolidPushSprite(World spawnWorld, String Name, String imageSrc, Position centerPos,
      Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos, velocity);
  }

  @Override
  public void onTimeTick(int delta) {
    List<Sprite> intersectingSprites = getWorld().getIntersectingSprites(this)
                                                 .stream()
                                                 .filter(s-> s instanceof Rideable)
                                                 .collect(Collectors.toList());
    for (Sprite sprite : intersectingSprites) {
          float pushX = sprite.getPosition().getX() + getMovementVelocity().getHorizontal()*delta;
          sprite.setLocation(new Position(pushX, getPosition().getY()));
      }
    super.onTimeTick(delta);
  }
}
