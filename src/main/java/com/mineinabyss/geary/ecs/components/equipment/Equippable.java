package com.mineinabyss.geary.ecs.components.equipment;

import com.badlogic.ashley.core.Component;
import org.bukkit.inventory.EquipmentSlot;

public class Equippable implements Component {

  EquipmentSlot slot;

  public Equippable(EquipmentSlot slot) {
    this.slot = slot;
  }
}
