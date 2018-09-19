package core;

import base.*;
import customsprites.PowerUp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import utilities.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/** A wrapper class that encapsulates all the sprites and events for a specified level. */
public class Level {
  /** The Y coordinate for the winning rpw */
  private static final int WINNING_Y = 48;
  /** Bottom limit to random spawn time for the extra life object */
  private static final int EXTRA_LIFE_MIN_WAIT = 25;
  /** Upper limit to random spawn time for the extra life object */
  private static final int EXTRA_LIFE_MAX_WAIT = 35;
  /** The starting X value for the winning holes */
  private static final int WINNING_X_START = 120;
  /** The X separation between the winning holes */
  private static final int WINNING_X_SEPARATION = 192;
  /** A description of the placeholder markers indicating level progress */
  private static final String PROGRESS_MARKER = "filledhole";
  /** Manages the Sprites and associated functions for this level */
  private final SpriteAssetManager spriteManager;
  /** The number of the current level */
  private final int levelNumber;
  /** A list of all partial completion positions ('holes') for the level */
  private final List<Position> winningPositions = new ArrayList<>();
  /** Used for internal JVM logging */
  private final Logger log = Logger.getLogger(getClass().getSimpleName());
  /** The time between successive extra life object spawns */
  private int extraLifeSpawnWaitTime;
  /** Counter to keep track of the time elapsed since the last extra spawn time */
  private int extraLifeTimeDelta;
  /** Initialises a new core.Level */
  public Level(int level) {
    levelNumber = level;
    spriteManager = new SpriteAssetManager(this);
    /* loads all assets for this level */
    getSpriteManager().loadAssets();
    /* stores Position of all holes to be filled */
    for (int x = WINNING_X_START; x < App.getScreenWidth(); x += WINNING_X_SEPARATION) {
      winningPositions.add(new Position(x, WINNING_Y));
    }
    updateExtraLifeSpawnTime();
  }

  public static int getWinningY() {
    return WINNING_Y;
  }

  public SpriteAssetManager getSpriteManager() {
    return spriteManager;
  }

  /**
   * Returns the current level number
   *
   * @return Integer representing the level number
   */
  public int getLevelNumber() {
    return levelNumber;
  }

  /**
   * Generates a pseudo-random number in a specified range.
   *
   * @param min Range minimum
   * @param max Range maximum
   * @return num such that min <= num <= max
   */
  private int getRandomNumber(int min, int max) {
    int diff = max - min;
    Random random = new Random();
    int i = random.nextInt(diff + 1);
    i += min;
    return i;
  }

  /**
   * Changes the state of the world
   *
   * @param state The state to change LevelState to
   */
  public void changeWorldState(LevelState state) {
    log.info("Level State Changed: " + "Current State = " + state.toString());
    switch (state) {
      case PlayerDeath:
        if (!getSpriteManager().getPlayer().removeLife()) {
          App.closeGame();
        } else {
          getSpriteManager().resetPlayer();
        }
        break;
      case Finished:
        App.nextWorld();
        break;
      case PartlyFinished:
        Position winLocation = getClosestHolePosition(getSpriteManager().getPlayer().getLocation());
        getSpriteManager().resetPlayer();
        getSpriteManager().addFauxPlayer(PROGRESS_MARKER, winLocation);
        if (checkWin()) {
          changeWorldState(LevelState.Finished);
        }
        break;
    }
  }

  /**
   * Gets the center Position of the closest hole.
   *
   * @param location Position to search for closest hole near to
   * @return Position representing the center of the closest 'hole'
   */
  public Position getClosestHolePosition(Position location) {
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
   *
   * @return True if all holes have been filled, else False
   */
  public boolean checkWin() {
    for (Position winningPos : winningPositions) {
      boolean holeFilled = false;
      for (Sprite sprite : getSpriteManager().getSpritesAt(winningPos)) {
        if (sprite.getSpriteName().equals(PROGRESS_MARKER)) {
          holeFilled = true;
        }
      }
      if (!holeFilled) return false;
    }
    return true;
  }

  /** Randomises the spawn time for the next Extra Life power-up */
  private void updateExtraLifeSpawnTime() {
    extraLifeSpawnWaitTime = getRandomNumber(EXTRA_LIFE_MIN_WAIT, EXTRA_LIFE_MAX_WAIT);
    log.info("An extra life will spawn in " + extraLifeSpawnWaitTime + " seconds.");
  }

  /**
   * Spawns an Extra Life power-up on a random log that grants a player an extra life on collision
   */
  private void spawnExtraLife() {
    List<Sprite> logs = getSpriteManager().filterSprites(s -> s.getSpriteName().contains("log"));
    Sprite randomLog = logs.get(getRandomNumber(0, logs.size() - 1));
    PowerUp extraLife =
        new PowerUp(this, "extralife", "assets/extralife.png", randomLog.getLocation());
    extraLife.attachDriver((Driver) randomLog);
    log.info("Spawned extra life on log at " + extraLife.getLocation());
    getSpriteManager().addSprite(extraLife);
    updateExtraLifeSpawnTime();
  }

  /**
   * Update all time-supported Sprites
   *
   * @param input Describes device input state (keyboard/mouse/controller)
   * @param delta Time passed since last frame (milliseconds).
   */
  public void update(Input input, int delta) {
    extraLifeTimeDelta += delta;
    if (extraLifeTimeDelta / 1000 >= extraLifeSpawnWaitTime) {
      spawnExtraLife();
      extraLifeTimeDelta = 0;
    }
    List<Sprite> timeSupportSprites =
        getSpriteManager().filterSprites(s -> s instanceof TimeSupport);
    for (Sprite s : timeSupportSprites) {
      ((TimeSupport) s).onTimeTick(delta);
    }
  }

  /**
   * Signals all key-pressed sprites of a new key press
   *
   * @param key The ASCII value of the key pressed
   * @param c The ASCII character of the key pressed
   */
  public void onKeyPressed(int key, char c) {
    List<Sprite> keySupportSprites = getSpriteManager().filterSprites(s -> s instanceof KeySupport);
    for (Sprite s : keySupportSprites) {
      ((KeySupport) s).onKeyPress(key, c);
    }
  }

  /**
   * Renders all sprites on the Sprite Map onto the Level
   *
   * @param g The Graphics object to render the Level on
   */
  public void render(Graphics g) {
    if (getSpriteManager() == null || getSpriteManager().getSpriteMap() == null) {
      return;
    }
    for (Sprite s : getSpriteManager().getSpriteMap()) {
      s.render(g);
    }
  }
}
