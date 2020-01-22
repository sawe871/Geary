package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Remove;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;

public class EntityRemovalSystem extends IteratingSystem {

  public EntityRemovalSystem() {
    super(Family.all(Remove.class).exclude(PullToLocation.class).get(), 100);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    getEngine().removeEntity(entity);
  }
}
