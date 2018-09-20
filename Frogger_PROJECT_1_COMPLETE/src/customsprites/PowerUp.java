package customsprites;

import base.*;
import core.App;
import core.Level;
import utilities.Position;

import java.util.logging.Logger;

public class PowerUp extends Sprite implements TimeSupport, PassengerSupport, Collidable {

  /** The time in seconds between movements */
  private static final float MOVE_INTERVAL = 2;
  /** The time in seconds before the powerup expires */
  private static final float EXPIRY_TIME = 14;
  /** Used for internal JVM logging */
  private final Logger log = Logger.getLogger(getClass().getSimpleName());
  /** The driver that the PowerUp is riding on, can be null */
  private Driver driver = null;
  /** Time tracker for death */
  private float deathTimer = 0;
  /** Time tracker for death */
  private float moveTimer = 0;
  /** Flag whether the Sprite should move right, if False then it moves left */
  private boolean moveRight = false;

  /**
   * Initialises a PowerUp Sprite that can modify characteristics of other Sprites
   *
   * @param spawnLevel The level to spawn the PowerUp Sprite on
   * @param name The name of the PowerUp Sprite
   * @param imgSrc The path to the image to represent the PowerUp Sprite
   * @param centerPos The location at which to spawn the PowerUp Sprite
   */
  public PowerUp(Level spawnLevel, String name, String imgSrc, Position centerPos) {
    super(spawnLevel, name, imgSrc, centerPos);
  }

  /**
   * Applies the function of the Power-Up
   *
   * @param sprite Target Sprite to apply action to
   */
  public void applyPowerUp(Sprite sprite) {
    if (sprite instanceof Player) {
      ((Player) sprite).addLife();
    }
    getLevel().getSpriteManager().removeSprite(this);
  }

  /** No functionality specified for driver detection */
  @Override
  public void checkForDrivers() {}

  /** Detaches the rider from the Driver, if any */
  @Override
  public void detachDriver() {
    if (this.driver != null) {
      driver.removeRider(this);
    }
  }

  /** Attaches the Power-Up to a driver */
  @Override
  public void attachDriver(Driver driver) {
    this.driver = driver;
    driver.addRider(this);
  }

  /**
   * Handles the death and movement of the Sprite as per set characteristics
   *
   * @param delta Time passed since last frame (milliseconds).
   */
  @Override
  public void onTimeTick(int delta) {
    /* Increment timers */
    moveTimer += delta;
    deathTimer += delta;
    if (deathTimer / 1000 > EXPIRY_TIME) {
      log.info("Deleted powerup. F F F F F");
      getLevel().getSpriteManager().removeSprite(this);
    }
    /* Checks if the Sprite must move */
    if (moveTimer / 1000 <= MOVE_INTERVAL) {
      /* The Sprite does not need to move yet */
      return;
    }
    /* The Sprite must try to move either left or right */
    if (moveRight) {
      if (!tryShuffleRight()) {
        moveRight = false;
        tryShuffleLeft();
      }
    } else {
      if (!tryShuffleLeft()) {
        moveRight = true;
        tryShuffleRight();
      }
    }
    /* Resets timer */
    moveTimer = 0;
  }

  /**
   * Attempts to move the Power-Up one tile to the right
   *
   * @return True if the Power-Up was succesfully moved to the right, else False
   */
  private boolean tryShuffleRight() {
    float newX = getLocation().getX() + App.getTileLength();
    Position position = new Position(newX, driver.getLocation().getY());
    boolean canMove = driver.getHitBox().intersects(position, App.getTileLength(), App.getTileLength());
    if (canMove) {
      this.setLocation(position);
      return true;
    }
    return false;
  }

  /**
   * Attempts to move the Power-Up one tile to the left
   *
   * @return True if the Power-Up was succesfully moved to the left, else False
   */
  private boolean tryShuffleLeft() {
    float newX = getLocation().getX() - App.getTileLength();
    Position position = new Position(newX, driver.getLocation().getY());
    boolean canMove = driver.getHitBox().intersects(position, App.getTileLength(), App.getTileLength());
    if (canMove) {
      this.setLocation(position);
      return true;
    }
    return false;
  }
}
