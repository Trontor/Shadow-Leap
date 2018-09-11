package base;

import utilities.Position;
import core.World;

/** Concrete class that represents all collidable obstacles in the game
 * @author Rohyl Joshi
 */
public class Obstacle extends MovingSprite implements Collidable {
	/** Initialises a new base.Obstacle */
	public Obstacle(World spawnWorld, String obstacleName, String imageSrc, Position centerPos)  {
		super(spawnWorld, obstacleName, imageSrc, centerPos, new Velocity(0,0));
	}
	
	public Obstacle(World spawnWorld, String obstacleName, String imageSrc, Position centerPos, Velocity speedInfo) {
		super(spawnWorld, obstacleName, imageSrc, centerPos, speedInfo);
	} 
}