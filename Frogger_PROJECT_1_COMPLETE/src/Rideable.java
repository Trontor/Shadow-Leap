import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import utilities.Position;

public class Rideable extends MovingSprite implements Touchable {

	private List<Sprite> ridingSprites;
	public Rideable(World spawnWorld, String Name, String imageSrc, Position centerPos, Velocity velocity)
			throws SlickException {
		super(spawnWorld, Name, imageSrc, centerPos, velocity); 
		ridingSprites = new ArrayList<Sprite>();
	}
	public void RemoveRider(Sprite rider) {
		if (ridingSprites.contains(rider)) {
			ridingSprites.remove(rider);
		}
	}  
	
	@Override
	public void onTimeTick(int delta) { 
		float deltaX = super.getMovementVelocity().getHorizontal() * delta;
		float deltaY = super.getMovementVelocity().getVertical() * delta;
		ridingSprites.forEach(s->s.setLocationDelta(deltaX, deltaY));
		super.onTimeTick(delta);
	}
	@Override
	public void OnTouch(Sprite touching) {
		if (!ridingSprites.contains(touching)) {
			ridingSprites.add(touching);
		}
	}  
}
