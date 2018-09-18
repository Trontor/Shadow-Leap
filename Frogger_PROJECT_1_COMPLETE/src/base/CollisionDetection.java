package base;

/** An implementation that permits collision checking with other Sprites */
interface CollisionDetection {
  void onCollision(Sprite sprite);

  void checkCollision();
}
