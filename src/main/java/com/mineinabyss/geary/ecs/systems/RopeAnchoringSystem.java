package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Actor;
import com.mineinabyss.geary.ecs.components.Projectile;
import com.mineinabyss.geary.ecs.components.Rope;

// TODO iffy on this class
public class RopeAnchoringSystem extends IteratingSystem {

  private ComponentMapper<AnchorToActor> anchorToActorComponentMapper = ComponentMapper
      .getFor(AnchorToActor.class);
  private ComponentMapper<AnchorToProjectile> anchorToProjectileComponentMapper = ComponentMapper
      .getFor(AnchorToProjectile.class);
  private ComponentMapper<Actor> actorComponentMapper = ComponentMapper.getFor(Actor.class);
  private ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper
      .getFor(Projectile.class);
  private ComponentMapper<Rope> ropeComponentMapper = ComponentMapper.getFor(Rope.class);

  public RopeAnchoringSystem() {
    super(Family.one(AnchorToActor.class, AnchorToProjectile.class).all(Rope.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Rope rope = ropeComponentMapper.get(entity);
    if (anchorToActorComponentMapper.has(entity) && actorComponentMapper.has(entity)) {
      rope.setStart(actorComponentMapper.get(entity).getActor().getLocation());
    }
    if (anchorToProjectileComponentMapper.has(entity) && projectileComponentMapper.has(entity)) {
      rope.setEnd(projectileComponentMapper.get(entity).getProjectile().getLocation());
    }
  }
}
