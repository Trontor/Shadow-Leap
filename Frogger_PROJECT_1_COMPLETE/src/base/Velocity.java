package base;

public class Velocity {

	private final float horizontal;
	private final float vertical;
	
	public float getHorizontal() {
		return horizontal;
	}
	
	public float getVertical() {
		return vertical;
	}
	
	public float getXSign() {
		return Math.signum(horizontal);
	}
	
	public float getYSign() {
		return Math.signum(vertical);
	}
	
	public float getMagnitude() {
		return (float)Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical,2));	
	}
	
	public Velocity getOppositeUnitVector() {
		float magnitude = getMagnitude();
		return new Velocity(-1/magnitude * horizontal, -1/magnitude * vertical);
	}
	
	public Velocity(float xVelocity, float yVelocity) {
		horizontal = xVelocity;
		vertical = yVelocity;
	}

	@Override
	public String toString() {
		return String.format("(x => %.2f px/ms, y => %.2f px/ms)", horizontal, vertical);
		
	}
	
	
}
