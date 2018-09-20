package base;

import core.App;
import core.Level;
import core.SpriteAssetManager;
import customsprites.PowerUp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.Position;

import java.util.List;
import java.util.logging.Logger;

/**
 * A Sprite that can be controlled by the user, is bound by the screen, can ride on Driver objects,
 * and can collide with objects that implement Collidable
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Player extends Sprite
    implements KeySupport, TimeSupport, CollisionDetection, ScreenBoundable, PassengerSupport {

  /** Represents the distance to separate the distance between each life representation */
  private static final float COUNTER_DISTANCE = 32;
  /** Image of the lives left icon */
  private static final String LIVES_IMAGE_NAME = "lives";
  /** Represents the location at which to render the life counter position */
  private static final Position LIFE_COUNTER_POS = new Position(24, 744);
  /** Image representing the */
  private static Image livesImage;
  /** Used for internal JVM logging */
  private final Logger log = Logger.getLogger(getClass().getSimpleName());
  /** Tracks the number of lives remaining for the player */
  private int lives = 3;
  /** The driver that the player is riding on - can be null */
  private Driver driver;

  public Player(Level spawnLevel, String imageSrc, Position centerPos) {
    super(spawnLevel, "base.Player", imageSrc, centerPos);
    try {
      livesImage = new Image(SpriteAssetManager.getAssetPath(LIVES_IMAGE_NAME));
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the number of lives left for the Player
   *
   * @return Integer value of number of lives left
   */
  public int getLives() {
    return lives;
  }

  /** Increments the number of lives left for the Player */
  public void addLife() {
    log.info("Lives left = " + lives);
    lives++;
  }

  /**
   * Decrements number of lives left for player
   *
   * @return true, if lives decremented sucessfully. false if no lives left
   */
  public boolean removeLife() {
    log.info("Lives left = " + lives);
    if (lives == 1) {
      return false;
    } else {
      lives--;
      return true;
    }
  }

  /**
   * Determines whether the Position given is out of the screen bounds
   *
   * @param futurePos The Position to check
   * @return True if the Position is out of bounds, else False
   */
  private boolean checkOutOfBounds(Position futurePos) {
    float offset = super.getWidth() / 2;
    float rightX = futurePos.getX() + offset;
    float leftX = futurePos.getX() - offset;
    float topY = futurePos.getY() + offset;
    float bottomY = futurePos.getY() - offset;
    float maxX = App.getScreenWidth();
    float maxY = App.getScreenHeight();
    return rightX > maxX || leftX < 0 || topY > maxY || bottomY < 0;
  }

  /**
   * Handles movement for player
   *
   * @see base.KeySupport#onKeyPress(int, char)
   */
  public void onKeyPress(int key, char c) {
    /* determine key press and set appropriate position offset */
    float newX = getLocation().getX();
    float newY = getLocation().getY();
    float delta = App.getTileLength();
    switch (key) {
      case Input.KEY_DOWN:
        newY += delta;
        break;
      case Input.KEY_UP:
        newY -= delta;
        break;
      case Input.KEY_LEFT:
        newX -= delta;
        break;
      case Input.KEY_RIGHT:
        newX += delta;
        break;
    }
    Position newPos = new Position(newX, newY);
    for (Sprite s : getLevel().getSpriteManager().getSpritesAt(newPos)) {
      if (getLevel().getSpriteManager().getAssetType(s.getSpriteName()) == AssetType.SOLID_TILE) {
        log.info(
            String.format("Intersection with %s which is a solid, can't move.", s.getSpriteName()));
        return;
      }
    }
    if (driver == null && checkOutOfBounds(newPos)) {
      return;
    }
    setLocation(newPos);
  }

  /**
   * Event raised when Player has collided with a Sprite object that implements Collidable Special
   * logic checking to account for if the Player is currently riding
   *
   * @param sprite The sprite to check
   */
  @Override
  public void onCollision(Sprite sprite) {
    /* signals player has died */
    if ((driver == null || !driver.isRideable()) && sprite instanceof Obstacle) {
      log.info("Collided with " + sprite.getSpriteName());
      getLevel().changeWorldState(LevelState.PlayerDeath);
    }
  }

  /** Checks if the screen's bounds have been extended */
  @Override
  public void onScreenBoundsExtended() {
    /* signals player has died */
    log.info("The player has exceeded screen bounds.");
    getLevel().changeWorldState(LevelState.PlayerDeath);
  }

  /**
   * Sets the location of the Player with bound checking
   *
   * @param center The location to center the Player at
   */
  @Override
  public void setLocation(Position center) {
    super.setLocation(center);
    if (super.checkOutOfBounds()) {
      onScreenBoundsExtended();
    }
  }

  /**
   * Determines if the player has collided with an object
   *
   * @see base.TimeSupport#onTimeTick(int)
   */
  @Override
  public void onTimeTick(int delta) {
    checkForDrivers();
    checkCollision();
    if (getLocation().getY() <= getLevel().getWinningY()) {
      getLevel().changeWorldState(LevelState.PartlyFinished);
    }
  }

  /** Handles collision detection is occuring */
  @Override
  public void checkCollision() {
    List<Sprite> collidableSprites =
        getLevel().getSpriteManager().getIntersectingSprites(this, s -> s instanceof Collidable);
    List<Sprite> powerUps =
        getLevel().getSpriteManager().getIntersectingSprites(this, s -> s instanceof PowerUp);
    for (Sprite powerUpSprite : powerUps) {
      ((PowerUp) powerUpSprite).applyPowerUp(this);
    }
    if (collidableSprites.size() > 0) {
      /* only care about colliding with first sprite, otherwise multiple deaths*/
      onCollision(collidableSprites.get(0));
    }
  }

  /**
   * Renders both the Player and its lives remaining
   *
   * @param g The Graphics object to render the base.Sprite on
   */
  @Override
  public void render(Graphics g) {
    super.render(g);
    if (livesImage == null) return;
    for (int i = 0; i < getLives(); i++) {
      float imageX = LIFE_COUNTER_POS.getX() + COUNTER_DISTANCE * i;
      float imageY = LIFE_COUNTER_POS.getY();
      imageX -= livesImage.getWidth() / 2f;
      imageY -= livesImage.getHeight() / 2f;
      g.drawImage(livesImage, imageX, imageY);
    }
  }

  /** Checks if the Sprite has encountered a potential Driver Sprite it can latch onto */
  @Override
  public void checkForDrivers() {
    List<Sprite> drivers =
        this.getLevel().getSpriteManager().getIntersectingSprites(this, s -> s instanceof Driver);
    if (drivers.size() == 0) {
      if (driver != null) {
        detachDriver();
      }
      return;
    }
    if (driver != null && !drivers.contains(driver)) {
      detachDriver();
    }
    Driver currentlyDriving = (Driver) drivers.get(0);
    if (driver != currentlyDriving) {
      detachDriver();
      attachDriver(currentlyDriving);
    }
  }

  /** Detaches the Player from the Driver */
  @Override
  public void detachDriver() {
    if (driver == null) return;
    log.info("Detached " + driver.getSpriteName() + " from " + this.getSpriteName());
    driver.removeRider(this);
    driver = null;
  }

  /**
   * Attaches the Player to a specified Driver
   *
   * @param driver The Driver to attach the Player to
   */
  @Override
  public void attachDriver(Driver driver) {
    log.info("Attached " + driver.getSpriteName() + " to " + this.getSpriteName());
    this.driver = driver;
    driver.addRider(this);
  }
}
