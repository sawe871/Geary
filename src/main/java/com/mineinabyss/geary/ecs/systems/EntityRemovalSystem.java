package com.mineinabyss.geary.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.Remove;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import org.bukkit.inventory.ItemStack;

public class EntityRemovalSystem extends IteratingSystem {

  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);

  public EntityRemovalSystem() {
    super(Family.all(Remove.class).get(), 100);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    // Remove if able
    if (equippedComponentMapper.has(entity)) {
      Equipped equipped = equippedComponentMapper.get(entity);
      ItemStack itemInMainHand = equipped.getOwner().getInventory()
          .getItemInMainHand();
      equipped.getOwner().getInventory().remove(itemInMainHand);
    }
    getEngine().removeEntity(entity);
  }
}
