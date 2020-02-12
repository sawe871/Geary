package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import com.google.common.base.Preconditions;
import com.mineinabyss.geary.ComponentSupplier;
import java.util.UUID;
import org.bukkit.Bukkit;

public class Projectile implements Component {

  private UUID projectile;
  private ComponentSupplier collisionComponents;

  public Projectile(org.bukkit.entity.Projectile projectile,
      ComponentSupplier onHitComponents) {
    Preconditions.checkNotNull(projectile);
    this.projectile = projectile.getUniqueId();
    this.collisionComponents = onHitComponents;
  }

  public Projectile(UUID projectileUUID,
      ComponentSupplier onHitComponents) {
    this.projectile = projectileUUID;
    this.collisionComponents = onHitComponents;
  }

  public ComponentSupplier getCollisionComponents() {
    return collisionComponents;
  }

  public org.bukkit.entity.Projectile getProjectile() {
    return (org.bukkit.entity.Projectile) Bukkit.getEntity(projectile);
  }

  public void setCollisionComponents(
      ComponentSupplier collisionComponents) {
    this.collisionComponents = collisionComponents;
  }
}
