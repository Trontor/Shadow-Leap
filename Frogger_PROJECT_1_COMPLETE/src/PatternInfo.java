
public class PatternInfo {
	private int y_location;
	private float separation; 
	private float offset;
	public int getYlocation() {
		return y_location;
	}
	public void setYlocation(int y_location) {
		this.y_location = y_location;
	}
	public float getSeparation() {
		return separation;
	}
	public void setSeparation(float separation) {
		this.separation = separation;
	}
	public float getOffset() {
		return offset;
	}
	public void setOffset(float offset) {
		this.offset = offset;
	}
	public PatternInfo(int y_location, float separation, float offset) {
		this.y_location = y_location;
		this.separation = separation;
		this.offset = offset;
	}	
}
