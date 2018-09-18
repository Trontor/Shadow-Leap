package core;

import base.AssetType;
import base.Driver;
import base.Obstacle;
import base.Player;
import base.Sprite;
import base.Velocity;
import customsprites.BikeSprite;
import customsprites.MagicianSprite;
import customsprites.SolidPushSprite;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import utilities.Position;

/**
 * Helper class that reads, parses, and categorieses assets for a given level. All generated Sprites
 * are stored in a list referred to as the 'Sprite Map'
 */
public class SpriteAssetManager {

  /** The folder name that contains all the levels and images */
  private static final String ASSET_ROOT = "assets";
  /** The folder name that is a child of the asset root and contains all level data files */
  private static final String LEVELS_ROOT = "levels";
  /** The name of the player that will be the proxy of all inputs */
  private static final String PLAYER_ASSET_NAME = "frog";
  /** The extension of the image files used for all assets */
  private static final String ASSET_IMAGE_EXTENSION = ".png";
  /** The starting position for the player */
  private final Position PLAYER_START_POS = new Position(512, 720);

  /**
   * A map of asset name -> asset type associations that describe the purpose of a sprite based on
   * its name
   */
  private final Map<String, AssetType> assetTypes = new HashMap<>();
  /** A list of custom sprites that have special hard-coded abilities */
  private final List<String> specialSprites = new ArrayList<>();
  /**
   * A map of asset name -> movement velocity associations that describe the default speeds for
   * moving sprites
   */
  private final Map<String, Velocity> speedInfo = new HashMap<>();
  /** Used for internal JVM logging */
  private final Logger log = Logger.getLogger(getClass().getSimpleName());
  /** A complete list of all Sprites that exist on the level and can be rendered */
  private final List<Sprite> spriteMap;
  /** The level that this class is managing */
  private final Level level;
  /** The main sprite that receives input events */
  private Player player;

  /**
   * Initialises an instance of SpriteAssetManager, whos purpose is to handle asset loading and
   * management for a specific level
   *
   * @param level The level to manage
   */
  public SpriteAssetManager(Level level) {
    this.level = level;
    spriteMap = new ArrayList<>();
    /* keeps all assets with specialised behaviour separately */
    specialSprites.add("turtles");
    specialSprites.add("bike");
    specialSprites.add("bulldozer");
    /* labels all the assets in the game with basic behaviour */
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
  }

  /**
   * Returns the relative path to the image of an asset (no file existence checking)
   *
   * @param assetName The asset to retrieve the image path to
   * @return The relative path to the asset image
   */
  public static String getAssetPath(String assetName) {
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
   * Utilises the power of lambda expressions for quick filtering
   *
   * @param predicate the boolean expression to evaluate on search
   */
  public List<Sprite> filterSprites(Predicate<Sprite> predicate) {
    return getSpriteMap().stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Gets all the sprites whose hitbox intersects at point
   *
   * @param pos The point to check intersection at
   * @return A list of Sprites objects
   */
  public List<Sprite> getSpritesAt(Position pos) {
    Sprite dummySprite = new Sprite(level, null, getAssetPath(PLAYER_ASSET_NAME), pos);
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
    return filterSprites(s -> s != sprite && s.getHitBox().intersects(sprite.getHitBox()));
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
   * Adds an obstacle that has the same visual representation as the player
   *
   * @param fauxName The name that describes the faux player
   * @param pos The position to spawn the faux player
   */
  public void addFauxPlayer(String fauxName, Position pos) {
    addSprite(new Obstacle(level, fauxName, getAssetPath(PLAYER_ASSET_NAME), pos));
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
        Paths.get(ASSET_ROOT, LEVELS_ROOT, String.format("%d.lvl", level.getLevelNumber()))
            .toString();
    List<String> lines = new ArrayList<>();
    File file = new File(String.format(relFilePath, level.getLevelNumber()));
    try (Scanner sc = new Scanner(file)) {
      while (sc.hasNextLine()) {
        lines.add(sc.nextLine());
      }
    } catch (FileNotFoundException e) {
      log.log(
          java.util.logging.Level.SEVERE,
          String.format("Level file: %s does not exist.\n", relFilePath));
    }
    return lines;
  }

  /** Adds the player to the Sprite Map */
  private void addPlayer() {
    player = new Player(level, getAssetPath(PLAYER_ASSET_NAME), PLAYER_START_POS);
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
        log.log(java.util.logging.Level.WARNING, "Tried to load unknown asset type: " + assetName);
        continue;
      }
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
        newVelocity =
            new Velocity(
                (moveRight ? 1 : -1) * newVelocity.getHorizontal(), newVelocity.getVertical());
      }
      Sprite newSprite = null;
      if (specialSprites.contains(assetName)) {
        switch (assetName) {
          case "turtles":
            newSprite = new MagicianSprite(level, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bike":
            newSprite = new BikeSprite(level, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case "bulldozer":
            newSprite = new SolidPushSprite(level, assetName, imageSrc, spawnPos, newVelocity);
            break;
        }
      } else {
        assert assetType != null;
        switch (assetType) {
          case MOVING_OBSTACLE:
            newSprite = new Obstacle(level, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case DRIVER_OBJECT:
            newSprite = new Driver(level, assetName, imageSrc, spawnPos, newVelocity);
            break;
          case PASSIVE_OBSTACLE:
            newSprite = new Obstacle(level, assetName, imageSrc, spawnPos);
            break;
          case SOLID_TILE:
          case FRIENDLY_TILE:
            newSprite = new Sprite(level, assetName, imageSrc, spawnPos);
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
