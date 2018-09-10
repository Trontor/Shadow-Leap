import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;
import utilities.Position;

public class Sprite { 
	
	/* specifies the hit box and visual bounds for the object */
	private BoundingBox hitBox = null;
	public BoundingBox getHitBox() {
		return hitBox;
	} 
	
	
	/* image to render that represents the object visually */
	private Image image;	
	public void setImage(String imageSource) throws SlickException {
		image = new Image(imageSource);
		height = image.getHeight();
		width = image.getWidth();  
	}
	
	/* descriptive name to identify the object, used for equality */
	private String spriteName;
	public String getSpriteName() {
		return spriteName;
	}
	
	/* current world the player is on*/
	private World world;  
	public World getWorld() {
		return world;
	} 
	
	/* specifies the (x,y) location of the center of the Sprite */
	public Position centerPosition;
	public Position getPosition() {
		return centerPosition;
	}

	/* details the height and width of the Sprite based on its Image*/
	private float height, width;
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	} 
	 
	/** Idenfies the (x,y) location of the bottom left pixel of the Sprite
	 * @return new Position class with pre-set (x,y) anchor location
	 */
	public Position getLeftAnchor() {
		float anchorX = centerPosition.getX() - width/2;
		float anchorY = centerPosition.getY() - height/2;
		return new Position(anchorX, anchorY);
	}
	
	/** Determines whether the sprite has completely exceeded the bounds of the screen 
	 * @return True if out of bounds, False if inside bounds
	 */
	public boolean outOfBounds() { 
		boolean tooHigh = hitBox.getBottom() > App.SCREEN_HEIGHT;
		boolean tooLow = hitBox.getTop() <  0;
		boolean tooFarLeft = hitBox.getRight() < 0;
		boolean tooFarRight = hitBox.getLeft() > App.SCREEN_WIDTH; 
		return tooHigh || tooLow || tooFarLeft || tooFarRight;
	}	
	
	/** Change the location of the Sprite
	 * @param centerLoc The position to center the Sprite around
	 */
	public void setLocation(Position centerLoc) { 
		centerPosition = centerLoc;
		if (hitBox == null) {
			hitBox = new BoundingBox(image, centerPosition);
		} 
		hitBox.setX(centerLoc.getX());
		hitBox.setY(centerLoc.getY());
		if (outOfBounds()) {
			//System.out.format("[OutOfBounds] %s at %s\n", spriteName, centerLoc);
		}
	}  
	
	public void setLocationDelta(float deltaX, float deltaY) { 
		float newX = centerPosition.getX() + deltaX;
		float newY = centerPosition.getY() + deltaY;
		Position newPos = new Position(newX, newY);
		setLocation(newPos);
	}  
	
	/** Initialises a new Sprite object
	 * @param spawn_world The world to spawn the sprite on
	 * @param Name The name of the Sprite
	 * @param imageSrc The path to the image to represent the sprite
	 * @param centerPos The location to spawn the Sprite at
	 * @throws SlickException Slick Libary Error
	 */
	public Sprite(World spawnWorld, String Name, String imageSrc, Position centerPos) throws SlickException { 
		this.world = spawnWorld;
		spriteName = Name;
		setImage(imageSrc);
		setLocation(centerPos);
	}
	
	/** Draws the Sprite
	 * @param g The Graphics object to render the Sprite on
	 */
	public void render(Graphics g) { 
		g.drawImage(image, getLeftAnchor().getX(), getLeftAnchor().getY());		
	}

	/* Determines whether two Sprites are of the same 'type'
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Sprite)) {
			return false;
		}
		return obj.toString().equals(this.toString());
	}

	/* Returns the name of the Sprite as the String representation of the Sprite 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return spriteName;
	} 
	
	
}
