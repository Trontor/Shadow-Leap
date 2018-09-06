package utilities;
public class Position {
	private float x, y;

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Position() {
		x = y = 0;
	}
	
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
