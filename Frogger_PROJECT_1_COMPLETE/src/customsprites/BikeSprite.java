package customsprites;

import base.Obstacle;
import base.Velocity;
import core.World;
import java.util.ArrayList;
import java.util.List;
import utilities.Position;

public class BikeSprite extends Obstacle {

  private final int REVERSE_MIN_BOUND = 24;
  private final int REVERSE_MAX_BOUND = 1000;
  public BikeSprite(World spawnWorld, String obstacleName, String imageSrc,
      Position centerPos, Velocity speedInfo) {
    super(spawnWorld, obstacleName, imageSrc, centerPos, speedInfo);
  }

  @Override
  public void onTimeTick(int delta) {
    super.onTimeTick(delta);
    int xApprox = (int)getPosition().getX();
    if (xApprox < REVERSE_MIN_BOUND || xApprox > REVERSE_MAX_BOUND){
      Velocity currVel = getMovementVelocity();
      Velocity newVel = new Velocity(currVel.getHorizontal() * -1, currVel.getVertical());
      setMovementVelocity(newVel);
    }
  }
}
