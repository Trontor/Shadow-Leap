import java.io.File;
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.Position;

public class World {
	
	private final int TILE_SIZE = 48;
	private final int LEVEL;

	private final Map<String, AssetType> AssetTypes = new HashMap<String, AssetType>();
	private final Map<String, Velocity> SpeedInfo = new HashMap<String, Velocity>();
	/** A list of all Sprites currently on the world */
	private List<Sprite> spriteMap;
	public List<Sprite> getSpriteMap(){
		return spriteMap;
	} 
	
	/** Initialises a new World */
	public World(int level) {
		AssetTypes.put("water", AssetType.PassiveObstacle);
		AssetTypes.put("grass", AssetType.Tile);
		AssetTypes.put("tree", AssetType.Tile);
		AssetTypes.put("bus", AssetType.MovingObstacle);
		AssetTypes.put("bike", AssetType.MovingObstacle);
		AssetTypes.put("bulldozer", AssetType.MovingObstacle);
		AssetTypes.put("log", AssetType.Ridable);
		AssetTypes.put("longlog", AssetType.Ridable);
		AssetTypes.put("racecar", AssetType.MovingObstacle);
		AssetTypes.put("turtle", AssetType.PassiveObstacle);
		
		SpeedInfo.put("bus", new Velocity(0.15f, 0));
		SpeedInfo.put("bike", new Velocity(0.2f, 0));
		SpeedInfo.put("bulldozer", new Velocity(0.05f, 0));
		SpeedInfo.put("log", new Velocity(0.1f, 0));
		SpeedInfo.put("longlog", new Velocity(0.07f, 0));
		SpeedInfo.put("racecar", new Velocity(0.5f, 0));
		SpeedInfo.put("turtle", new Velocity(0.085f, 0));
		
		this.LEVEL = level; 
		try {
			//AddMap();
			LoadAssets();
			AddPlayers(); 
		} catch (SlickException e) { 
			e.printStackTrace();
		}
	}
	
	private List<String> ReadAssets() { 
		List<String> lines = new ArrayList<String>();
		File file = new File(String.format("assets/levels/%d.lvl", LEVEL));
        Scanner sc = null;
		try {
			sc = new Scanner(file);
	        while(sc.hasNextLine()){
	        	lines.add(sc.nextLine());
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sc != null)
				sc.close();
		}
		return lines;
	}
	

	private void LoadAssets() throws SlickException {
		spriteMap = new ArrayList<Sprite>(); 
		List<String> assets = ReadAssets();
		for (String line : assets) {
			String[] assetInfo = line.split(",");
			String assetName = assetInfo[0].toLowerCase();
			AssetType assetType;
			if (AssetTypes.containsKey(assetName)) {
				assetType = AssetTypes.get(assetName);
			} else {
				//System.out.println("Tried to load unknown asset type: " + assetName);
				continue;
			} 
			Sprite newSprite = null;
			String imageSrc = String.format("assets/%s.png", assetName);
			float x = Float.parseFloat(assetInfo[1]);
			float y = Float.parseFloat(assetInfo[2]);
			Position spawnPos = new Position(x, y);
			
			
			boolean moveRight;
			Velocity newVelocity = new Velocity(0, 0);
			/* checks if there is information about direction of movement*/
			if (assetInfo.length > 3) {
				if (SpeedInfo.containsKey(assetName)) {
					newVelocity = SpeedInfo.get(assetName);
				} 
				moveRight = Boolean.parseBoolean(assetInfo[3]);
				newVelocity = new Velocity((moveRight ? 1: -1)*newVelocity.getHorizontal(), 0);
			}
			switch (assetType) {
			case MovingObstacle: 
				newSprite = new Obstacle(this, assetName, imageSrc, spawnPos, newVelocity);
				break;
			case PassiveObstacle: 
				newSprite = new Obstacle(this, assetName, imageSrc, spawnPos);
				break; 
			case Tile: 
				newSprite = new Sprite(this, assetName, imageSrc, spawnPos);
				break; 
			case Ridable:
				moveRight = Boolean.parseBoolean(assetInfo[3]); 
				newSprite = new Rideable(this, assetName, imageSrc, spawnPos, newVelocity);
			}
			if (newSprite != null) {
				spriteMap.add(newSprite);
			}
		}
	}
	
	/** Adds all the players to the Sprite Map
	 * @throws SlickException Slick Library Error
	 */
	private void AddPlayers() throws SlickException {
		Player player = new Player(this, "assets/frog.png", new Position(512, 720));
		spriteMap.add(player);
	} 
	
	/** Creates and renders a new map with passive Sprites 
	 * @throws SlickException Slick Library Error
	 */
	private void AddMap() throws SlickException {
		spriteMap = new ArrayList<Sprite>(); 
		/* hardcoded map */
		List<Integer> grassYs = Arrays.asList(672, 384);
		List<Integer> waterYs = new ArrayList<Integer>();
		/* water filled in range y = 48 -> 336 */
		int waterRangeStart = 48 + TILE_SIZE;
		int waterRangeFinish = 336;
		for (int i = waterRangeStart; i <= waterRangeFinish; i += TILE_SIZE) {
			waterYs.add(i);
		}
		/* fills grass across specified y locations */
		for (int y: grassYs) {
			for (int x = 0; x <= App.SCREEN_WIDTH; x += TILE_SIZE) {
				Sprite newSprite = new Sprite(this, "Grass", "assets/grass.png", new Position(x, y));	
				if (newSprite != null)
					spriteMap.add(newSprite);
			}
		}
		/* fills water across specified y range*/
		for (int y: waterYs) {
			for (int x = 0; x <= App.SCREEN_WIDTH; x += TILE_SIZE) {
				Sprite newSprite = new Obstacle(this, "Water", "assets/water.png", new Position(x, y), null);	
				if (newSprite != null)
					spriteMap.add(newSprite);	
			}
		} 
	}
	
	/** Changes the state of the world
	 * @param state The state to change WorldState to
	 */
	public void ChangeWorldState(WorldState state) { 
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
	
	/** Update all time-supported Sprites
	 * @param input Describes device input state (keyboard/mouse/controller)
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta) {
		List<Sprite> timeSupportSprites = getSpriteMap().stream()
													    .filter(s-> s instanceof TimeSupport)
													    .collect(Collectors.toList());
		for (Sprite s : timeSupportSprites) {
			((TimeSupport)s).onTimeTick(delta);
		}	
	}
	
	/** Signals all key-pressed Sprites of a new key press
	 * @param key Integer value of pressed key (ASCII)
	 * @param c Java character representation of pressed key
	 */
	public void onKeyPressed(int key, char c) {
		List<Sprite> keySupportSprites = getSpriteMap().stream()
														.filter(s-> s instanceof KeySupport)
													   .collect(Collectors.toList());
		for (Sprite s: keySupportSprites) {
			((KeySupport)s).onKeyPress(key, c);
		}
	}	
	
	/** Renders all sprites on the Sprite Map onto the World
	 * @param g The Graphics object to render the World on
	 */
	public void render(Graphics g) {
		for (Sprite s: getSpriteMap()) {
			s.render(g);
		}
	}
}
