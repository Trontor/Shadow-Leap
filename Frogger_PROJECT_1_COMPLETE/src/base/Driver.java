package base;

import core.App;

import java.nio.file.attribute.FileOwnerAttributeView;
import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;
import org.lwjgl.Sys;
import org.newdawn.slick.TrueTypeFont;

import utilities.Position;
import core.World;

public class Driver extends MovingSprite {

	private List<Sprite> ridingSprites;
  private boolean rideable = true;

  public boolean isRideable() {
    return rideable;
  }

  public void setRideable(boolean rideable) {
    this.rideable = rideable;
  }

  public Driver(World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
		super(spawnWorld, Name, imageSrc, centerPos, velocity); 
		ridingSprites = new ArrayList<>();
	}

	@Override
	public void onTimeTick(int delta) {
    if (ridingSprites.size() > 0){
      float deltaX = super.getMovementVelocity().getHorizontal() * delta;
      float deltaY = super.getMovementVelocity().getVertical() * delta;
      for (int i = 0; i < ridingSprites.size(); i++) {
    	  Sprite sprite = ridingSprites.get(i);
          sprite.setLocationDelta(deltaX, deltaY); 
      }
	}
	super.onTimeTick(delta);
	}

	private void snapRider(Rideable rider){ 
    Sprite ridingSprite = (Sprite)rider;
    float currX = ridingSprite.getPosition().getX();
    float currY = ridingSprite.getPosition().getY();
    float snapX = roundUpToNearestMultiple((int)currX, App.TILE_SIZE/2);
    ridingSprite.setLocation(new Position(snapX, currY));
  }

	
  @Override
	public void respawn() {
		super.respawn();
		if (ridingSprites.size() > 0) {
			for (Sprite sprite: ridingSprites) {
				sprite.setLocation(centerPosition);
			}
		}
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
    ridingSprites.add((Sprite)rider); 
    snapRider(rider);
  }
  public void removeRider(Rideable rider){
	  if (!(rider instanceof Rideable)) {
		  // this shouldn't happen
	  } else if (ridingSprites.contains((Sprite)rider)) {
		  ridingSprites.remove((Sprite)rider);
	  }
  }

}
