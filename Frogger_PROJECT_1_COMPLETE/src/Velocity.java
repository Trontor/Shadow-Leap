public class Velocity {

	private float horizontal, vertical;
	
	public float getHorizontal() {
		return horizontal;
	}
	
	public float getVertical() {
		return vertical;
	}
	
	public float getMagnitude() {
		return (float)Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical,2));	
	}
	
	public Velocity getOppositeUnitVector() {
		float magnitude = getMagnitude(); 
		Velocity velocity = new Velocity(-1/magnitude * horizontal, -1/magnitude * vertical); 
		return velocity;
	}
	
	public Velocity(float xVelocity, float y_velocity) {
		horizontal = xVelocity;
		vertical = y_velocity;
	}
}
