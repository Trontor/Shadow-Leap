package core;

import base.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import customsprites.PowerUp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import utilities.Position;

/** A wrapper class that encapsulates all the sprites and events for a specified level. */
public class World {
  /* constants that describe the world*/
  public final AssetManager spriteManager;
  public final int WINNING_Y = 48;
  private final String WIN_MARKER = "filledhole";
  private final int EXTRA_LIFE_MIN_WAIT = 25;
  private final int EXTRA_LIFE_MAX_WAIT = 35;
  private final int WINNING_X_START = 120;
  private final int WINNING_X_SEPARATION = 192;
  private final List<Position> winningPositions = new ArrayList<>();
  private int extraLifeSpawnWaitTime;
  private int extraLifeTimeDelta;
  private Logger log = Logger.getLogger(getClass().getSimpleName());

  /** Initialises a new core.World */
  public World(int level) {
    spriteManager = new AssetManager(this, level);
    spriteManager.loadAssets();
    /* stores Position of all holes to be filled */
    for (int x = WINNING_X_START; x < App.SCREEN_WIDTH; x += WINNING_X_SEPARATION) {
      winningPositions.add(new Position(x, WINNING_Y));
    }
    updateExtraLifeSpawnTime();
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
   * @param state The state to change WorldState to
   */
  public void changeWorldState(WorldState state) {
    log.info("World State Changed: " + "Current State = " + state.toString());
    switch (state) {
      case Death:
        if (!spriteManager.getPlayer().removeLife()) {
          App.CloseGame();
        } else {
          spriteManager.resetPlayer();
        }
        break;
      case Finished:
        App.nextWorld();
        break;
      case PartlyFinished:
        Position winLocation = getClosestHolePosition(spriteManager.getPlayer().getPosition());
        spriteManager.resetPlayer();
        spriteManager.addFauxPlayer(WIN_MARKER, winLocation);
        if (checkWin()) {
          changeWorldState(WorldState.Finished);
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
      for (Sprite sprite : spriteManager.getSpritesAt(winningPos)) {
        if (sprite.getSpriteName().equals(WIN_MARKER)) {
          holeFilled = true;
        }
      }
      if (!holeFilled)
        return false;
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
    List<Sprite> logs = spriteManager.filterSprites(s -> s.getSpriteName().contains("log"));
    Sprite randomLog = logs.get(getRandomNumber(0, logs.size() - 1));
    PowerUp extraLife =
        new PowerUp(this, "extralife", "assets/extralife.png", randomLog.getPosition());
    extraLife.attachDriver((Driver) randomLog);
    log.info("Spawned extra life on log at " + extraLife.getPosition());
    spriteManager.addSprite(extraLife);
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
    List<Sprite> timeSupportSprites = spriteManager.filterSprites(s -> s instanceof TimeSupport);
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
    List<Sprite> keySupportSprites = spriteManager.filterSprites(s -> s instanceof KeySupport);
    for (Sprite s : keySupportSprites) {
      ((KeySupport) s).onKeyPress(key, c);
    }
  }

  /**
   * Renders all sprites on the Sprite Map onto the World
   *
   * @param g The Graphics object to render the World on
   */
  public void render(Graphics g) {
    if (spriteManager == null || spriteManager.getSpriteMap() == null) {
      return;
    }
    for (Sprite s : spriteManager.getSpriteMap()) {
      s.render(g);
    }
  }
}
