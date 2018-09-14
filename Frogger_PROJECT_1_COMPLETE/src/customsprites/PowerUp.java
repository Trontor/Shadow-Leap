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

  public void applyPowerUp(Sprite sprite) {
    if (sprite instanceof Player) {
      ((Player) sprite).addLife();
    }
    getWorld().spriteManager.removeSprite(this);
  }

  @Override
  public void checkForDrivers() {}

  @Override
  public void detachDriver() {
    if (this.driver != null) {
      driver.removeRider(this);
    }
  }

  @Override
  public void attachDriver(Driver driver) {
    this.driver = driver;
    driver.addRider(this);
  }

  @Override
  public void onTimeTick(int delta) {
    cumulativeDelta += delta;
    deathTimer += delta;
    if (deathTimer / 1000 > DEATH_TIME) {
      log.info("Deleted powerup. F F F F F");
      getWorld().spriteManager.removeSprite(this);
    }
    if (cumulativeDelta / 1000 <= MOVE_RATE) {
      return;
    }
    cumulativeDelta = 0;
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
  }

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
