package base;

import core.App;
import core.Level;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import utilities.BoundingBox;
import utilities.Position;

/**
 * The superclass of all rendered objects in the game (tiles, obstacles, drivers, players, e.t.c.)
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Sprite {

  /** The name of the Sprite */
  private final String spriteName;
  /** The level that the Sprite is a part of */
  private final Level level;
  /** The center position to spawn the sprite at */
  private Position centerPosition;
  /** The hitbox representing the bounds of the Sprite */
  private BoundingBox hitBox;
  /** The image representing the visual aspect of this Sprite */
  private Image image;
  /** The dimensions of the sprite */
  private float height, width;

  /**
   * Initialises a new Sprite object
   *
   * @param spawnLevel The level to spawn the sprite on
   * @param name The name of the Sprite
   * @param imageSrc The path to the image to represent the sprite
   * @param centerPos The location to spawn the Sprite at
   */
  public Sprite(Level spawnLevel, String name, String imageSrc, Position centerPos) {
    this.centerPosition = new Position(App.getScreenWidth() / 2f, App.getScreenHeight() / 2f);
    this.level = spawnLevel;
    spriteName = name;
    setImage(imageSrc);
    if (hitBox == null) {
      hitBox = new BoundingBox(image, centerPos);
    }
    setLocation(centerPos);
  }

  /**
   * Gets the current bounding box of the Sprite
   *
   * @return BoundingBox object of Sprite
   */
  public BoundingBox getHitBox() {
    return hitBox;
  }

  /**
   * Gets the image used for rendering this Sprite
   *
   * @return Image object
   */
  public Image getImage() {
    return image;
  }

  /**
   * Sets the image used for rendering this Sprite
   *
   * @param imageSource Path to the image to set
   */
  private void setImage(String imageSource) {
    try {
      image = new Image(imageSource);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    height = image.getHeight();
    width = image.getWidth();
  }

  /**
   * Sets the image used for rendering this Sprite
   *
   * @param image Image to set
   */
  public void setImage(Image image) {
    this.image = image;
  }

  /**
   * Gets the name describing the Sprite
   *
   * @return String representing the name of the Sprite
   */
  public String getSpriteName() {
    return spriteName;
  }

  /**
   * Gets the current level the Sprite is being rendered on
   *
   * @return Level object attached to the Sprite
   */
  public Level getLevel() {
    return level;
  }

  /**
   * Gets the (x, y) coordinates of the center of the Sprite
   *
   * @return Position object specifying the (x, y) coordinates of Sprite center
   */
  public Position getLocation() {
    return centerPosition;
  }

  /**
   * Change the location of the Sprite
   *
   * @param centerLoc The position to center the base.Sprite around
   */
  public void setLocation(Position centerLoc) {
    centerPosition = centerLoc;
    hitBox.setX(centerLoc.getX());
    hitBox.setY(centerLoc.getY());
    outOfBounds();
  }

  /**
   * Idenfies the (x,y) location of the bottom left pixel of the Sprite
   *
   * @return new Position class with pre-set (x,y) anchor location
   */
  public Position getLeftAnchor() {
    float anchorX = centerPosition.getX() - width / 2;
    float anchorY = centerPosition.getY() - height / 2;
    return new Position(anchorX, anchorY);
  }

  /**
   * Moves the sprite by the specified number of pixels in any direction
   *
   * @param deltaX The number of pixels to move in the x direction
   * @param deltaY The number of pixels to move in the y direction
   */
  public void setLocationDelta(float deltaX, float deltaY) {
    float newX = centerPosition.getX() + deltaX;
    float newY = centerPosition.getY() + deltaY;
    Position newPos = new Position(newX, newY);
    setLocation(newPos);
  }

  /**
   * Gets the absolute width of the Sprite (horizontal)
   *
   * @return floating point width of the Sprite
   */
  public float getWidth() {
    return width;
  }

  /**
   * Gets the absolute height of the Sprite (vertical)
   *
   * @return floating point height of the Sprite
   */
  public float getHeight() {
    return height;
  }

  /**
   * Determines whether the sprite has completely exceeded the bounds of the screen
   *
   * @return True if out of bounds, False if inside bounds
   */
  public boolean outOfBounds() {
    boolean tooHigh = hitBox.getBottom() > App.getScreenHeight();
    boolean tooLow = hitBox.getTop() < 0;
    boolean tooFarLeft = hitBox.getRight() < 0;
    boolean tooFarRight = hitBox.getLeft() > App.getScreenWidth();
    return tooHigh || tooLow || tooFarLeft || tooFarRight;
  }

  /**
   * Draws the Sprite
   *
   * @param g The Graphics object to render the base.Sprite on
   */
  public void render(Graphics g) {
    g.drawImage(image, getLeftAnchor().getX(), getLeftAnchor().getY());
  }

  /**
   * Determines whether two Sprites are of the same 'type'
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Sprite)) {
      return false;
    }
    return obj.toString().equals(this.toString());
  }

  /**
   * Returns the name of the Sprite as the String representation of the Sprite
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return spriteName;
  }
}
