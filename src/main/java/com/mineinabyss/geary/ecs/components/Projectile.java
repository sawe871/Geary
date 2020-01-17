package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.Set;
import java.util.function.Supplier;

public class Projectile implements Component {

  private org.bukkit.entity.Projectile projectile;
  private Supplier<Set<Component>> collisionComponents;
  private boolean removeOnHit;

  public Projectile(org.bukkit.entity.Projectile projectile,
      Supplier<Set<Component>> onHitComponents, boolean removeOnHit) {
    this.projectile = projectile;
    this.collisionComponents = onHitComponents;
    this.removeOnHit = removeOnHit;
  }

  public Supplier<Set<Component>> getCollisionComponents() {
    return collisionComponents;
  }

  public org.bukkit.entity.Projectile getProjectile() {
    return projectile;
  }

  public boolean isRemoveOnHit(){
    return removeOnHit;
  }
}
