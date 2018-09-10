import java.util.List; 
import java.util.stream.Collectors;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position; 

public class Player extends Sprite implements KeySupport, CollisionDetection {
	
	private int LIVES;
	private Rideable currentlyRiding;
	public Player(World spawnWorld, String imageSrc, Position centerPos) throws SlickException {
		super(spawnWorld, "Player", imageSrc, centerPos);
	}   
	 
	/* Ensures that the player stays within the screen bounds 
	 * @see Sprite#setLocation(utilities.Position)
	 */
	@Override
	public void setLocation(Position center) { 
		float offset = super.getWidth()/2;
		float rightX = center.getX() + offset;
		float leftX = center.getX() - offset;
		float topY = center.getY() + offset;
		float bottomY = center.getY() - offset; 
		float maxX = App.SCREEN_WIDTH;
		float maxY = App.SCREEN_HEIGHT;
		if (rightX > maxX || leftX < 0 || topY > maxY || bottomY < 0)
			return;
		super.setLocation(center);
	} 

	/* Handles movement for player
	 * @see KeySupport#onKeyPress(int, char)
	 */
	public void onKeyPress(int key, char c) {
		/* determine key press and set appropriate position offset */
		float newX = centerPosition.getX();
		float newY = centerPosition.getY();
		float delta = super.getWidth();
		switch(key) {
			case Input.KEY_DOWN:
				newY += delta;
				break;
			case Input.KEY_UP:
				newY -= delta;
				break;
			case Input.KEY_LEFT:
				newX -= delta;
				break;
			case Input.KEY_RIGHT:
				newX += delta;
				break;
		}
		setLocation(new Position(newX, newY));
	}

	/* (non-Javadoc)
	 * @see CollisionDetection#onCollision(Sprite)
	 */
	public void onCollision(Sprite sprite) {
		/* signals player has died */
		getWorld().ChangeWorldState(WorldState.Death);
	}
	
	/* Determines if the player has collided with an object
	 * @see TimeSupport#onTimeTick(int)
	 */
	public void onTimeTick(int delta) { 
		List<Sprite> intersectingSprites = getWorld().getSpriteMap().stream()
				  .filter(s-> s != this && s.getHitBox().intersects(this.getHitBox()))
				  .collect(Collectors.toList()); 
		
		List<Sprite> rideableSprites = intersectingSprites.stream()
				  .filter(s-> s instanceof Rideable)
				  .collect(Collectors.toList()); 
		if (rideableSprites.size() > 0) {
			/* attach Sprite to first rideable object */
			Rideable driver = (Rideable)rideableSprites.get(0);
			if (currentlyRiding != null && currentlyRiding != driver) {
				RemoveDrivers();
			}
			currentlyRiding = driver;
			driver.OnTouch(this);
			return;
		} else if (currentlyRiding != null) {
			/* remove Sprite from object */
			RemoveDrivers();
		} 

		List<Sprite> collidableSprites = intersectingSprites.stream()
				  .filter(s-> s instanceof Collidable)
				  .collect(Collectors.toList()); 
		if (collidableSprites.size() > 0) {
			/* We are colliding */
			onCollision(collidableSprites.get(0));
		}
	}
	private void RemoveDrivers() { 
		currentlyRiding.RemoveRider(this); 
		currentlyRiding = null;
	}
}
