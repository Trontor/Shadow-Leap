import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class World {
	private List<Sprite> spriteMap;
	final int TILE_SIZE = 48;
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
		List<PatternInfo> bus_patterns = new ArrayList<PatternInfo>();
		bus_patterns.add(new PatternInfo(432, (float) 6.5, 48));
		bus_patterns.add(new PatternInfo(480, (float) 5, 0));
		bus_patterns.add(new PatternInfo(528, (float) 12, 64));
		bus_patterns.add(new PatternInfo(576, (float) 5, 128));
		bus_patterns.add(new PatternInfo(624, (float) 6.5, 250));
		boolean right = false;
		for(PatternInfo info : bus_patterns) {
			Velocity bus_velocity = new Velocity((right? -1: 1)*(float)0.15, 0);
			for (float x = info.getOffset(); x < App.SCREEN_WIDTH ; x += TILE_SIZE *(info.getSeparation() + 1)) {
				Sprite new_bus = new Obstacle(this, "Bus", "assets/bus.png", x, info.getYlocation(), bus_velocity);
				spriteMap.add(new_bus);
			}
			right = !right;
		}
	}
	
	private void AddPlayers() throws SlickException {
		/* add player */
		Player player = new Player(this, "assets/frog.png", 512, 720);
		spriteMap.add(player);
	} 
	
	private void AddMap() throws SlickException {
		spriteMap = new ArrayList<Sprite>(); 
		/* hardcode map */
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
				Sprite newSprite = new Sprite(this, "Grass", "assets/grass.png", x, y);	
				if (newSprite != null)
					spriteMap.add(newSprite);	
			}
		}
		for (int y: waterYs) {
			for (int x = 0; x <= App.SCREEN_WIDTH; x += TILE_SIZE) {
				Sprite newSprite = new Obstacle(this, "Water", "assets/water.png", x, y, null);	
				if (newSprite != null)
					spriteMap.add(newSprite);	
			}
		} 
	}
	
	public List<Sprite> getAllSprites(){
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
		for (Sprite s : getAllSprites()) {
			s.update(input, delta);
			if (s instanceof TimeSupport) {
				((TimeSupport) s).OnTimeTick(delta);
			}
		}	
	}
	
	public void onKeyPressed(int key, char c) {
		for (Sprite s: getAllSprites()) {
			if (s instanceof KeySupport) {
				((KeySupport)s).onKeyPress(key, c);
			}
		}
	}
	
	public void render(Graphics g) {
		// Draw all of the sprites in the game 
		for (Sprite s: getAllSprites()) {
			s.render(g);
		}
		
	}
}
