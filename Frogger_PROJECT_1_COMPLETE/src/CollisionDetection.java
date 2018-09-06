/** Interface to be inherited by objects that perform an action on collision
 * @author Rohyl Joshi
 */
public interface CollisionDetection extends TimeSupport {	
	/** Raised when implementing class encounters a collision with a Sprite
	 * @param sprite The sprite collided with
	 */
	public void onCollision(Sprite sprite);
}
