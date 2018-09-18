package customsprites;

import base.Driver;
import base.Velocity;
import core.Level;
import org.newdawn.slick.Graphics;
import utilities.Position;

/** Represents a Sprite that can disappear and reappear */
public class MagicianSprite extends Driver {

  /* Characteristics and default values */
  private final float VISIBLE_TIME = 7;
  private final float HIDDEN_TIME = 2;
  private boolean visible = true;
  private float currentTime = 0;

  public MagicianSprite(
      Level spawnLevel, String Name, String imageSrc, Position centerPos, Velocity velocity) {
    super(spawnLevel, Name, imageSrc, centerPos, velocity);
  }

  /**
   * Override method that handles the visibility switching of the Sprite
   *
   * @param delta The time in milliseconds since the last tick
   */
  @Override
  public void onTimeTick(int delta) {
    currentTime += (float) delta / 1000;
    if (visible && currentTime >= VISIBLE_TIME) {
      currentTime = 0;
      visible = false;
      setRideable(false);
    } else if (!visible && currentTime >= HIDDEN_TIME) {
      visible = true;
      setRideable(true);
      currentTime = 0;
    }
    super.onTimeTick(delta);
  }

  /**
   * Override method that only renders the Sprite if it is supposed to be visible
   *
   * @param g The Graphics object to render the base.Sprite on
   */
  @Override
  public void render(Graphics g) {
    if (visible) super.render(g);
  }
}
