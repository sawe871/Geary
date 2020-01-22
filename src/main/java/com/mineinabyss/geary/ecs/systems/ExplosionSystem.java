package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.effect.Explosion;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ExplosionSystem extends ActingSystem {

  private ComponentMapper<Explosion> explosionComponentMapper = ComponentMapper
      .getFor(Explosion.class);
  private ComponentMapper<Position> positionComponentMapper = ComponentMapper
      .getFor(Position.class);

  public ExplosionSystem() {
    super(ImmutableList.of(Explosion.class, Position.class));
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Location location = positionComponentMapper.get(entity).getLocation();

    location.getWorld()
        .spawnParticle(Particle.EXPLOSION_LARGE, location, 1);

//    entity.remove(Activated.class);
  }
}
