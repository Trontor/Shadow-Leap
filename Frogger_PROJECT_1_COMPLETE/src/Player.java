import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException; 

public class Player extends Sprite implements KeySupport, CollisionDetection {
	
	public Player(World spawnWorld, String imageSrc, float x, float y) throws SlickException {
		super(spawnWorld, "Player", imageSrc, x, y);
	}   
	 
	@Override
	public void setLocation(float centerX, float centerY) { 
		float offset = super.getWidth()/2;
		float rightX = centerX + offset;
		float leftX = centerX - offset;
		float topY = centerY + offset;
		float bottomY = centerY - offset; 
		float maxX = App.SCREEN_WIDTH;
		float maxY = App.SCREEN_HEIGHT;
		if (rightX > maxX || leftX < 0)
			return;
		if (topY > maxY || bottomY < 0)
			return;
		super.setLocation(centerX, centerY);
	} 

	public void onKeyPress(int key, char c) {
		float newX = getXPos(), newY = getYPos();
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
		setLocation(newX, newY);
	}

	public void onCollision(Sprite sprite) {
		getWorld().ChangeGameState(WorldState.Death);
	}

	public void onTimeTick(int delta) {
		for (Sprite sprite : getWorld().getSpriteMap()) {
			if (sprite instanceof Collidable && getHitBox().intersects(sprite.getHitBox())) {
				onCollision(sprite);
			}
		}
	}
}
