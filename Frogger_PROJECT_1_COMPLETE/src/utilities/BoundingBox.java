package utilities;

import org.newdawn.slick.Image;

public class BoundingBox {
  private static final float FUZZ = 0.95f;

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

  public boolean intersects(BoundingBox other) {
    return !(other.left > getRight()
        || other.getRight() < left
        || other.top > getBottom()
        || other.getBottom() < top);
  }

  public boolean intersects(Position other) {
    boolean belowTop = other.getY() > getTop();
    boolean aboveBottom = other.getY() < getBottom();
    boolean rightOfLeft = other.getX() > getLeft();
    boolean leftOfRight = other.getX() < getRight();
    return belowTop && aboveBottom && rightOfLeft && leftOfRight;
  }

  @Override
  public String toString() {
    return String.format("Top = %.2f, Bottom = %.2f, Left = %.2f, Right = %.2f", getTop(), getBottom(), getLeft(), getRight());
  }
}
