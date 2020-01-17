package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;

public class Speed implements Component {

  private final float multiplier;

  public Speed(float multiplier) {

    this.multiplier = multiplier;
  }

  public float getMultiplier() {
    return multiplier;
  }
}
