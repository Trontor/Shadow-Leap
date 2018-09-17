package base;

import core.App;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import utilities.Position;
import core.World;

/**
 * A Sprite that can move itself and other sprites that are 'riding' the sprite at a specified
 * velocity
 *
 * @author Rohyl Joshi
 * @see <a href="github.com/Trontor">Hosted on GitHub</a>
 */
public class Driver extends MovingSprite {
  private Logger log = Logger.getLogger(getClass().getSimpleName());
  private List<Sprite> ridingSprites = new ArrayList<>();
  private boolean rideable = true;
  private List<Position> playerSnapPositions = new ArrayList<>();

  public Driver(World world, String name, String imgSrc, Position centerPos, Velocity vel) {
    super(world, name, imgSrc, centerPos, vel);
  }

  /**
   * Specifies whether the Driver can take any riders
   *
   * @return True if the Driver can take riders, else False
   */
  public boolean isRideable() {
    return rideable;
  }

  /**
   * Modifies the ability of the Driver to take riders
   *
   * @param rideable True to enable riders, else False
   */
  public void setRideable(boolean rideable) {
    this.rideable = rideable;
  }

  /**
   * Updates a list of possible positions that the rider can snap to (helper function to prevent
   * pixel glitching).
   */
  private void updateSnapPositions(){
    playerSnapPositions.clear();
    float firstSnap = getLeftAnchor().getX() + App.TILE_SIZE/2f;
    int j = 0;
    int numSnaps = Math.round(getWidth()/App.TILE_SIZE);
    for (float i = firstSnap; j < numSnaps; i+= App.TILE_SIZE, j++){
      playerSnapPositions.add(new Position(i, getLocation().getY()));
    }
  }

  /**
   * Repositions the rider onto a discrete point on the Driver in an attempt to prevent pixel
   * glitching. This looks visually appealing and logical.
   *
   * @param rider The rider to reposition
   */
  private void snapRider(Rideable rider) {
    updateSnapPositions();
    Sprite ridingSprite = (Sprite) rider;
    Position closestSnap = ridingSprite.getLocation().getClosest(playerSnapPositions);
    ridingSprite.setLocation(closestSnap);
  }

  /**
   * Adds a new rider to the currently tracked riders
   *
   * @param rider The rider to add
   */
  public void addRider(Rideable rider) {
    if (!(rider instanceof Sprite)) {
      log.info("Cannot add rider because it is not of type Sprite");
      return;
    }
    ridingSprites.add((Sprite) rider);
    snapRider(rider);
  }

  /**
   * Removes a rider from the currently tracked riders
   *
   * @param rider The rider to remove
   */
  public void removeRider(Rideable rider) {
    if (rider instanceof Sprite) {
      ridingSprites.remove(rider);
    }
  }

  /**
   * Modified behaviour that moves not only the sprite but also the riders on the sprite at the same
   * velocity as the Driver
   *
   * @param delta The time in milliseconds since the last tick
   */
  @Override
  public void onTimeTick(int delta) {
    if (ridingSprites.size() > 0) {
      float deltaX = super.getMovementVelocity().getHorizontal() * delta;
      float deltaY = super.getMovementVelocity().getVertical() * delta;
      /* for loop to avoid concurrent modifcation that can result
         from setLocationDelta deleting the sprite */
      for (int i = 0; i < ridingSprites.size(); i++) {
        Sprite sprite = ridingSprites.get(i);
        sprite.setLocationDelta(deltaX, deltaY);
      }
    }
    super.onTimeTick(delta);
  }

  /** Extends the MovingSprite respawn functionality to also respawn all riders with the Driver */
  @Override
  public void respawn() {
    super.respawn();
    if (ridingSprites.size() > 0) {
      for (Sprite sprite : ridingSprites) {
        sprite.setLocation(getLocation());
      }
    }
  }
}
