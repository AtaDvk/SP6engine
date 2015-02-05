package Bleach.PhysicsEngine;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import Bleach.Entity;
import Bleach.EntityTranslatable;
import Bleach.LevelInteractable;

public class Physique {

	@Deprecated
	private static long timestamp_OLD = System.nanoTime();

	private static long timestamp = System.currentTimeMillis();
	private static double gravity = 0.01;

	public static double distanceSquared(double x1, double y1, double x2, double y2) {
		double dX = x2 - x1;
		double dY = y2 - y1;
		return dX * dX + dY * dY;
	}

	public static boolean collides(EntityTranslatable first, EntityTranslatable second) {

		// Closest point on collision box
		Point2D.Double closestPoint = new Point2D.Double(0, 0);

		// Find closest x offset
		if (first.getPosition().x < second.getPosition().x) {
			closestPoint.x = second.getPosition().x;
		} else if (first.getPosition().x > second.getPosition().x + second.getRadius()) {
			closestPoint.x = second.getPosition().x + second.getRadius();
		} else {
			closestPoint.x = first.getPosition().x;
		}

		// Find closest y offset
		if (first.getPosition().y < second.getPosition().y) {
			closestPoint.y = second.getPosition().y;
		} else if (first.getPosition().y > second.getPosition().y + second.getRadius()) {
			closestPoint.y = second.getPosition().y + second.getRadius();
		} else {
			closestPoint.y = first.getPosition().y;
		}

		// If the closest point is inside the circle, the circles have collided
		if (distanceSquared(first.getPosition().x, first.getPosition().y, closestPoint.x, closestPoint.y) < first.getRadius() * 2) {
			return true;
		}
		return false;
	}

	public static boolean step(LevelInteractable currentLevelSetting) {

		// Flag that represents whether if a collision has occured during the
		// physics calculation step
		boolean collisionPresent = false;

		// List that will contain all the entities present on the level
		List<EntityTranslatable> entities = accumulateLevelEntityTranslatables(currentLevelSetting);

		// Current time in nanoseconds
		long currentTime = System.currentTimeMillis();
		double deltaTimeMilli = currentTime - timestamp;
		double deltaTimeSec = deltaTimeMilli / 1000.0;

		// TODO FPS-bottleneck starts here!
		// Print delta time using System.nanoTime()
		System.out.print("Tick time(render): " + ((System.nanoTime() - timestamp_OLD) / 1000000.0) + " seconds (" + (System.nanoTime() - timestamp_OLD) + " ns)");
		// Print delta time using System.currentTimeMillis()
		System.out.println(" which equals to: " + ((System.currentTimeMillis() - timestamp) / 1000.0) + " seconds (" + (System.currentTimeMillis() - timestamp) + " ms)");
		// TODO FPS-bottleneck ends here!

		// Iterate over entities and calculate physics
		for (EntityTranslatable entity : entities) {

			// Translate entity
			Point2D.Double newPosition = translate(entity, deltaTimeSec);

			// Checks whether if the new position collides with any object in
			// its way
			for (EntityTranslatable otherEntity : entities)

				// As long as it doesn't check for a collision with itself...
				if (entity != otherEntity)
					if (collides(entity, otherEntity)) {

						// Flag sets true
						collisionPresent = true;

						// Distance between the object's previous position and
						// the position of the object it has collided with
						double distanceBeforeCollision = distanceSquared(newPosition.x, newPosition.y, otherEntity.getPosition().x, otherEntity.getPosition().y);

						// Repositions the object to the position just before it
						// collides
						newPosition.x = Math.cos(entity.getVectorAngle()) * distanceBeforeCollision;
						newPosition.y = Math.sin(entity.getVectorAngle()) * distanceBeforeCollision;

						// Breaks out of the loop that checks for collisions
						break;
					}

		}

		// Update timestamp
		timestamp_OLD = System.nanoTime();
		timestamp = System.currentTimeMillis();

		return collisionPresent;
	}

	private static Point2D.Double translate(EntityTranslatable entity, double deltaTime) {
		// Gets current values
		double vectorAngle = entity.getVectorAngle();
		double velocity = entity.getVelocity();
		double magnitude = deltaTime * velocity;

		// Calculates the next position based on velocity
		Point2D.Double nextPosition = entity.getPosition();
		if (((Entity) entity).isMoving()) {
			nextPosition.x += Math.cos(vectorAngle) * magnitude;
			nextPosition.y += Math.sin(vectorAngle) * magnitude;
		}

		// Re-calculates the next Y-position based on velocity + gravity
		nextPosition.y += gravity * Math.pow(deltaTime, 2);

		// Sets the position to the newly calculated one
		entity.setPosition(nextPosition);

		return nextPosition;
	}
	
	private static List<EntityTranslatable> accumulateLevelEntityTranslatables(LevelInteractable level) {
		List<EntityTranslatable> entities = new ArrayList<>();

		// Accumulate objects on scene
		entities.addAll(level.getLoots());
		entities.addAll(level.getMobiles());
		entities.addAll(level.getPlayers());
		entities.addAll(level.getProjectiles());

		return entities;
	}

	public static double getGravity() {
		return gravity;
	}

	public static void setGravity(double gravity) {
		Physique.gravity = gravity;
	}
}
