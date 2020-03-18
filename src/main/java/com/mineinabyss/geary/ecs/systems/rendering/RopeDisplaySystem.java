package com.mineinabyss.geary.ecs.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Rope;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.util.Vector;

/**
 * System that draws ropes using particles.
 */
public class RopeDisplaySystem extends IteratingSystem {

  ComponentMapper<Rope> ropeComponentMapper = ComponentMapper.getFor(Rope.class);

  public RopeDisplaySystem() {
    super(Family.all(Rope.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Rope rope = ropeComponentMapper.get(entity);

    Location start = rope.getStart().getLocation();
    Location end = rope.getEnd().getLocation();

    Vector step = end.toVector().subtract(start.toVector()).normalize().multiply(.1);
    Vector drawLocation = start.toVector();

    do {
      start.getWorld().spawnParticle(Particle.REDSTONE, drawLocation.getX(), drawLocation.getY(),
          drawLocation.getZ(), 1,
          new DustOptions(rope.getColor(), .2f));
      drawLocation.add(step);
    } while (drawLocation.distance(end.toVector()) > step.length());
  }
}
