import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.BoundingBox;

public class Sprite { 
	private BoundingBox hitBox = null;
	private Image image;
	private String spriteName;
	private World world;
	public World getWorld() {
		return world;
	} 

	private float x, y, height, width;

	protected BoundingBox getHitBox() {
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
	
	public float getRight() {
		return x + width/2;
	}

	public float getLeft() {
		return x - width/2;
	}
	
	public float getTop() {
		return y + width/2;
	}

	public float getBottom() {
		return y - width/2;
	}
	 
	public void setImageSource(String value) throws SlickException {
		image = new Image(value);
		height = image.getHeight();
		width = image.getWidth(); 
		//System.out.format("| Image Source =  %s, Dimensions = %.0fx%.0f| ", value, width, height);
	}
	
	public float getXPos() {
		return x + width/2;
	}
	
	public float getYPos() {
		return y + height/2;
	}
	
	protected void setLocation(float centerX, float centerY) { 
		x = centerX - width/2; 
		y = centerY - height/2;
		if (hitBox == null) {
			hitBox = new BoundingBox(image, x, y);
		} else {
			hitBox.setX(x);
			hitBox.setY(y);
		}
	}  
	
	public Sprite(World spawn_world, String Name, String imageSrc, float centerX, float centerY) throws SlickException { 
		this.world = spawn_world;
		spriteName = Name;
		setImageSource(imageSrc);
		setLocation(centerX, centerY);
	}
	
	public void update(Input input, int delta) {
				
	}
	
	public void render(Graphics g) { 
		g.drawImage(image, x, y);		
	} 
}
