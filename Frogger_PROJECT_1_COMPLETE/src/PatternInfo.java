
public class PatternInfo {
	private int yLocation;
	private float separation; 
	private float offset;
	public int getYlocation() {
		return yLocation;
	}
	public void setYlocation(int yLocation) {
		this.yLocation = yLocation;
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
	public PatternInfo(int yLocation, float separation, float offset) {
		this.yLocation = yLocation;
		this.separation = separation;
		this.offset = offset;
	}	
}
