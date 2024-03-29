package utilities;

import org.newdawn.slick.Image;

public class BoundingBox {
  private static final float FUZZ = 0.95f;
  private boolean disableFuzz = false;

  private float left;
  private float top;
  private float width;
  private float height;

  public BoundingBox(Image img, Position centerPos) {
    setWidth(img.getWidth());
    setHeight(img.getHeight());
    setX(centerPos.getX());
    setY(centerPos.getY());
  }

  public BoundingBox(Image img, Position centerPos, boolean disableFuzz) {
    this.disableFuzz = disableFuzz;
    setWidth(img.getWidth());
    setHeight(img.getHeight());
    setX(centerPos.getX());
    setY(centerPos.getY());
  }

  public void setX(float x) {
    left = x - width / 2;
  }

  public void setY(float y) {
    top = y - height / 2;
  }

  private void setWidth(float w) {
    width = disableFuzz ? w : w * FUZZ;
  }

  private void setHeight(float h) {
    height = disableFuzz ? h : h * FUZZ;
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

  public boolean intersects(BoundingBox other) {
    return !(other.left > getRight()
        || other.getRight() < left
        || other.top > getBottom()
        || other.getBottom() < top);
  }

  public boolean intersects(Position other, float height, float width) {
    return !(other.getX() - width / 2 > getRight()
        || other.getX() + width / 2 < left
        || other.getY() + height / 2 < getBottom()
        || other.getY() - height / 2 > top);
  }

  @Override
  public String toString() {
    return String.format(
        "Top = %.2f, Bottom = %.2f, Left = %.2f, Right = %.2f",
        getTop(), getBottom(), getLeft(), getRight());
  }
}
