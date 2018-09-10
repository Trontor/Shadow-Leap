import org.newdawn.slick.SlickException;
import utilities.Position;

/** Concrete class that represents all collidable obstacles in the game
 * @author Rohyl Joshi
 */
public class Obstacle extends MovingSprite implements Collidable {  
	/** Initialises a new Obstacle
	 * @param spawn_world The world to spawn the Obstacle on
	 * @param Name The name of the Obstacle 
	 * @param imageSrc The path to the image to represent the Obstacle 
	 * @param centerPos The location to spawn the Obstacle 
	 * @param speedInfo The Velocity the Obstacle moves at
	 * @throws SlickException Slick Libary Error
	 */
	public Obstacle(World world, String obstacleName, String imageSrc, Position centerPos) throws SlickException {
		super(world, obstacleName, imageSrc, centerPos, new Velocity(0,0));
	}
	
	public Obstacle(World world, String obstacleName, String imageSrc, Position centerPos, Velocity speedInfo) throws SlickException {
		super(world, obstacleName, imageSrc, centerPos, speedInfo); 
	} 
}