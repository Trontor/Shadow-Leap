package base;

/**
 * Enumeration to describe the state of the game inside a core.Level
 *
 * @author Rohyl Joshi
 */
public enum LevelState {
  /** Signifies the level has been completed and you can move to the next one. */
  Finished,
  /** Signifies the player has lost a life */
  PlayerDeath,
  /** Signifies that the level has been partially completed (i.e. hole filled) */
  PartlyFinished
}
