package customsprites;

import java.util.logging.Logger;

import base.Driver;
import base.Player;
import base.Rideable;
import base.Sprite;
import base.Collidable;
import base.TimeSupport;
import core.App;
import core.World;
import utilities.Position;

public class PowerUp extends Sprite implements TimeSupport, Rideable, Collidable {

  /** Universal characteristics of the PowerUp */
  private final float MOVE_RATE = 2;

  private final float DEATH_TIME = 14;

  private Logger log = Logger.getLogger(getClass().getSimpleName());
  private Driver driver = null;
  private float deathTimer = 0;
  private float cumulativeDelta = 0;
  private boolean moveRight = false;

  public PowerUp(World spawnWorld, String Name, String imageSrc, Position centerPos) {
    super(spawnWorld, Name, imageSrc, centerPos);
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
    getWorld().spriteManager.removeSprite(this);
  }

  /** No functionality specified for driver detection */
  @Override
  public void checkForDrivers() {}

  /**
   * Detaches the rider from the Driver, if any
   */
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
   * @param delta Time passed since last frame (milliseconds).
   */
  @Override
  public void onTimeTick(int delta) {
    /* Increment timers */
    cumulativeDelta += delta;
    deathTimer += delta;
    if (deathTimer / 1000 > DEATH_TIME) {
      log.info("Deleted powerup. F F F F F");
      getWorld().spriteManager.removeSprite(this);
    }
    /* Checks if the Sprite must move */
    if (cumulativeDelta / 1000 <= MOVE_RATE) {
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
    cumulativeDelta = 0;
  }

  /**
   * Attempts to move the Power-Up one tile to the right
   * @return True if the Power-Up was succesfully moved to the right, else False
   */
  private boolean tryShuffleRight() {
    float newX = getPosition().getX() + App.TILE_SIZE;
    Position position = new Position(newX, driver.getPosition().getY());
    boolean canMove = driver.getHitBox().intersects(position);
    if (canMove) {
      this.setLocation(position);
      return true;
    }
    return false;
  }

  /**
   * Attempts to move the Power-Up one tile to the left
   * @return True if the Power-Up was succesfully moved to the left, else False
   */
  private boolean tryShuffleLeft() {
    float newX = getPosition().getX() - App.TILE_SIZE;
    Position position = new Position(newX, driver.getPosition().getY());
    boolean canMove = driver.getHitBox().intersects(position);
    if (canMove) {
      this.setLocation(position);
      return true;
    }
    return false;
  }
}
