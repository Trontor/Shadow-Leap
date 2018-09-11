package base;

import java.util.List;
import java.util.stream.Collectors;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utilities.Position;
import core.*;

public class Player extends Sprite
    implements KeySupport, TimeSupport, CollisionDetection, Boundable, Rideable {

  private int lives = 3;

  private final Position LIFE_COUNTER_POS = new Position(24, 744);
  private Image livesImage = null;
  private final float COUNTER_DISTANCE = 32;

  public int getLives() {
    return lives;
  }

  /**
   * Decrements number of lives left for player
   * @return true, if lives decremented sucessfully. false if no lives left
   */
  public boolean removeLife() {
    System.out.println("Lives left = " + lives);
    if (lives == 1) {
      return false;
    }
    else {
      lives--;
      return true;
    }
  }

  private Driver currentlyDriving;
	public Player(World spawnWorld, String imageSrc, Position centerPos){
		super(spawnWorld, "base.Player", imageSrc, centerPos);
    try {
      livesImage = new Image("assets/lives.png");
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

	private boolean futureOutOfBounds(Position futurePos) {
		float offset = super.getWidth()/2;
		float rightX = futurePos.getX() + offset;
		float leftX = futurePos.getX() - offset;
		float topY = futurePos.getY() + offset;
		float bottomY = futurePos.getY() - offset;
		float maxX = App.SCREEN_WIDTH;
		float maxY = App.SCREEN_HEIGHT;
		return rightX > maxX || leftX < 0 || topY > maxY || bottomY < 0;
	}

	@Override
	public void setLocation(Position center) {
		if (currentlyDriving == null && futureOutOfBounds(center)) {
			return;
		} else if (super.outOfBounds()) {
			onBoundsExtended();
		}
    super.setLocation(center);
	} 

	/* Handles movement for player
	 * @see base.KeySupport#onKeyPress(int, char)
	 */
	public void onKeyPress(int key, char c) {
		/* determine key press and set appropriate position offset */
		float newX = centerPosition.getX();
		float newY = centerPosition.getY();
		float delta = super.getWidth();
		switch(key) {
			case Input.KEY_DOWN:
				newY += delta;
				break;
			case Input.KEY_UP:
				newY -= delta;
				break;
			case Input.KEY_LEFT:
				newX -= delta;
				break;
			case Input.KEY_RIGHT:
				newX += delta;
				break;
		}
		Position newPos = new Position(newX, newY);
    for (Sprite s : super.getWorld().getSpritesAt(newPos)) {
      if (getWorld().getAssetType(s.getSpriteName()) == AssetType.SOLID_TILE) {
        System.out.println("Intersection with solid, can't move.");
        return;
      }
    }
		setLocation(newPos);
	}

	@Override
	public void onBoundsExtended() {
		/* signals player has died */
		getWorld().ChangeWorldState(WorldState.Death);
	}
	
	/* (non-Javadoc)
	 * @see base.CollisionDetection#onCollision(base.Sprite)
	 */
	public void onCollision(Sprite sprite) {
		/* signals player has died */
		getWorld().ChangeWorldState(WorldState.Death);
	}

	/* Determines if the player has collided with an object
	 * @see base.TimeSupport#onTimeTick(int)
	 */

	public void onTimeTick(int delta) {
        List<Sprite> rideableSprites = getWorld().getIntersectingSprites(this).stream()
                .filter(s-> s instanceof Driver)
                .collect(Collectors.toList());
        if (rideableSprites.size() > 0) {
            Driver driver = (Driver)rideableSprites.get(0);
            if (currentlyDriving != null && currentlyDriving != driver) {
                detachDriver();
            } else if (currentlyDriving != driver && driver.isRideable()) {
                attachDriver(driver);
            }
            return;
        } else if (currentlyDriving != null) {
            /* remove base.Sprite from object */
          detachDriver();
        }
        checkCollision();
        if (centerPosition.getY() <= getWorld().WINNING_Y) {
          getWorld().ChangeWorldState(WorldState.PartlyFinished);
          return;
        }
	}

	@Override
	public boolean checkCollision(){
    List<Sprite> collidableSprites = getWorld().getIntersectingSprites(this).stream()
        .filter(s -> s instanceof Collidable)
        .collect(Collectors.toList());
    if (collidableSprites.size() > 0) {
      /* we are colliding */
      onCollision(collidableSprites.get(0));
      return true;
    }
    return false;
  }

  @Override
  public void detachDriver() {
    System.out.println("Detached driver");
    currentlyDriving.removeRider(this);
    currentlyDriving = null;
  }

  @Override
  public void attachDriver(Driver driver) {
    System.out.println("Attached new driver");
    currentlyDriving = driver;
    driver.onTouch(this);
  }

  @Override
  public void render(Graphics g) {
    super.render(g);
    if (livesImage == null)
        return;
    for (int i = 0; i < getLives(); i++){
      float imageX = LIFE_COUNTER_POS.getX() + COUNTER_DISTANCE * i;
      float imageY = LIFE_COUNTER_POS.getY();
      imageX -= livesImage.getWidth()/2;
      imageY -= livesImage.getHeight()/2;
      g.drawImage(livesImage, imageX, imageY);
    }
  }
}
