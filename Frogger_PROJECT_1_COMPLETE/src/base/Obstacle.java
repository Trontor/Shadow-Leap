package base;

import core.Level;
import utilities.Position;

/**
 * Class that represents all collidable obstacles in the game
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Obstacle extends MovingSprite implements Collidable {

  /**
   * Initialises an instance of an Obstacle Sprite
   *
   * @param spawnLevel The level the obstacle is spawned on
   * @param name The name of the obstacle
   * @param imgSrc The source image for the obstacle
   * @param centerPos The spawn center coordinates for the obstacle
   */
  public Obstacle(Level spawnLevel, String name, String imgSrc, Position centerPos) {
    super(spawnLevel, name, imgSrc, centerPos, new Velocity(0, 0));
  }

  /**
   * Overload used for moving obstacles
   *
   * @param spawnLevel The level the obstacle is spawned on
   * @param name The name of the obstacle
   * @param imgSrc The source image for the obstacle
   * @param centerPos The spawn center coordinates for the obstacle
   * @param speedInfo The velocity that the obstacle moves at
   */
  public Obstacle(
      Level spawnLevel, String name, String imgSrc, Position centerPos, Velocity speedInfo) {
    super(spawnLevel, name, imgSrc, centerPos, speedInfo);
  }
}
