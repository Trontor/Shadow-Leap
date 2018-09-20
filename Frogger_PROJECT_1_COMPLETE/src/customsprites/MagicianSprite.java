package customsprites;

import base.Driver;
import utilities.Velocity;
import core.Level;
import org.newdawn.slick.Graphics;
import utilities.Position;

/** Represents a Sprite that can disappear and reappear */
public class MagicianSprite extends Driver {

  /** The time in seconds that the MagicianSprite should be periodically hidden */
  private static final float VISIBLE_TIME = 7;
  /** The time in seconds that the MagicianSprite should be periodically hidden */
  private static final float HIDDEN_TIME = 2;
  /** Flag indicating whether the MagicianSprite should be visible or not */
  private boolean visible = true;
  /** Tracks the time since the last visibility action was performed */
  private float currentTimeElapsed = 0;

  /**
   * Initialises a Magician Sprite that represents a Sprite that can change visibility
   *
   * @param spawnLevel The level to spawn the Magician Sprite on
   * @param name The name of the Magician Sprite
   * @param imgSrc The path to the image to represent the Magician Sprite
   * @param centerPos The location at which to spawn the Magician Sprite
   */
  public MagicianSprite(
      Level spawnLevel, String name, String imgSrc, Position centerPos, Velocity velocity) {
    super(spawnLevel, name, imgSrc, centerPos, velocity);
  }

  /**
   * Override method that handles the visibility switching of the Sprite
   *
   * @param delta The time in milliseconds since the last tick
   */
  @Override
  public void onTimeTick(int delta) {
    currentTimeElapsed += (float) delta / 1000;
    if (visible && currentTimeElapsed >= VISIBLE_TIME) {
      currentTimeElapsed = 0;
      visible = false;
      setRideable(false);
    } else if (!visible && currentTimeElapsed >= HIDDEN_TIME) {
      visible = true;
      setRideable(true);
      currentTimeElapsed = 0;
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
