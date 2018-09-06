import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position; 

public class Player extends Sprite implements KeySupport, CollisionDetection {
	
	public Player(World spawnWorld, String imageSrc, Position centerPos) throws SlickException {
		super(spawnWorld, "Player", imageSrc, centerPos);
	}   
	 
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

	public void onKeyPress(int key, char c) {
		float newX = centerPosition.getX(), newY = centerPosition.getY();
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
