package core;

import base.AssetType;
import base.Driver;
import base.KeySupport;
import base.Obstacle;
import base.Player;
import base.Sprite;
import base.TimeSupport;
import base.Velocity;
import base.WorldState;
import customsprites.BikeSprite;
import customsprites.PowerUp;
import customsprites.SolidPushSprite;
import customsprites.TurtleSprite;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import utilities.Position;

public class World {

  private final int LEVEL;
  private final Position PLAYER_START_POS = new Position(512, 720);
  private final String WIN_MARKER = "filledhole";
  public final float WINNING_Y = 48;
  private final int WINNING_X_START = 120;
  private final int WINNING_X_SEPARATION = 192;
  private final int EXTRA_LIFE_MIN_WAIT = 25;
  private final int EXTRA_LIFE_MAX_WAIT = 35;

  private Logger log = Logger.getLogger(getClass().getSimpleName());
  private Player player;
  private final Map<String, AssetType> assetTypes = new HashMap<>();
  private final List<String> specialSprites = new ArrayList<>();
  private final Map<String, Velocity> speedInfo = new HashMap<>();
  private final List<Position> winningPositions = new ArrayList<>();
  private final int extraLifeSpawnTime;
  private int spawnExtraLifeTime = 0;

  /** A list of all Sprites currently on the world */
  private List<Sprite> spriteMap;
  /**
   * Gets list of sprites.
   * @return ArrayList of Sprites currently on the map
   */
  private List<Sprite> getSpriteMap() {
    return spriteMap;
  }

  /**
   * Gets the center Position of the closest hole.
   * @param location Position to search for closest hole near to
   * @return Position representing the center of the closest 'hole'
   */
  private Position getClosestHolePosition(Position location) {
    Position returnPos = winningPositions.get(0);
    float closest = location.distanceTo(returnPos);
    for (Position possiblePosition : winningPositions) {
      float distance = location.distanceTo(possiblePosition);
      if (distance < closest) {
        closest = distance;
        returnPos = possiblePosition;
      }
    }
    return returnPos;
  }

  /**
   * Checks if all holes have been filled.
   * @return True if all holes have been filled, else False
   */
  private boolean checkWin() {
    for (Position winningPos : winningPositions) {
      boolean holeFilled = false;
      for (Sprite sprite : getSpritesAt(winningPos)) {
        if (sprite.getSpriteName().equals(WIN_MARKER)) {
          holeFilled = true;
        }
      }
      if (!holeFilled)
        return false;
    }
    return true;
  }

  /** Initialises a new core.World */
  public World(int level) {
    /* Keeps all assets with specialised behaviour separately */
    specialSprites.add("turtles");
    specialSprites.add("bike");
    specialSprites.add("bulldozer");
    /* Labels all the assets in the game with basic behaviour*/
    assetTypes.put("water", AssetType.PASSIVE_OBSTACLE);
    assetTypes.put("grass", AssetType.FRIENDLY_TILE);
    assetTypes.put("tree", AssetType.SOLID_TILE);
    assetTypes.put("bus", AssetType.MOVING_OBSTACLE);
    assetTypes.put("bulldozer", AssetType.SOLID_TILE);
    assetTypes.put("log", AssetType.DRIVER_OBJECT);
    assetTypes.put("longlog", AssetType.DRIVER_OBJECT);
    assetTypes.put("racecar", AssetType.MOVING_OBSTACLE);
    /* Labels all moving objects with their movement velocities */
    speedInfo.put("bus", new Velocity(0.15f, 0));
    speedInfo.put("bike", new Velocity(0.2f, 0));
    speedInfo.put("bulldozer", new Velocity(0.05f, 0));
    speedInfo.put("log", new Velocity(0.1f, 0));
    speedInfo.put("longlog", new Velocity(0.07f, 0));
    speedInfo.put("racecar", new Velocity(0.5f, 0));
    speedInfo.put("turtles", new Velocity(0.085f, 0));
    /* Stores Position of all holes to be filled */
    for (int x = WINNING_X_START; x < App.SCREEN_WIDTH; x += WINNING_X_SEPARATION){
      winningPositions.add(new Position(x, WINNING_Y));
    }
    LEVEL = level;
    loadAssets();
    addPlayer();
    extraLifeSpawnTime = getRandomNumber(EXTRA_LIFE_MIN_WAIT, EXTRA_LIFE_MAX_WAIT) * 1000;
  }

  /**
   * Parses the lines of the appropriate level file
   * @return A list of lines in the file (needs more processing)
   */
  private List<String> readAssets() {
    String relFilePath = "assets/levels/%d.lvl";
    List<String> lines = new ArrayList<>();
    File file = new File(String.format(relFilePath, LEVEL));
    try (Scanner sc = new Scanner(file)) {
      while (sc.hasNextLine()) {
        lines.add(sc.nextLine());
      }
    } catch (FileNotFoundException e) {
      log.log(Level.SEVERE, String.format("Level file: %s does not exist.\n", relFilePath));
    }
    return lines;
  }

