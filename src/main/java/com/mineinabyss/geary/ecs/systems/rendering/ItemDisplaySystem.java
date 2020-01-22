package com.mineinabyss.geary.ecs.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDisplaySystem extends IteratingSystem {

  private ComponentMapper<DisplayState> displayStateComponentMapper = ComponentMapper
      .getFor(DisplayState.class);

  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);

  public ItemDisplaySystem() {
    super(Family.all(DisplayState.class, Equipped.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    DisplayState displayState = displayStateComponentMapper.get(entity);

    int modelNo = displayState.getModelNo();

    ItemStack equipped = equippedComponentMapper.get(entity).getOwner().getInventory()
        .getItemInMainHand();

    if (equipped.hasItemMeta()) {
      ItemMeta itemMeta = equipped.getItemMeta();
      itemMeta.setCustomModelData(modelNo);
      equipped.setItemMeta(itemMeta);
    }
  }
}
