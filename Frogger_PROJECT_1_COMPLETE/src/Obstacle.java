import org.newdawn.slick.SlickException;

import utilities.BoundingBox;
import utilities.Position;

public class Obstacle extends Sprite implements TimeSupport, Collidable {
 
	private Velocity movementVelocity; 
	
	public Obstacle(World world, String Name, String imageSrc, Position centerPos, Velocity speedInfo) throws SlickException {
		super(world, Name, imageSrc, centerPos);
		if (speedInfo == null) {
			speedInfo = new Velocity(0,0);
		}
		movementVelocity = speedInfo;
	}

	public void respawn() {   
		Velocity otherDirection = movementVelocity.getOppositeUnitVector(); 
		float newX = centerPosition.getX() + otherDirection.getHorizontal() * (App.SCREEN_WIDTH + super.getWidth());
		float newY = centerPosition.getY() + otherDirection.getVertical() * (App.SCREEN_HEIGHT + super.getHeight()); 
		setLocation(new Position(newX, newY));
	}
	
	private boolean outOfBounds() {
		BoundingBox box = getHitBox();
		boolean tooHigh = box.getBottom() > App.SCREEN_HEIGHT;
		boolean tooLow = box.getTop() <  0;
		boolean tooFarLeft = box.getRight() < 0;
		boolean tooFarRight = box.getLeft() > App.SCREEN_WIDTH;
		return tooHigh || tooLow || tooFarLeft || tooFarRight;
	}
	
	public void onTimeTick(int delta) {
		if (movementVelocity.getMagnitude() == 0) {
			return;
		} 
		if (outOfBounds()) {
			respawn();
			return;
		}
		float xDelta = movementVelocity.getHorizontal()*delta;
		float yDelta = movementVelocity.getVertical()*delta;
		float newX = centerPosition.getX() + xDelta;
		float newY = centerPosition.getY() + yDelta;
		setLocation(new Position(newX, newY));
	}
}