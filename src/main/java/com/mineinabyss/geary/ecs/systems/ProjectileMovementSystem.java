package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.ProjectileHitGround;

public class ProjectileMovementSystem extends IteratingSystem {

  ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper.getFor(Projectile.class);

  public ProjectileMovementSystem() {
    super(Family.all(Projectile.class).exclude(ProjectileHitGround.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    entity.add(new Position(projectileComponentMapper.get(entity).getProjectile().getLocation()));
  }
}
