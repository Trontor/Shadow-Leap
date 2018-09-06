import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;
import utilities.Position;

public class Sprite { 
	private BoundingBox hitBox = null;
	private Image image;
	private String spriteName;
	private World world;
	public Position getClosestWall;
	public Position centerPosition;
	
	public World getWorld() {
		return world;
	} 

	private float height, width;

	public Position getPosition() {
		return centerPosition;
	}
	
	public Position getLeftAnchor() {
		float anchorX = centerPosition.getX() - width/2;
		float anchorY = centerPosition.getY() - height/2;
		return new Position(anchorX, anchorY);
	}
	
	public BoundingBox getHitBox() {
		return hitBox;
	} 
	
	public String getSpriteName() {
		return spriteName;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	} 
	 
	public void setImageSource(String value) throws SlickException {
		image = new Image(value);
		height = image.getHeight();
		width = image.getWidth();  
	}
	
	public boolean outOfBounds() { 
		boolean tooHigh = hitBox.getBottom() > App.SCREEN_HEIGHT;
		boolean tooLow = hitBox.getTop() <  0;
		boolean tooFarLeft = hitBox.getRight() < 0;
		boolean tooFarRight = hitBox.getLeft() > App.SCREEN_WIDTH; 
		return tooHigh || tooLow || tooFarLeft || tooFarRight;
	}	
	
	protected void setLocation(Position centerLoc) { 
		centerPosition = centerLoc;
		if (hitBox == null) {
			hitBox = new BoundingBox(image, centerPosition);
		} 
		hitBox.setX(centerLoc.getX());
		hitBox.setY(centerLoc.getY());
		if (outOfBounds()) {
			System.out.format("[OutOfBounds] %s at %s\n", spriteName, centerLoc);
		}
	}  
	
	public Sprite(World spawn_world, String Name, String imageSrc, Position centerPos) throws SlickException { 
		this.world = spawn_world;
		spriteName = Name;
		setImageSource(imageSrc);
		setLocation(centerPos);
	}
	
	public void render(Graphics g) { 
		g.drawImage(image, getLeftAnchor().getX(), getLeftAnchor().getY());		
	} 
}
