package base;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import core.App;
import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.Sys;
import utilities.Position;
import core.World;

public class Driver extends MovingSprite {

	private Sprite ridingSprite;
  private boolean rideable = true;

  public boolean isRideable() {
    return rideable;
  }

  public void setRideable(boolean rideable) {
    this.rideable = rideable;
  }

  public Driver(World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
		super(spawnWorld, Name, imageSrc, centerPos, velocity); 
		ridingSprite = null;
	}

	@Override
	public void onTimeTick(int delta) {
    if (ridingSprite != null){
      float deltaX = super.getMovementVelocity().getHorizontal() * delta;
      float deltaY = super.getMovementVelocity().getVertical() * delta;
      ridingSprite.setLocationDelta(deltaX, deltaY);
    }
		super.onTimeTick(delta);
	}

	private void snapRider(){
    if (ridingSprite == null){
      return;
    }
    float currX = ridingSprite.getPosition().getX();
    float currY = ridingSprite.getPosition().getY();
    float snapX = roundUpToNearestMultiple((int)currX, App.TILE_SIZE/2);
    ridingSprite.setLocation(new Position(snapX, currY));
  }

  private int roundUpToNearestMultiple(int numToRound, int multiple){
    if (multiple == 0)
      return numToRound;
    int remainder = numToRound % multiple;
    if (remainder == 0)
      return numToRound;
    return numToRound + multiple - remainder;
  }

	public void addRider(Rideable rider){
    if (!(rider instanceof Sprite)){
      System.out.println("Cannot add rider because it is not of type Sprite");
      return;
    }
    ridingSprite = (Sprite)rider;
    snapRider();
  }
  public void removeRider(){
    if (ridingSprite != null){
      ridingSprite = null;
    }
  }

}
