package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import org.bukkit.Location;

/**
 * Component that represents a rope.
 */
public class Rope implements Component {

  private Location start;
  private Location end;

  public Rope() {
  }

  public Rope(Location start, Location end) {
    this.start = start;
    this.end = end;
  }

  public Location getStart() {
    return start;
  }

  public void setStart(Location start) {
    this.start = start;
  }

  public Location getEnd() {
    return end;
  }

  public void setEnd(Location end) {
    this.end = end;
  }
}
