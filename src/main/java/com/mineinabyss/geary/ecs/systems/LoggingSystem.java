package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.EntitySystem;

public class LoggingSystem extends EntitySystem {

  @Override
  public void update(float deltaTime) {
    System.out.println(String.format("Num Entities: %s", getEngine().getEntities().size()));
  }
}
