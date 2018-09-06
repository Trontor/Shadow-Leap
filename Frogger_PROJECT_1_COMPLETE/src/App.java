/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2018
 * by Eleanor McMurtry, University of Melbourne
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

public class App extends BasicGame {
	
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    private static boolean keepRunning = true;
    private static World world;

    public App() {
        super("Shadow Leap");
    }

    @Override
    public void init(GameContainer gc)
            throws SlickException {
        world = new World();
    }

    @Override
    public void keyPressed(int key, char c) {
    	world.onKeyPressed(key, c);
    }
    
    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
            throws SlickException {
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
    public void render(GameContainer gc, Graphics g)
            throws SlickException {
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args) 
            throws SlickException {
        AppGameContainer app = new AppGameContainer(new App());
        app.setShowFPS(false);
        app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.start();
    }

    public static void CloseGame() { 
    	keepRunning = false;
    }
    
    /* to do enable world switching */
    public static void setWorld(World newWorld) {
    	world = newWorld;
    }

}