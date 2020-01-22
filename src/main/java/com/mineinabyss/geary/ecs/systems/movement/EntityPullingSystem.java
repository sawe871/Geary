package com.mineinabyss.geary.ecs.systems.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Actor;
import com.mineinabyss.geary.ecs.components.Position;
import com.mineinabyss.geary.ecs.components.effect.PullToLocation;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class EntityPullingSystem extends IteratingSystem {

  private static final double MAX_SPEED = 3;

  private ComponentMapper<Actor> actorComponentMapper = ComponentMapper.getFor(Actor.class);
  private ComponentMapper<Position> positionComponentMapper = ComponentMapper
      .getFor(Position.class);

  public EntityPullingSystem() {
    super(Family.all(Position.class, Actor.class, PullToLocation.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionComponentMapper.get(entity);
    org.bukkit.entity.Entity who = actorComponentMapper.get(entity).getActor();

    Location target = position.getLocation().add(0, 1, 0);

    Location from = who.getLocation();

    double distance = from.distance(target);

    double speed = Math.max(.7, Math.min(MAX_SPEED, distance / 10.0));

    Vector normalize = target.toVector().subtract(from.toVector()).normalize();
    BoundingBox newbb = who.getBoundingBox().shift(normalize);

    boolean collides = false;
    for (int x = 0; x < newbb.getWidthX(); x++) {
      for (int y = 0; y < newbb.getHeight(); y++) {
        for (int z = 0; z < newbb.getWidthZ(); z++) {
          if (y >= 0 && y <= 255) {
            Vector vector = newbb.getMin().clone().add(new Vector(x, y, z));
            collides |= !from.getWorld().getBlockAt(vector.toLocation(from.getWorld()))
                .isPassable();
          }
        }
      }
    }

    if (distance > 1 && !collides) {
      Vector velocity = normalize.multiply(speed);

      who.setVelocity(velocity);
    } else {
      entity.remove(PullToLocation.class);
    }
  }
}
