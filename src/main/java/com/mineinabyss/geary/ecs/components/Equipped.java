package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import org.bukkit.entity.Player;

/**
 * Indicates that an item is equipped.
 */
public class Equipped implements Component {

  public Equipped(Player owner) {
    this.owner = owner;
  }

  Player owner;

  public Player getOwner() {
    return owner;
  }
}
