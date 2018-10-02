package base;

/** Intended to be implemented by Sprites that should be bounded by the screen */
public interface ScreenBoundable {
  /**
   * Event to be raised when the bounds of the screen has been extended by the object implementing
   */
  void onScreenBoundsExtended();
}
