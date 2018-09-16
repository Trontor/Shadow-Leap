package base;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position;
import core.*;
import customsprites.PowerUp;

/**
 * A Sprite that can be controlled by the user, is bound by the screen, can ride on Driver objects,
 * and can collide with objects that implement Collidable
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Player extends Sprite
    implements KeySupport, TimeSupport, CollisionDetection, ScreenBoundable, Rideable {

  private final Position LIFE_COUNTER_POS = new Position(24, 744);
  private final float COUNTER_DISTANCE = 32;
  private Logger log = Logger.getLogger(getClass().getSimpleName());
  private int lives = 3;
  private Image livesImage;
  private Driver driver;

  public Player(World spawnWorld, String imageSrc, Position centerPos) {
    super(spawnWorld, "base.Player", imageSrc, centerPos);
    try {
      livesImage = new Image("assets/lives.png");
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
    float maxX = App.SCREEN_WIDTH;
    float maxY = App.SCREEN_HEIGHT;
    return rightX > maxX || leftX < 0 || topY > maxY || bottomY < 0;
  }

  /* Handles movement for player
   * @see base.KeySupport#onKeyPress(int, char)
   */
  public void onKeyPress(int key, char c) {
    /* determine key press and set appropriate position offset */
    float newX = centerPosition.getX();
    float newY = centerPosition.getY();
    float delta = App.TILE_SIZE;
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
    for (Sprite s : super.getWorld().spriteManager.getSpritesAt(newPos)) {
      if (getWorld().spriteManager.getAssetType(s.getSpriteName()) == AssetType.SOLID_TILE) {
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
  public void onCollision(Sprite sprite) {
    /* signals player has died */
    if ((driver == null || !driver.isRideable()) && sprite instanceof Obstacle) {
      log.info("Collided with " + sprite.getSpriteName());
      getWorld().changeWorldState(WorldState.Death);
    }
  }

  @Override
  public void onScreenBoundsExtended() {
    /* signals player has died */
    log.info("The player has exceeded screen bounds.");
    getWorld().changeWorldState(WorldState.Death);
  }

  @Override
  public void setLocation(Position center) {
    super.setLocation(center);
    if (super.outOfBounds()) {
      onScreenBoundsExtended();
    }
  }

  /* Determines if the player has collided with an object
   * @see base.TimeSupport#onTimeTick(int)
   */
  @Override
  public void onTimeTick(int delta) {
    checkForDrivers();
    checkCollision();
    if (centerPosition.getY() <= getWorld().WINNING_Y) {
      getWorld().changeWorldState(WorldState.PartlyFinished);
    }
  }

  @Override
  public void checkCollision() {
    List<Sprite> collidableSprites =
        getWorld().spriteManager.getIntersectingSprites(this, s -> s instanceof Collidable);
    List<Sprite> powerUps =
        getWorld().spriteManager.getIntersectingSprites(this, s -> s instanceof PowerUp);
    for (Sprite powerUpSprite : powerUps) {
      ((PowerUp) powerUpSprite).applyPowerUp(this);
    }
    if (collidableSprites.size() > 0) {
      /* only care about colliding with first sprite, otherwise multiple deaths*/
      onCollision(collidableSprites.get(0));
    }
  }

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

  @Override
  public void checkForDrivers() {
    List<Sprite> drivers =
        this.getWorld()
            .spriteManager
            .getIntersectingSprites(this)
            .stream()
            .filter(s -> s instanceof Driver)
            .collect(Collectors.toList());
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

  @Override
  public void detachDriver() {
    if (driver == null) return;
    log.info("Detached " + driver.getSpriteName() + " from " + this.getSpriteName());
    driver.removeRider(this);
    driver = null;
  }

  @Override
  public void attachDriver(Driver driver) {
    log.info("Attached " + driver.getSpriteName() + " to " + this.getSpriteName());
    this.driver = driver;
    driver.addRider(this);
  }
}