  private void loadAssets() {
    spriteMap = new ArrayList<>();
    List<String> assets = readAssets();
    for (String line : assets) {
      String[] assetInfo = line.split(",");
      String assetName = assetInfo[0].toLowerCase();
      if (assetName.equals("turtle")) {
        assetName = "turtles";
      }
      AssetType assetType = null;
      if (assetTypes.containsKey(assetName)) {
        assetType = assetTypes.get(assetName);
      } else if (!specialSprites.contains(assetName)) {
    	  log.log(Level.WARNING, "Tried to load unknown asset type: " + assetName);
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
        if (speedInfo.containsKey(assetName)) {
          newVelocity = speedInfo.get(assetName);
        }
        moveRight = Boolean.parseBoolean(assetInfo[3]);
        newVelocity = new Velocity((moveRight ? 1 : -1) * newVelocity.getHorizontal(), 0);
      }
      if (specialSprites.contains(assetName)) {
        switch (assetName) {
          case "turtles":
            newSprite = new TurtleSprite(this, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bike":
            newSprite = new BikeSprite(this, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bulldozer":
            newSprite = new SolidPushSprite(this, assetName, imageSrc, spawnPos, newVelocity);
            break;
        }
      } else {
        assert assetType != null;
        switch (assetType) {
          case MOVING_OBSTACLE:
            newSprite = new Obstacle(this, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case DRIVER_OBJECT:
            newSprite = new Driver(this, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case PASSIVE_OBSTACLE:
            newSprite = new Obstacle(this, assetName, imageSrc, spawnPos);
            break;
          case SOLID_TILE:
          case FRIENDLY_TILE:
            newSprite = new Sprite(this, assetName, imageSrc, spawnPos);
            break;
        }
      }
      if (newSprite != null) {
        spriteMap.add(newSprite);
      }
    }
  }

  /** Adds all the players to the base.Sprite Map */
  private void addPlayer() {
    player = new Player(this, "assets/frog.png", PLAYER_START_POS);
    spriteMap.add(player);
  }

  public void removeSprite(Sprite sprite) {
    spriteMap.remove(sprite);
  }

  /**
   * Changes the state of the world
   *
   * @param state The state to change base.WorldState to
   */
  public void changeWorldState(WorldState state) {
	log.info("World State Changed: " + "Current State = " + state.toString());
    switch (state) {
      case Death:
        if (!player.removeLife()) {
          App.CloseGame();
        } else {
          player.reset();
        }
      case Finished:
        /* to do: world switching */
        break;
      case PartlyFinished:
        Position winLocation = getClosestHolePosition(player.getPosition());
        player.reset();
        spriteMap.add(new Obstacle(this, WIN_MARKER, "assets/frog.png", winLocation));
        if (checkWin()) {
          App.nextWorld();
        }
    }
  }

  /**
   * Utilises the power of lambda expressions for quick filtering
   *
   * @param predicate the boolean expression to evaluate on search
   */
  public List<Sprite> filterSprites(Predicate<Sprite> predicate) {
    return getSpriteMap().stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Update all time-supported Sprites
   *
   * @param input Describes device input state (keyboard/mouse/controller)
   * @param delta Time passed since last frame (milliseconds).
   */
  public void update(Input input, int delta) {
    spawnExtraLifeTime += delta;
    if (spawnExtraLifeTime >= extraLifeSpawnTime) {
      spawnExtraLife();
      spawnExtraLifeTime = 0;
    }
    List<Sprite> timeSupportSprites = filterSprites(s -> s instanceof TimeSupport);
    for (Sprite s : timeSupportSprites) {
      ((TimeSupport) s).onTimeTick(delta);
    }
  }

  /**
   * Signals all key-pressed Sprites of a new key press
   *
   * @param key Integer value of pressed key (ASCII)
   * @param c Java character representation of pressed key
   */
  public void onKeyPressed(int key, char c) {
    List<Sprite> keySupportSprites = filterSprites(s -> s instanceof KeySupport);
    for (Sprite s : keySupportSprites) {
      ((KeySupport) s).onKeyPress(key, c);
    }
  }

  /**
   * Renders all sprites on the base.Sprite Map onto the core.World
   *
   * @param g The Graphics object to render the core.World on
   */
  public void render(Graphics g) {
    for (Sprite s : getSpriteMap()) {
      s.render(g);
    }
  }

  public List<Sprite> getIntersectingSprites(Sprite sprite) {
    return getSpriteMap()
        .stream()
        .filter(s -> s != sprite && s.getHitBox().intersects(sprite.getHitBox()))
        .collect(Collectors.toList());
  }

  public AssetType getAssetType(String spriteName) {
    return assetTypes.getOrDefault(spriteName, null);
  }

  public List<Sprite> getSpritesAt(Position pos) {
    Sprite dummySprite = new Sprite(this, null, "assets/frog.png", pos);
    List<Sprite> returnList = new ArrayList<>();
    for (Sprite s : spriteMap) {
      if (s.getHitBox().intersects(dummySprite.getHitBox())) returnList.add(s);
    }
    return returnList;
  }

  private int getRandomNumber(int min, int max) {
    int diff = max - min;
    Random random = new Random();
    int i = random.nextInt(diff + 1);
    i += min;
    return i;
  }

  private void spawnExtraLife() {
    List<Sprite> logs = filterSprites(s -> s.getSpriteName().contains("log"));
    Sprite randomLog = logs.get(getRandomNumber(0, logs.size() - 1));
    PowerUp extraLife =
        new PowerUp(this, "extralife", "assets/extralife.png", randomLog.getPosition());
    extraLife.attachDriver((Driver) randomLog);
    log.info("Spawned extra life on log at " + extraLife.getPosition());
    spriteMap.add(extraLife);
  }
}
