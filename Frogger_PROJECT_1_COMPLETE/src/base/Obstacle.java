package base;

import utilities.Position;
import core.World;

/**
 * Class that represents all collidable obstacles in the game
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Obstacle extends MovingSprite implements Collidable {
  /** Initialises a new Obstacle */
  public Obstacle(World spawnWorld, String name, String imgSrc, Position centerPos) {
    super(spawnWorld, name, imgSrc, centerPos, new Velocity(0, 0));
  }

  /**
   * Overload used for moving obstacles
   *
   * @param world The world the obstacle is spawned on
   * @param name The name of the obstacle
   * @param imgSrc The source image for the obstacle
   * @param centerPos The spawn center coordinates for the obstacle
   * @param speedInfo The velocity that the obstacle moves at
   */
  public Obstacle(World world, String name, String imgSrc, Position centerPos, Velocity speedInfo) {
    super(world, name, imgSrc, centerPos, speedInfo);
  }
}
