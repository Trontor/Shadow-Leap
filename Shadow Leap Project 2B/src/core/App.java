package core;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Main entry point for the Slick2D application
 *
 * @author Rohyl Joshi Designed for Project 2B of Object Oriented Software Development, Semester 2 -
 *     2018
 */
public class App extends BasicGame {

  /** Describes the number of pixels the screen can render horizontally */
  private static final int SCREEN_WIDTH = 1024;
  /** Describes the number of pixels the screen can render vertically */
  private static final int SCREEN_HEIGHT = 768;
  /** Describes the number of pixels the base sprite object should */
  private static final int TILE_LENGTH = 48;
  /** Keeps track of the maximum number of worlds in the game */
  private static final int NUMBER_OF_WORLDS = 2;
  /** Represents the level to run at the start of the game */
  private static final int SPAWN_WORLD_NUM = 0;
  private static final String GAME_TITLE = "Shadow Leap";
  /** Flag that indicates whether the game should continue running */
  private static boolean keepRunning = true;
  /** Represents the world to be currently rendered */
  private static Level currentLevel;

  /** Initializes the core.App class */
  public App() {
    super(GAME_TITLE);
  }

  /**
   * Gets the screen width
   *
   * @return Width of the screen
   */
  public static int getScreenWidth() {
    return SCREEN_WIDTH;
  }

  /**
   * Gets the screen height
   *
   * @return Height of the screen
   */
  public static int getScreenHeight() {
    return SCREEN_HEIGHT;
  }

  /**
   * Gets the side length of the base square tile
   *
   * @return The side length of the basic tile object
   */
  public static int getTileLength() {
    return TILE_LENGTH;
  }

  /**
   * Start-up method. Creates the game and runs it.
   *
   * @param args Command-line arguments (ignored).
   * @throws SlickException Error running game.
   */
  public static void main(String[] args) throws SlickException {
    AppGameContainer app = new AppGameContainer(new App());
    app.setShowFPS(false);
    app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
    app.start();
  }

  /** Signals to close game on next update */
  public static void closeGame() {
    keepRunning = false;
  }

  /** Make app change to the next currentLevel */
  public static void nextWorld() {
    int currentWorldNum = currentLevel.getLevelNumber();
    if (currentWorldNum++ >= NUMBER_OF_WORLDS) {
      App.closeGame();
      return;
    }
    currentLevel = new Level(++currentWorldNum);
  }

  /**
   * Initialises the game
   *
   * @param gc The GameContainer object to use for rendering
   */
  @Override
  public void init(GameContainer gc) {
    currentLevel = new Level(SPAWN_WORLD_NUM);
  }

  /**
   * Event that is called when a key is pressed
   *
   * @param key The ASCII value of the key pressed
   * @param c The ASCII character of the key pressed
   */
  @Override
  public void keyPressed(int key, char c) {
    currentLevel.onKeyPressed(key, c);
  }

  /**
   * Update the game state for a frame.
   *
   * @param gc The Slick game container object.
   * @param delta Time passed since last frame (milliseconds).
   */
  @Override
  public void update(GameContainer gc, int delta) {
    if (!keepRunning) {
      gc.exit();
    }
    Input input = gc.getInput();
    currentLevel.update(input, delta);
  }

  /**
   * Render the entire screen, so it reflects the current game state.
   *
   * @param gc The Slick game container object.
   * @param g The Slick graphics object, used for drawing.
   */
  @Override
  public void render(GameContainer gc, Graphics g) {
    currentLevel.render(g);
  }
}
