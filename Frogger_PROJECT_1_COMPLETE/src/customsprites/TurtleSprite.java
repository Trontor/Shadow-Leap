package customsprites;

import base.Driver;
import base.Velocity;
import core.World;
import org.newdawn.slick.Graphics;
import utilities.Position;

public class TurtleSprite extends Driver {

  private boolean visible = true;
  private final float VISIBLE_TIME = 7;
  private final float HIDDEN_TIME = 2;
  private float currentTime = 0;

  public TurtleSprite(World spawnWorld, String Name, String imageSrc, Position centerPos,
      Velocity velocity) {
    super(spawnWorld, Name, imageSrc, centerPos, velocity);
  }

  @Override
  public void onTimeTick(int delta) {
    currentTime += (float)delta/1000;
    if (visible && currentTime >= VISIBLE_TIME) {
      currentTime = 0;
      visible = false;
      setRideable(false);
    } else if (!visible && currentTime >= HIDDEN_TIME){
      visible = true;
      setRideable(true);
      currentTime = 0;
    }
    super.onTimeTick(delta);
  }

  @Override
  public void render(Graphics g) {
    if (visible)
    super.render(g);
  }
}
