package customsprites;

import base.Driver;
import base.MovingSprite;
import base.Rideable;
import base.Sprite;
import base.Velocity;
import core.World;
import utilities.Position;

public class SolidPushSprite extends Driver {

  public SolidPushSprite(World spawnWorld, String Name, String imageSrc, Position centerPos,
      Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos, velocity);
  }
  @Override
  public void onTimeTick(int delta) {
    super.onTimeTick(delta);
    for (Sprite s : super.getWorld().getIntersectingSprites(this)){
      float pushX = s.getPosition().getX() + getMovementVelocity().getHorizontal()*delta;
      s.setLocation(new Position(pushX, getPosition().getY()));
    }
  }
}
