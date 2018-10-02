package base;

/**
 * Interface to be inherited by objects that perform an action every millisecond
 *
 * @author Rohyl Joshi
 */
public interface TimeSupport {
  /**
   * Update the game state for a frame.
   *
   * @param delta Time passed since last frame (milliseconds).
   */
  void onTimeTick(int delta);
}
