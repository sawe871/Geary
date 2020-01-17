package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Shadows the Minecraft position of entities/blocks.
 */
public class Position implements Component {

  private final Location location;

  public Position(Location location) {
    this.location = location;
  }

  public Location getLocation() {
    return location.clone();
  }
}
