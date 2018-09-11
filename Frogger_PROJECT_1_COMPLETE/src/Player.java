import java.util.List; 
import java.util.stream.Collectors;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position; 

public class Player extends Sprite implements KeySupport, TimeSupport, CollisionDetection, Boundable {
	
	private int LIVES;
	private Rideable currentlyRiding;
	public Player(World spawnWorld, String imageSrc, Position centerPos) throws SlickException {
		super(spawnWorld, "Player", imageSrc, centerPos);
	}   
	 
	private boolean FutureOutOfBounds(Position futurePos) {
		float offset = super.getWidth()/2;
		float rightX = futurePos.getX() + offset;
		float leftX = futurePos.getX() - offset;
		float topY = futurePos.getY() + offset;
		float bottomY = futurePos.getY() - offset; 
		float maxX = App.SCREEN_WIDTH;
		float maxY = App.SCREEN_HEIGHT;
		return rightX > maxX || leftX < 0 || topY > maxY || bottomY < 0;
	}
	@Override
	public void setLocation(Position center) { 
		if (currentlyRiding == null && FutureOutOfBounds(center)) {
			return;
		} else if (super.outOfBounds()) { 
			OnBoundsExtended();
		}
		if (center.getY() <= getWorld().FINISH_HEIGHT) {
			getWorld().ChangeWorldState(WorldState.PartlyFinished);
		}
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
		Position newPos = new Position(newX, newY); 
		for (Sprite s : super.getWorld().getSpritesAt(newPos)) {
			if (getWorld().getAssetType(s.getSpriteName()) == AssetType.SolidTile) {
				System.out.println("Intersection with solid, can't move.");
				return;
			}
		}
		setLocation(newPos);
	}

	@Override
	public void OnBoundsExtended() {
		/* signals player has died */
		getWorld().ChangeWorldState(WorldState.Death);
	}
	
	/* (non-Javadoc)
	 * @see CollisionDetection#onCollision(Sprite)
	 */
	public void OnCollision(Sprite sprite) {
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
			} else if (currentlyRiding != driver) {
				System.out.println("Attached new driver");
				currentlyRiding = driver;
				driver.OnTouch(this);
			}
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
			OnCollision(collidableSprites.get(0));
		}
	}
	private void RemoveDrivers() { 
		System.out.println("Detached driver");
		currentlyRiding.RemoveRider(this); 
		currentlyRiding = null;
	}
}
