package core;

import base.*;
import customsprites.BikeSprite;
import customsprites.SolidPushSprite;
import customsprites.TurtleSprite;
import utilities.Position;

import java.nio.file.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Helper class that reads, parses, and categorieses assets for a given level.
 * All generates Sprites are stored in a list referred to as the 'Sprite Map'
 */
public class LevelAssetManager {
  private static final String ASSET_ROOT = "assets";
  private static final String LEVELS_ROOT = "levels";
  private static final String PLAYER_ASSET_NAME = "frog";
  private static final String ASSET_IMAGE_EXTENSION = ".png";
  private final int LEVEL;
  private final Position PLAYER_START_POS = new Position(512, 720);

  private final Map<String, AssetType> assetTypes = new HashMap<>();
  private final List<String> specialSprites = new ArrayList<>();
  private final Map<String, Velocity> speedInfo = new HashMap<>();
  private final Logger log = Logger.getLogger(getClass().getSimpleName());
  private Player player;
  private List<Sprite> spriteMap;
  private World world;

  public LevelAssetManager(World world, int level) {
    /* keeps all assets with specialised behaviour separately */
    specialSprites.add("turtles");
    specialSprites.add("bike");
    specialSprites.add("bulldozer");
    /* labels all the assets in the game with basic behaviour*/
    assetTypes.put("water", AssetType.PASSIVE_OBSTACLE);
    assetTypes.put("grass", AssetType.FRIENDLY_TILE);
    assetTypes.put("tree", AssetType.SOLID_TILE);
    assetTypes.put("bus", AssetType.MOVING_OBSTACLE);
    assetTypes.put("bulldozer", AssetType.SOLID_TILE);
    assetTypes.put("log", AssetType.DRIVER_OBJECT);
    assetTypes.put("longlog", AssetType.DRIVER_OBJECT);
    assetTypes.put("racecar", AssetType.MOVING_OBSTACLE);
    /* labels all moving objects with their movement velocities */
    speedInfo.put("bus", new Velocity(0.15f, 0));
    speedInfo.put("bike", new Velocity(0.2f, 0));
    speedInfo.put("bulldozer", new Velocity(0.05f, 0));
    speedInfo.put("log", new Velocity(0.1f, 0));
    speedInfo.put("longlog", new Velocity(0.07f, 0));
    speedInfo.put("racecar", new Velocity(0.5f, 0));
    speedInfo.put("turtles", new Velocity(0.085f, 0));
    LEVEL = level;
    this.world = world;
    spriteMap = new ArrayList<>();
  }

  private static String getAssetPath(String assetName) {
    return Paths.get(ASSET_ROOT, assetName + ASSET_IMAGE_EXTENSION).toString();
  }

  /**
   * Gets the current player.
   *
   * @return Reference to Player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets all the sprites on the current level.
   *
   * @return List of all Sprite objects
   */
  public List<Sprite> getSpriteMap() {
    return spriteMap;
  }

  /**
   * Gets all the sprites whose hitbox intersects at point
   *
   * @param pos The point to check intersection at
   * @return A list of Sprites objects
   */
  public List<Sprite> getSpritesAt(Position pos) {
    Sprite dummySprite = new Sprite(world, null, getAssetPath(PLAYER_ASSET_NAME), pos);
    List<Sprite> returnList = new ArrayList<>();
    for (Sprite s : spriteMap) {
      if (s.getHitBox().intersects(dummySprite.getHitBox())) returnList.add(s);
    }
    return returnList;
  }

  /**
   * Gets all the sprites whose hitbox intersects with the hitbox of specified Sprite
   *
   * @param sprite Sprite to check hitbox collision
   * @return A list of Sprites
   */
  public List<Sprite> getIntersectingSprites(Sprite sprite) {
    return getSpriteMap()
        .stream()
        .filter(s -> s != sprite && s.getHitBox().intersects(sprite.getHitBox()))
        .collect(Collectors.toList());
  }

  /**
   * Gets all the sprites whose hitbox intersects with the hitbox of specified Sprite and satisfies
   * a specified boolean condition
   *
   * @param sprite Sprite to check hitbox collision
   * @return A list of Sprites
   */
  public List<Sprite> getIntersectingSprites(Sprite sprite, Predicate<Sprite> predicate) {
    return getIntersectingSprites(sprite).stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Adds a Sprite object to the Sprite Map
   *
   * @param newSprite The Sprite object to add to the Sprite Map
   */
  public void addSprite(Sprite newSprite) {
    spriteMap.add(newSprite);
  }

  /**
   * Removes a given Sprite from the Sprite Map
   *
   * @param sprite The Sprite to remove from the Sprite Map
   */
  public void removeSprite(Sprite sprite) {
    spriteMap.remove(sprite);
  }

  /**
   * Parses the lines of the appropriate level file
   *
   * @return A list of lines in the file (needs more processing)
   */
  private List<String> readAssets() {
    String relFilePath =
        Paths.get(ASSET_ROOT, LEVELS_ROOT, String.format("%d.lvl", LEVEL)).toString();
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

  /** Adds the player to the Sprite Map */
  private void addPlayer() {
    player = new Player(world, getAssetPath(PLAYER_ASSET_NAME), PLAYER_START_POS);
    spriteMap.add(player);
  }

  /**
   * Parses and categorises lines from the level file into their appropriate Sprite subclasses and
   * adds them the Sprite Map
   */
  public void loadAssets() {
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
      String imageSrc = getAssetPath(assetName);
      Position spawnPos =
          new Position(Float.parseFloat(assetInfo[1]), Float.parseFloat(assetInfo[2]));
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
            newSprite = new TurtleSprite(world, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bike":
            newSprite = new BikeSprite(world, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bulldozer":
            newSprite = new SolidPushSprite(world, assetName, imageSrc, spawnPos, newVelocity);
            break;
        }
      } else {
        assert assetType != null;
        switch (assetType) {
          case MOVING_OBSTACLE:
            newSprite = new Obstacle(world, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case DRIVER_OBJECT:
            newSprite = new Driver(world, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case PASSIVE_OBSTACLE:
            newSprite = new Obstacle(world, assetName, imageSrc, spawnPos);
            break;
          case SOLID_TILE:
          case FRIENDLY_TILE:
            newSprite = new Sprite(world, assetName, imageSrc, spawnPos);
            break;
        }
      }
      if (newSprite != null) {
        spriteMap.add(newSprite);
      }
    }
    addPlayer();
  }

  /**
   * Helper method to identify the type of sprite based on the Sprite's name
   *
   * @param spriteName Name of the Sprite
   * @return AsseType specifying the type of asset the Sprite is
   */
  public AssetType getAssetType(String spriteName) {
    return assetTypes.getOrDefault(spriteName, null);
  }

  /**
   * Sets the location of the player to the default spawn location and detaches any existing drivers
   */
  public void resetPlayer() {
    player.detachDriver();
    log.info("Resetting sprite to " + PLAYER_START_POS);
    player.setLocation(PLAYER_START_POS);
  }
}
