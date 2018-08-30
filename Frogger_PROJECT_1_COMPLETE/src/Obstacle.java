import org.newdawn.slick.SlickException;

public class Obstacle extends Sprite implements TimeSupport, CollisionDetection {
 
	private Velocity movement_velocity; 
	
	public Obstacle(World world, String Name, String imageSrc, float x, float y, Velocity speed_info) throws SlickException {
		super(world, Name, imageSrc, x, y);
		if (speed_info == null) {
			speed_info = new Velocity(0,0);
		}
		movement_velocity = speed_info;
	}

	@Override
	public void setLocation(float x, float y) {
		super.setLocation(x, y);
	} 
	 
	@Override
	public void onCollision(Sprite sprite) { 
		if (sprite != this)
			super.getWorld().ChangeGameState(WorldState.Death);
	}

	public void respawn() {   
		Velocity other_direction = movement_velocity.getOppositeUnitVector(); 
		float new_x = getXPos() + other_direction.getHorizontal()*(App.SCREEN_WIDTH + super.getWidth());
		float new_y = getYPos() + other_direction.getVertical()*(App.SCREEN_HEIGHT + super.getHeight()); 
		setLocation(new_x, new_y);
	}
	
	@Override
	public void OnTimeTick(int delta) {
		if (movement_velocity.getMagnitude() == 0) {
			return;
		} 
		/* sprite is too high */
		boolean too_high = super.getBottom() > App.SCREEN_HEIGHT;
		boolean too_low = super.getTop() < 0;
		boolean too_far_left = super.getRight() < 0;
		boolean too_far_right = super.getLeft() > App.SCREEN_WIDTH;
		if (too_high || too_low || too_far_left || too_far_right) {
			respawn();
			return;
		}
		float xpos_delta = movement_velocity.getHorizontal()*delta;
		float ypos_delta = movement_velocity.getVertical()*delta;
		float new_x = this.getXPos() + xpos_delta;
		float new_y = this.getYPos() + ypos_delta;
		setLocation(new_x, new_y);
	}
}
