package com.mineinabyss.geary.ecs.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mineinabyss.geary.ecs.components.equipment.Durability;
import com.mineinabyss.geary.ecs.components.equipment.Equipped;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDisplaySystem extends IteratingSystem {

  private ComponentMapper<DisplayState> displayStateComponentMapper = ComponentMapper
      .getFor(DisplayState.class);
  private ComponentMapper<Durability> durabilityComponentMapper = ComponentMapper
      .getFor(Durability.class);

  private ComponentMapper<Equipped> equippedComponentMapper = ComponentMapper
      .getFor(Equipped.class);

  public ItemDisplaySystem() {
    super(Family.all(Equipped.class).one(DisplayState.class, Durability.class).get());
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Optional<DisplayState> displayState = Optional
        .ofNullable(displayStateComponentMapper.get(entity));
    Optional<Durability> durability = Optional
        .ofNullable(durabilityComponentMapper.get(entity));

    Optional<ItemStack> equipped = Optional
        .ofNullable(equippedComponentMapper.get(entity).getOwner()).map(
            Player::getInventory).map(PlayerInventory::getItemInMainHand);

    if (equipped.isPresent() && equipped.get().hasItemMeta()) {
      ItemMeta itemMeta = equipped.get().getItemMeta();
      displayState
          .map(DisplayState::getModelNo)
          .ifPresent(itemMeta::setCustomModelData);
      durability
          .ifPresent(dur -> setDurability(dur, equipped.get(), itemMeta));
      equipped.get().setItemMeta(itemMeta);
    }
  }

  private void setDurability(Durability durability, ItemStack itemStack, ItemMeta itemMeta) {
    if (itemMeta instanceof Damageable) {
      Damageable damageable = (Damageable) itemMeta;

      int maxDurability = itemStack.getType().getMaxDurability();

      int maxUses = durability.getMaxUses();
      int currentUses = durability.getCurrentUses();

      double multiplier = 1 - currentUses / (1.0 * maxUses);

      damageable.setDamage((int) (maxDurability * multiplier));

      itemStack.setItemMeta(itemMeta);
    }
  }
}
