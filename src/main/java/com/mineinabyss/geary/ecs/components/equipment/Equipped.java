package com.mineinabyss.geary.ecs.components.equipment;

import com.badlogic.ashley.core.Component;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Indicates that an item is equipped.
 */
public class Equipped implements Component {

  private UUID owner;

  public Equipped(Player owner) {
    this.owner = owner.getUniqueId();
  }

  /**
   * The player that has this entity equipped.
   */
  public Player getOwner() {
    return Bukkit.getPlayer(owner);
  }
}
