import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException; 

public class Player extends Sprite implements KeySupport, CollisionDetection {

	/* Default tile size */
	private float TILE_SIZE = 48;
	public Player(World spawn_world, String imageSrc, float x, float y) throws SlickException {
		super(spawn_world, "Player", imageSrc, x, y);
		Image tile = new Image(imageSrc);
		TILE_SIZE = tile.getWidth(); 
	}   
	 
	@Override
	public void setLocation(float center_x, float center_y) { 
		float offset = TILE_SIZE/2;
		float right_x = center_x + offset;
		float left_x = center_x - offset;
		float top_y = center_y + offset;
		float bottom_y = center_y - offset; 
		float max_x = App.SCREEN_WIDTH;
		float max_y = App.SCREEN_HEIGHT;
		if (right_x > max_x || left_x < 0)
			return;
		if (top_y > max_y || bottom_y < 0)
			return;
		super.setLocation(center_x, center_y);
	} 

	
	@Override
	public void update(Input input, int delta) {  
		super.update(input, delta);
	}

	@Override
	public void onKeyPress(int key, char c) {
		float newX = getXPos(), newY = getYPos();
		switch(key) {
			case Input.KEY_DOWN:
				newY += TILE_SIZE;
				break;
			case Input.KEY_UP:
				newY -= TILE_SIZE;
				break;
			case Input.KEY_LEFT:
				newX -= TILE_SIZE;
				break;
			case Input.KEY_RIGHT:
				newX += TILE_SIZE;
				break;
		}
		setLocation(newX, newY);
	}	
}
