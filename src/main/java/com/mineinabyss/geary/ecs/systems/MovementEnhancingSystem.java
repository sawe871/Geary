package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Speed;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import org.bukkit.entity.Player;

public class MovementEnhancingSystem extends IteratingSystem implements EntityListener {

  private final static float DEFAULT_WALK_SPEED = .2f;
  ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper.getFor(Equipped.class);
  ComponentMapper<Speed> speedComponentMapper = ComponentMapper.getFor(Speed.class);


  public MovementEnhancingSystem() {
    super(Family.all(Equipped.class, Speed.class).get(), 6);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Equipped equipped = equippedComponentMapper.get(entity);

    Player owner = equipped.getOwner();

    owner
        .setWalkSpeed(
            DEFAULT_WALK_SPEED * speedComponentMapper.get(entity).getMultiplier());
  }

  @Override
  public void entityAdded(Entity entity) {

  }

  @Override
  public void entityRemoved(Entity entity) {
    equippedComponentMapper.get(entity).getOwner().setWalkSpeed(DEFAULT_WALK_SPEED);
  }
}
