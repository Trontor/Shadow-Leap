import org.newdawn.slick.SlickException; 
import utilities.Position;

public class PassiveObstacle extends Sprite implements Collidable{

	public PassiveObstacle(World spawnWorld, String Name, String imageSrc, Position centerPos) throws SlickException {
		super(spawnWorld, Name, imageSrc, centerPos); 
	}

}
