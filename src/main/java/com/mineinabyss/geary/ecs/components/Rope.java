package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import com.mineinabyss.geary.core.LocationWrapper;

/**
 * Component that represents a rope.
 */
public class Rope implements Component {

  private LocationWrapper start;
  private LocationWrapper end;

  public Rope(LocationWrapper start, LocationWrapper end) {
    this.start = start;
    this.end = end;
  }

  public LocationWrapper getStart() {
    return start;
  }

  public LocationWrapper getEnd() {
    return end;
  }
}
