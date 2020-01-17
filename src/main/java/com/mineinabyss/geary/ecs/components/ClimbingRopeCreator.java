package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;

public class ClimbingRopeCreator implements Component {

  private int maxLength;

  public ClimbingRopeCreator(int maxLength) {
    this.maxLength = maxLength;
  }

  public int getMaxLength() {
    return maxLength;
  }
}
