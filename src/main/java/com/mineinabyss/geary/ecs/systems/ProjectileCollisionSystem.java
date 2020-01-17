package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Actor;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.ProjectileHitGround;
import com.mineinabyss.geary.ecs.components.Remove;

public class ProjectileCollisionSystem extends IteratingSystem {

  ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper.getFor(Projectile.class);
  ComponentMapper<Position> positionComponentMapper = ComponentMapper.getFor(Position.class);
  ComponentMapper<Actor> actorComponentMapper = ComponentMapper.getFor(Actor.class);

  public ProjectileCollisionSystem() {
    super(Family.all(Projectile.class, ProjectileHitGround.class, Position.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionComponentMapper.get(entity);
    Projectile projectile = projectileComponentMapper.get(entity);
    Actor actor = actorComponentMapper.get(entity);
    entity.removeAll();
    projectile.getCollisionComponents().get().forEach(entity::add);
    entity.add(actor);
    entity.add(position);

    if (projectile.isRemoveOnHit()) {
      entity.add(new Remove());
    }
  }
}
