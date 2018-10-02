package base;

/** Types of assets that can be rendered */
public enum AssetType {
  /** Represents a tile that cannot be moved onto */
  SOLID_TILE,
  /** Represents a tile that does not incur a penalty and can be safely moved onto */
  FRIENDLY_TILE,
  /** Represents a tile that the player incurs a penalty on interaction but stays static */
  PASSIVE_OBSTACLE,
  /** Represents a tile that the player incurs a penalty on interaction and moves at a velocity */
  MOVING_OBSTACLE,
  /** Represets a tile that the player can move along with */
  DRIVER_OBJECT
}
