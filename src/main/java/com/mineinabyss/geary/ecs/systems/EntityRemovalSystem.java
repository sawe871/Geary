package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Remove;

/**
 * Removes entities that are marked with {@link Remove}. This system should run last.
 */
public class EntityRemovalSystem extends IteratingSystem {

  public EntityRemovalSystem() {
    super(Family.all(Remove.class).get(), 100);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    getEngine().removeEntity(entity);
  }
}
