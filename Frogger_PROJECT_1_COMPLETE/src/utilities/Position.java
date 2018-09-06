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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Position))
			return false;
		Position p = ((Position)obj);
		return p.getX() == x && p.getY() == y;
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
	
	
}
