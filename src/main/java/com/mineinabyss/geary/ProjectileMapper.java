package com.mineinabyss.geary;

import com.badlogic.ashley.core.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Projectile;

public class ProjectileMapper {

  private Map<UUID, Entity> projectileMap;

  public ProjectileMapper() {
    projectileMap = new HashMap<>();
  }

  public void addProjectile(Projectile projectile, Entity entity) {
    projectileMap.put(projectile.getUniqueId(), entity);
  }

  public void removeProjectile(Projectile projectile) {
    projectileMap.remove(projectile.getUniqueId());
  }

  public Entity getEntity(Projectile projectile) {
    return projectileMap.get(projectile.getUniqueId());
  }
}
