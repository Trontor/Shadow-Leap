import java.util.List; 
import java.util.stream.Collectors;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position; 

public class Player extends Sprite implements KeySupport, CollisionDetection {
	
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
		getWorld().ChangeGameState(WorldState.Death);
	}
	
	/* Determines if the player has collided with an object
	 * @see TimeSupport#onTimeTick(int)
	 */
	public void onTimeTick(int delta) {
		List<Sprite> collidableObjects = getWorld().getSpriteMap().stream()
																  .filter(s->s instanceof Collidable)
																  .collect(Collectors.toList()); 
		for (Sprite sprite : collidableObjects) {
			if (getHitBox().intersects(sprite.getHitBox())) {
				onCollision(sprite);
			}
		}
	}
}
