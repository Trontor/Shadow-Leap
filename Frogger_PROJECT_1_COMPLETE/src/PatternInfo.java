/** Stores information about collections of objects that are repeated
 * @author Rohyl Joshi
 */
public class PatternInfo {
	
	private int yLocation;
	public int getYlocation() {
		return yLocation;
	}
	public void setYlocation(int yLocation) {
		this.yLocation = yLocation;
	}
	
	private float separation; 
	public float getSeparation() {
		return separation;
	}
	public void setSeparation(float separation) {
		this.separation = separation;
	}

	private float offset;
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
