package base;

import java.util.ArrayList;
import java.util.List;

import utilities.Position;
import core.World;

public class Driver extends MovingSprite implements Touchable {

	private List<Rideable> ridingSprites;
  private boolean rideable = true;

  public boolean isRideable() {
    return rideable;
  }

  public void setRideable(boolean rideable) {
    if (rideable == false){
      for (int i = 0; i < ridingSprites.size(); i++){
        Rideable passenger = ridingSprites.get(i);
        ridingSprites.get(i).detachDriver();
        if (passenger instanceof CollisionDetection){
          ((CollisionDetection)passenger).checkCollision();
        }
        i--;
      }
    }
    this.rideable = rideable;
  }

  public Driver(World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity) {
		super(spawnWorld, Name, imageSrc, centerPos, velocity); 
		ridingSprites = new ArrayList<>();
	}

	public void removeRider(Rideable rider) {
        ridingSprites.remove(rider);
	}  
	
	@Override
	public void onTimeTick(int delta) { 
		float deltaX = super.getMovementVelocity().getHorizontal() * delta;
		float deltaY = super.getMovementVelocity().getVertical() * delta;
		ridingSprites.forEach(s->((Sprite)s).setLocationDelta(deltaX, deltaY));
		super.onTimeTick(delta);
	}

	@Override
	public void onTouch(Sprite touching) {
		if (touching instanceof Rideable && !ridingSprites.contains(touching)) {
			ridingSprites.add((Rideable)touching);
		}
	}  
}
