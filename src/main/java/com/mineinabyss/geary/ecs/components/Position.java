package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Shadows the Minecraft position of entities/blocks.
 */
public class Position implements Component {

  private final UUID worldUUID;
  private final Vector position;

  public Position(Location location) {
    this.worldUUID = location.getWorld().getUID();
    this.position = location.toVector();
  }

  public Location getLocation() {
    return position.toLocation(Bukkit.getWorld(worldUUID));
  }
}
