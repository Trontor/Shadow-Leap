import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.Position;

public class World {
	private List<Sprite> spriteMap;
	private final int TILE_SIZE = 48;
	
	public World() {
		spriteMap = new ArrayList<Sprite>();
		try {
			AddMap();
			AddPlayers(); 
			AddObstacles();
		} catch (SlickException e) { 
			e.printStackTrace();
		}
	}
	
	private void AddObstacles() throws SlickException {
		/* add buses */
		List<PatternInfo> busPatterns = new ArrayList<PatternInfo>();
		busPatterns.add(new PatternInfo(432, 6.5f, 48));
		busPatterns.add(new PatternInfo(480, 5, 0));
		busPatterns.add(new PatternInfo(528, 12, 64));
		busPatterns.add(new PatternInfo(576, 5, 128));
		busPatterns.add(new PatternInfo(624, 6.5f, 250));
		boolean right = false;
		for(PatternInfo info : busPatterns) {
			Velocity busVelocity = new Velocity((right? -1: 1)*0.15f, 0);
			float xDelta = TILE_SIZE *(info.getSeparation() + 1);
			for (float x = info.getOffset(); x < App.SCREEN_WIDTH; x += xDelta) {
				Position spawnLocation = new Position(x, info.getYlocation());
				Sprite newBus = new Obstacle(this, "Bus", "assets/bus.png", spawnLocation, busVelocity);
				spriteMap.add(newBus);
			}
			right = !right;
		}
	}
	
	private void AddPlayers() throws SlickException {
		Player player = new Player(this, "assets/frog.png", new Position(512, 720));
		spriteMap.add(player);
	} 
	
	private void AddMap() throws SlickException {
		spriteMap = new ArrayList<Sprite>(); 
		/* hardcoded map */
		List<Integer> grassYs = Arrays.asList(672, 384);
		List<Integer> waterYs = new ArrayList<Integer>();
		/* water filled in range y = 48 -> 336 */
		int waterRangeStart = 48 + TILE_SIZE;
		int waterRangeFinish = 336 ;
		for (int i = waterRangeStart; i <= waterRangeFinish; i += TILE_SIZE) {
			waterYs.add(i);
		}
		for (int y: grassYs) {
			for (int x = 0; x <= App.SCREEN_WIDTH; x += TILE_SIZE) {
				Sprite newSprite = new Sprite(this, "Grass", "assets/grass.png", new Position(x, y));	
				if (newSprite != null)
					spriteMap.add(newSprite);
			}
		}
		for (int y: waterYs) {
			for (int x = 0; x <= App.SCREEN_WIDTH; x += TILE_SIZE) {
				Sprite newSprite = new Obstacle(this, "Water", "assets/water.png", new Position(x, y), null);	
				if (newSprite != null)
					spriteMap.add(newSprite);	
			}
		} 
	}
	
	public List<Sprite> getSpriteMap(){
		return spriteMap;
	}
	
	public void ChangeGameState(WorldState state) { 
		switch(state) {
			case Death:
				App.CloseGame();
		case Finished:
			/* to do: world switching */
			break;
		default:
			break;
		}
	}
	
	public void update(Input input, int delta) {
		List<Sprite> timeSupportSprites = getSpriteMap().stream()
													    .filter(s-> s instanceof TimeSupport)
													    .collect(Collectors.toList());
		for (Sprite s : timeSupportSprites) {
			((TimeSupport)s).onTimeTick(delta);
		}	
	}
	
	public void onKeyPressed(int key, char c) {
		List<Sprite> keySupportSprites = getSpriteMap().stream()
													   .filter(s-> s instanceof KeySupport)
													   .collect(Collectors.toList());
		for (Sprite s: keySupportSprites) {
			((KeySupport)s).onKeyPress(key, c);
		}
	}	
	public void render(Graphics g) { 
		/* renders every sprite in the sprite map */
		for (Sprite s: getSpriteMap()) {
			s.render(g);
		}
	}
}
