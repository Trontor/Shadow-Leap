import org.newdawn.slick.SlickException;
import utilities.Position;

/** Concrete class that represents all collidable obstacles in the game
 * @author Rohyl Joshi
 */
public class Obstacle extends Sprite implements TimeSupport, Collidable {
	
	/**
	 * Details movement speed and direction of obstacle
	 */
	private Velocity movementVelocity; 	
	
	public Velocity getMovementVelocity() {
		return movementVelocity;
	}

	public void setMovementVelocity(Velocity movementVelocity) {
		this.movementVelocity = movementVelocity;
	}

	/** Initialises a new Obstacle
	 * @param spawn_world The world to spawn the Obstacle on
	 * @param Name The name of the Obstacle 
	 * @param imageSrc The path to the image to represent the Obstacle 
	 * @param centerPos The location to spawn the Obstacle 
	 * @param speedInfo The Velocity the Obstacle moves at
	 * @throws SlickException Slick Libary Error
	 */
	public Obstacle(World world, String obstacleName, String imageSrc, Position centerPos, Velocity speedInfo) throws SlickException {
		super(world, obstacleName, imageSrc, centerPos);
		if (speedInfo == null) {
			speedInfo = new Velocity(0,0);
		}
		movementVelocity = speedInfo;
	}

	/** Identifies the closest vertical edge of the app window
	 * @param xVal The x coordinate to find closest edge to 
	 * @return The x coordinate of the closest edge (0 or Screen Width)
	 */
	private float getClosestWall(float xVal) {
		float leftDiff = Math.abs(xVal - 0);
		float rightDiff = Math.abs(xVal - App.SCREEN_WIDTH);
		return leftDiff < rightDiff ? 0: App.SCREEN_WIDTH;
	}
	/** Identifies the closest horizontal edge of the app window
	 * @param yVal The y coordinate to find closest edge to 
	 * @return The y coordinate of the closest edge (0 or Screen Height)
	 */
	private float getClosestCeiling(float yVal) {
		float bottomDiff = Math.abs(yVal - 0);
		float topDiff = Math.abs(yVal - App.SCREEN_HEIGHT);
		return bottomDiff < topDiff  ? 0: App.SCREEN_HEIGHT;
	}
	

	/** Uses modulo arithmetic and error management to respawn the object 
	 * at the appropriate point (regardless of velocity).
	 */
	private void respawn() { 
		/* Variables to hold transformations to find new spawn location */
		float roundedX = centerPosition.getX();
		float roundedY = centerPosition.getY();
		/* Truth values to determine whether object has over-stepped vertical 
		 * or horizontal boundaries 
		 */
		boolean horizontalExtend = roundedX < 0 || roundedX > App.SCREEN_WIDTH;
		boolean verticalExtend = roundedY < 0 || roundedY > App.SCREEN_HEIGHT;
		/* Rounds the extension to the nearest edge to account for over-extension */
		if (horizontalExtend) {
			roundedX = centerPosition.getX(); 
		}
		if (verticalExtend) {
			roundedY = centerPosition.getY();  
		}
		/* performs loop arithmetic with error-fixed values p(new) = (p(old) + v) % (height/width)*/
		/* math.stackexchange.com/questions/2907303/finding-opposite-edge-wraparound-location-given-vector-and-location */
		float newX = roundedX + movementVelocity.getHorizontal(); 
		float newY = roundedY + movementVelocity.getVertical();  
		newX = (newX % App.SCREEN_WIDTH) + (newX < 0 ? App.SCREEN_WIDTH : 0);
		newY = (newY % App.SCREEN_HEIGHT) + (newY < 0 ? App.SCREEN_HEIGHT : 0);
		/* performs further error-adjustment that may have resulted in bugged modulo arithmetic 
		 * also respawns bus as close to out-of-bounds as possible to enforce a smooth animation
		 * */
		if (horizontalExtend) {
			newX = getClosestWall(newX);
			float widthPadding = getWidth()/2 - 1.5f;
			if (newX == 0) {
				newX -= widthPadding;
			} else {
				newX += widthPadding;
			}
		}
		if (verticalExtend) {
			float heightPadding = getHeight()/2 - 1.5f;
			newY = getClosestCeiling(newY);
			if (newY == 0) {
				newY -= heightPadding;
			} else {
				newY += heightPadding;
			}
		}
		/* returns, logs, and sets the new position */
		Position newPos = new Position(newX, newY);
		System.out.format("[Respawn] Respawning at %s\n", newPos);
		setLocation(newPos);
	}
	
	
	/* (non-Javadoc)
	 * @see TimeSupport#onTimeTick(int)
	 */
	public void onTimeTick(int delta) {
		if (movementVelocity.getMagnitude() == 0) {
			return;
		} 
		if (outOfBounds()) {
			System.out.println("tick");
			respawn();
			return;
		}
		float xDelta = movementVelocity.getHorizontal()*delta;
		float yDelta = movementVelocity.getVertical()*delta;
		float newX = centerPosition.getX() + xDelta;
		float newY = centerPosition.getY() + yDelta;
		setLocation(new Position(newX, newY));
	}
}