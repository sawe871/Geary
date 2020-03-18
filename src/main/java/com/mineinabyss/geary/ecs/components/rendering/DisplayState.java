package com.mineinabyss.geary.ecs.components.rendering;

import com.badlogic.ashley.core.Component;

/**
 * Information about what model to display for an item or entity.
 */
public class DisplayState implements Component {

  private int modelNo;

  public DisplayState(int modelNo) {
    this.modelNo = modelNo;
  }

  public int getModelNo() {
    return modelNo;
  }

  public void setModelNo(int modelNo) {
    this.modelNo = modelNo;
  }
}
