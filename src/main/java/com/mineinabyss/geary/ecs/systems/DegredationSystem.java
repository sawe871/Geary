package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Remove;
import com.mineinabyss.geary.ecs.components.equipment.Degrading;
import com.mineinabyss.geary.ecs.components.equipment.Durability;

public class DegredationSystem extends IteratingSystem {

  private ComponentMapper<Durability> durabilityComponentMapper = ComponentMapper
      .getFor(Durability.class);

  public DegredationSystem() {
    super(Family.all(Durability.class, Degrading.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Durability durability = durabilityComponentMapper.get(entity);
    durability.setCurrentUses(durability.getCurrentUses() - 1);

    if (durability.getCurrentUses() <= 0) {
      entity.add(new Remove());
    }

    entity.remove(Degrading.class);
  }
}
