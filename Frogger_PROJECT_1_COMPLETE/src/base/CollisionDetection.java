package base;

/** An implementation that permits collisions and collision checking with other Sprites */
interface CollisionDetection {
	void onCollision(Sprite sprite);
	void checkCollision();
}
