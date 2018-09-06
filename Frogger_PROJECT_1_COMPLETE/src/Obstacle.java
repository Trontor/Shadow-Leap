import javax.crypto.spec.GCMParameterSpec;

import org.lwjgl.Sys;
import org.newdawn.slick.SlickException;
import utilities.Position;

public class Obstacle extends Sprite implements TimeSupport, Collidable {
 
	private Velocity movementVelocity; 
	
	public Obstacle(World world, String obstacleName, String imageSrc, Position centerPos, Velocity speedInfo) throws SlickException {
		super(world, obstacleName, imageSrc, centerPos);
		if (speedInfo == null) {
			speedInfo = new Velocity(0,0);
		}
		movementVelocity = speedInfo;
	}

	private float getClosestWall(float xVal) {
		float leftDiff = Math.abs(xVal - 0);
		float rightDiff = Math.abs(xVal - App.SCREEN_WIDTH);
		return leftDiff < rightDiff ? 0: App.SCREEN_WIDTH;
	}
	
	private float getClosestCeiling(float yVal) {
		float bottomDiff = Math.abs(yVal - 0);
		float topDiff = Math.abs(yVal - App.SCREEN_HEIGHT);
		return bottomDiff < topDiff  ? 0: App.SCREEN_HEIGHT;
	}

	public void respawn() {
		Velocity otherDirection = movementVelocity.getOppositeUnitVector();
		float roundedX = centerPosition.getX();
		float roundedY = centerPosition.getY();
		boolean horizontalExtend = roundedX < 0 || roundedX > App.SCREEN_WIDTH;
		boolean verticalExtend = roundedY < 0 || roundedY > App.SCREEN_HEIGHT;
		if (horizontalExtend) {
			roundedX = getClosestWall(centerPosition.getX()); 
		}
		if (verticalExtend) {
			roundedY = getClosestWall(centerPosition.getY());  
		}
		float newX = roundedX + movementVelocity.getHorizontal(); 
		float newY = roundedY + movementVelocity.getVertical();  
		newX = (newX % App.SCREEN_WIDTH) + (newX < 0 ? App.SCREEN_WIDTH : 0);
		newY = (newY % App.SCREEN_HEIGHT) + (newY < 0 ? App.SCREEN_HEIGHT : 0);
		if (horizontalExtend)
			newX = getClosestWall(newX);
		if (verticalExtend)
			newY = getClosestWall(newY);
		Position newPos = new Position(newX, newY);
		System.out.format("[Respawn] Respawning at %s\n", newPos);
		setLocation(newPos);
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