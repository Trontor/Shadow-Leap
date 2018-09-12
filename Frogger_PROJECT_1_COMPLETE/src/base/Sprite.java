package base;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import utilities.BoundingBox;
import utilities.Position;
import core.*;

public class Sprite {
	/* specifies the hit box and visual bounds for the object */
	private BoundingBox hitBox = null;
	public BoundingBox getHitBox() {
		return hitBox;
	} 
	
	
	/* image to render that represents the object visually */
	private Image image;
	public Image getImage(){
	  return image;
  }
	private void setImage(String imageSource) {
		try {
			image = new Image(imageSource);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		height = image.getHeight();
		width = image.getWidth();  
	}
	
	/* descriptive name to identify the object, used for equality */
	private final String spriteName;
  public String getSpriteName() {
		return spriteName;
	}
	
	/* current world the player is on*/
	private final World world;
	public World getWorld() {
		return world;
	} 
	
	/* specifies the (x,y) location of the center of the base.Sprite */
    Position centerPosition;
	public Position getPosition() {
		return centerPosition;
	}

	/* details the height and width of the base.Sprite based on its Image*/
	private float height, width;
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	} 
	 
	/** Idenfies the (x,y) location of the bottom left pixel of the base.Sprite
	 * @return new Position class with pre-set (x,y) anchor location
	 */
	private Position getLeftAnchor() {
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
	
	/** Change the location of the base.Sprite
	 * @param centerLoc The position to center the base.Sprite around
	 */
  public void setLocation(Position centerLoc) {
		centerPosition = centerLoc; 
		hitBox.setX(centerLoc.getX());
		hitBox.setY(centerLoc.getY());
		if (outOfBounds()) {
			//System.out.format("[OutOfBounds] %s at %s\n", spriteName, centerLoc);
		}
	}  
	
	void setLocationDelta(float deltaX, float deltaY) {
		float newX = centerPosition.getX() + deltaX;
		float newY = centerPosition.getY() + deltaY;
		Position newPos = new Position(newX, newY);
		setLocation(newPos);
	}  
	
	/** Initialises a new base.Sprite object
	 * @param spawnWorld The world to spawn the sprite on
	 * @param Name The name of the base.Sprite
	 * @param imageSrc The path to the image to represent the sprite
	 * @param centerPos The location to spawn the base.Sprite at
	 */
	public Sprite(World spawnWorld, String Name, String imageSrc, Position centerPos) {
		this.centerPosition = new Position(App.SCREEN_WIDTH/2, App.SCREEN_HEIGHT/2);
		this.world = spawnWorld;
		spriteName = Name;		 
		setImage(imageSrc); 
		if (hitBox == null) {
			hitBox = new BoundingBox(image, centerPos);
		} 
		setLocation(centerPos);
	}
	
	/** Draws the base.Sprite
	 * @param g The Graphics object to render the base.Sprite on
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

	/* Returns the name of the base.Sprite as the String representation of the base.Sprite
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return spriteName;
	} 
	
	
}
