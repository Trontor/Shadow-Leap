package base;

import javafx.geometry.Pos;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import utilities.BoundingBox;
import utilities.Position;
import core.*;

/**
 * The superclass of all rendered objects in the game (tiles, obstacles, drivers, players, e.t.c.)
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Sprite {
  private final String spriteName;
  private final World world;
  Position centerPosition;
  private BoundingBox hitBox;
  private Image image;
  private float height, width;

  /**
   * Initialises a new base.Sprite object
   *
   * @param spawnWorld The world to spawn the sprite on
   * @param Name The name of the base.Sprite
   * @param imageSrc The path to the image to represent the sprite
   * @param centerPos The location to spawn the base.Sprite at
   */
  public Sprite(World spawnWorld, String Name, String imageSrc, Position centerPos) {
    this.centerPosition = new Position(App.SCREEN_WIDTH / 2f, App.SCREEN_HEIGHT / 2f);
    this.world = spawnWorld;
    spriteName = Name;
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
   * @param image Image to set
   */
  public void setImage(Image image) {
    this.image = image;
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
   * Gets the name describing the Sprite
   *
   * @return String representing the name of the Sprite
   */
  public String getSpriteName() {
    return spriteName;
  }

  /**
   * Gets the current world the Sprite is being rendered on
   *
   * @return World object attached to the Sprite
   */
  public World getWorld() {
    return world;
  }

  /**
   * Gets the (x, y) coordinates of the center of the Sprite
   *
   * @return Position object specifying the (x, y) coordinates of Sprite center
   */
  public Position getPosition() {
    return centerPosition;
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
   * Idenfies the (x,y) location of the bottom left pixel of the base.Sprite
   *
   * @return new Position class with pre-set (x,y) anchor location
   */
  public Position getLeftAnchor() {
    float anchorX = centerPosition.getX() - width / 2;
    float anchorY = centerPosition.getY() - height / 2;
    return new Position(anchorX, anchorY);
  }

  /**
   * Determines whether the sprite has completely exceeded the bounds of the screen
   *
   * @return True if out of bounds, False if inside bounds
   */
  public boolean outOfBounds() {
    boolean tooHigh = hitBox.getBottom() > App.SCREEN_HEIGHT;
    boolean tooLow = hitBox.getTop() < 0;
    boolean tooFarLeft = hitBox.getRight() < 0;
    boolean tooFarRight = hitBox.getLeft() > App.SCREEN_WIDTH;
    return tooHigh || tooLow || tooFarLeft || tooFarRight;
  }

  /**
   * Change the location of the base.Sprite
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
   * Draws the base.Sprite
   *
   * @param g The Graphics object to render the base.Sprite on
   */
  public void render(Graphics g) {
    g.drawImage(image, getLeftAnchor().getX(), getLeftAnchor().getY());
  }

  /* Determines whether two Sprites are of the same 'type'
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Sprite)) {
      return false;
    }
    return obj.toString().equals(this.toString());
  }

  /* Returns the name of the Sprite as the String representation of the Sprite
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return spriteName;
  }
}
