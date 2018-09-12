package customsprites;
import base.Driver;
import base.Rideable;
import base.Sprite;
import base.TimeSupport;
import base.Velocity;
import core.App;
import core.World;
import javafx.geometry.Pos;
import utilities.Position;

public class PowerUp extends Sprite implements TimeSupport, Rideable {

  private Driver driver = null;
  private float cumulativeDelta = 0;
  private final float MOVE_RATE = 2;
  private boolean moveRight= false;

  public PowerUp(World spawnWorld, String Name, String imageSrc,
      Position centerPos) {
    super(spawnWorld, Name, imageSrc, centerPos);
  }

  @Override
  public void checkForDrivers() {}

  @Override
  public void detachDriver() {
    if (this.driver != null){
      driver.removeRider();
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
    if (cumulativeDelta/1000 <= MOVE_RATE){
      return;
    }
    cumulativeDelta = 0;
    Position newPos = moveRight ? shuffleRight(): shuffleLeft();
    if (!driver.getHitBox().intersects(newPos)){
      moveRight = !moveRight;
      newPos = moveRight ? shuffleRight(): shuffleLeft();
      System.out.println("Recalibrated");
    }
    System.out.format("Old Pos = %s, New Pos = %s, Hitbox = %s\n", getPosition(), newPos, driver.getHitBox());
    setLocation(newPos);
  }
  private Position shuffleRight(){
    float newX = getPosition().getX() + 1 * App.TILE_SIZE;
    return new Position(newX, driver.getPosition().getY());
  }
  private Position shuffleLeft(){
    float newX = getPosition().getX() - 1 * App.TILE_SIZE;
    return new Position(newX, driver.getPosition().getY());
  }
}
