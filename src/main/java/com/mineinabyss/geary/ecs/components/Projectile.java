package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.Set;
import java.util.function.Supplier;

public class Projectile implements Component {

  private org.bukkit.entity.Projectile projectile;
  private Supplier<Set<Component>> collisionComponents;
  public Projectile(org.bukkit.entity.Projectile projectile,
      Supplier<Set<Component>> onHitComponents) {
    this.projectile = projectile;
    this.collisionComponents = onHitComponents;
  }

  public Supplier<Set<Component>> getCollisionComponents() {
    return collisionComponents;
  }

  public org.bukkit.entity.Projectile getProjectile() {
    return projectile;
  }

  public void setCollisionComponents(
      Supplier<Set<Component>> collisionComponents) {
    this.collisionComponents = collisionComponents;
  }
}
