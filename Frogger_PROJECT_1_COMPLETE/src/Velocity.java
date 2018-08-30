

public class Velocity {
	private float horizontal;
	public float getHorizontal() {
		return horizontal;
	}
	public float vertical;
	public float getVertical() {
		return vertical;
	}
	
	public float getVerticalSign() {
		return (int) Math.signum(vertical);
	}

	public float getHorizontalSign() {
		return (int) Math.signum(horizontal);
	}
	
	public float getMagnitude() {
		return (float)Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical,2));
		
	}
	
	public Velocity getOppositeUnitVector() {
		float magnitude = getMagnitude(); 
		Velocity velocity = new Velocity(-1/magnitude * horizontal, -1/magnitude * vertical); 
		return velocity;
	}
	
	public Velocity(float x_velocity, float y_velocity) {
		horizontal = x_velocity;
		vertical = y_velocity;
	}
}
