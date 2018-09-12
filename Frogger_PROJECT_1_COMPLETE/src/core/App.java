package core;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/** 
 * @author Rohyl Joshi
 * Designed for Project 1 of Object Oriented Software Development, Semester 2 - 2018
 */
public class App extends BasicGame {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    private static int NUM_WORLDS = 2;
    private static boolean keepRunning = true;
    private static World world;
    private static int worldNum = 0;
    public static int TILE_SIZE = 48;
    
    /**
     * Initializes the core.App class
     */
    public App() {
        super("Shadow Leap");
    }
    
    /* (non-Javadoc)
     * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
     */
    @Override
    public void init(GameContainer gc){
        world = new World(worldNum);
    }

    /* (non-Javadoc)
     * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
     */
    @Override
    public void keyPressed(int key, char c) {
    	world.onKeyPressed(key, c);
    }
    
    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta) {
    	if (!keepRunning) {
    		gc.exit();
    	}
        Input input = gc.getInput();
        world.update(input, delta);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g) {
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     * @throws SlickException Error running game
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new App());
        app.setShowFPS(false);
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
    }

    /**
     * Signals to close the game on next update
     */
    public static void CloseGame() { 
    	keepRunning = false;
    }
 
    /** [Future] Enables world switching functionality
     */
    public static void nextWorld() {
        if (worldNum > NUM_WORLDS) {
        	App.CloseGame();
        	return;
        }
        worldNum++;
        world = new World(worldNum);
    }

}