import org.newdawn.slick.SlickException;

public class Obstacle extends Sprite implements TimeSupport, Collidable {
 
	private Velocity movementVelocity; 
	
	public Obstacle(World world, String Name, String imageSrc, float x, float y, Velocity speedInfo) throws SlickException {
		super(world, Name, imageSrc, x, y);
		if (speedInfo == null) {
			speedInfo = new Velocity(0,0);
		}
		movementVelocity = speedInfo;
	}

	@Override
	public void setLocation(float x, float y) {
		super.setLocation(x, y);
	} 

	public void respawn() {   
		Velocity otherDirection = movementVelocity.getOppositeUnitVector(); 
		float newX = getXPos() + otherDirection.getHorizontal() * (App.SCREEN_WIDTH + super.getWidth());
		float newY = getYPos() + otherDirection.getVertical() * (App.SCREEN_HEIGHT + super.getHeight()); 
		setLocation(newX, newY);
	}
	
	private boolean outOfBounds() {
		boolean tooHigh = getBottom() > App.SCREEN_HEIGHT;
		boolean tooLow = getTop() < 0;
		boolean tooFarLeft = getRight() < 0;
		boolean tooFarRight = getLeft() > App.SCREEN_WIDTH;
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
		float newX = getXPos() + xDelta;
		float newY = getYPos() + yDelta;
		setLocation(newX, newY);
	}
}