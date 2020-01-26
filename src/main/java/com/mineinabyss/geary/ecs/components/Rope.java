package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import com.mineinabyss.geary.core.LocationWrapper;
import org.bukkit.Color;

/**
 * Component that represents a rope.
 */
public class Rope implements Component {

  private LocationWrapper start;
  private LocationWrapper end;
  private Color color;

  public Rope(LocationWrapper start, LocationWrapper end, Color color) {
    this.start = start;
    this.end = end;
    this.color = color;
  }

  public LocationWrapper getStart() {
    return start;
  }

  public LocationWrapper getEnd() {
    return end;
  }

  public Color getColor() {
    return color;
  }
}
