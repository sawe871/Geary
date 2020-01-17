package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.Set;
import java.util.function.Supplier;

public class ProjectileLauncher implements Component {

  private double speed;
  private final Supplier<Set<Component>> collisionComponents;
  private final Supplier<Set<Component>> launchComponents;

  public ProjectileLauncher(double speed,
      Supplier<Set<Component>> collisionComponents,
      Supplier<Set<Component>> launchComponents) {
    this.speed = speed;
    this.collisionComponents = collisionComponents;
    this.launchComponents = launchComponents;
  }

  public double getSpeed() {
    return speed;
  }

  public Supplier<Set<Component>> getCollisionComponents() {
    return collisionComponents;
  }

  public Supplier<Set<Component>> getLaunchComponents() {
    return launchComponents;
  }
}
