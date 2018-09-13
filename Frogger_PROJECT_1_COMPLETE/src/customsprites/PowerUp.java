package customsprites;
import java.util.PrimitiveIterator.OfDouble;

import org.lwjgl.Sys;
import org.omg.CosNaming._BindingIteratorImplBase;

import base.Driver;
import base.Player;
import base.Rideable;
import base.Sprite;
import base.Collidable;
import base.TimeSupport;
import base.Velocity;
import core.App;
import core.World; 
import utilities.Position;

public class PowerUp extends Sprite implements TimeSupport, Rideable, Collidable {

  private Driver driver = null;
  private float cumulativeDelta = 0;
  private final float MOVE_RATE = 2;
  private boolean moveRight= false;

  public PowerUp(World spawnWorld, String Name, String imageSrc,
      Position centerPos) {
    super(spawnWorld, Name, imageSrc, centerPos);
  }

  public void applyPowerUp(Sprite sprite) {
	  if (sprite instanceof Player) {
		  ((Player)sprite).addLife();
	  }
	  getWorld().removeSprite(this);
  }
  @Override
  public void checkForDrivers() {}

  @Override
  public void detachDriver() {
    if (this.driver != null){
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
    if (cumulativeDelta/1000 <= MOVE_RATE){
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
  
  private boolean tryShuffleRight(){
    float newX = getPosition().getX() + 1 * App.TILE_SIZE;
    Position position = new Position(newX, driver.getPosition().getY());
    boolean canMove = driver.getHitBox().intersects(position);
    if (canMove) { 
    	this.setLocation(position);
    	return true;
    } 
	return false; 
  }
  
  private boolean tryShuffleLeft(){
	  float newX = getPosition().getX() - 1 * App.TILE_SIZE;
	    Position position = new Position(newX, driver.getPosition().getY());
	    boolean canMove = driver.getHitBox().intersects(position);
	    if (canMove) {
	    	this.setLocation(position);
	    	return true;
	    } 
		return false; 
  }
}
