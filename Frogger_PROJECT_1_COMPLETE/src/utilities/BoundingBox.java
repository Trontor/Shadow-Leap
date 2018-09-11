/**
 * BoundingBox complete class for SWEN20003: Object Oriented Software Development 2018 by Eleanor
 * McMurtry, University of Melbourne modified by Rohyl Joshi, ^
 */
package utilities;

import org.newdawn.slick.Image;

public class BoundingBox {
  private static final float FUZZ = 0.95f;

  private float left;
  private float top;
  private float width;
  private float height;

  public BoundingBox(Position centerPos, float width, float height) {
    setWidth(width);
    setHeight(height);
    setX(centerPos.getX());
    setY(centerPos.getX());
  }

  public BoundingBox(Image img, Position centerPos) {
    setWidth(img.getWidth());
    setHeight(img.getHeight());
    setX(centerPos.getX());
    setY(centerPos.getY());
  }

  public BoundingBox(BoundingBox bb) {
    width = bb.width;
    height = bb.height;
    left = bb.left;
    top = bb.top;
  }

  public void setX(float x) {
    left = x - width / 2;
  }

  public void setY(float y) {
    top = y - height / 2;
  }

  private void setWidth(float w) {
    width = w * FUZZ;
  }

  private void setHeight(float h) {
    height = h * FUZZ;
  }

  public float getLeft() {
    return left;
  }

  public float getTop() {
    return top;
  }

  public float getRight() {
    return left + width;
  }

  public float getBottom() {
    return top + height;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public boolean intersects(BoundingBox other) {
    return !(other.left > getRight()
        || other.getRight() < left
        || other.top > getBottom()
        || other.getBottom() < top);
  }
}
