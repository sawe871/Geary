package com.mineinabyss.geary.ecs.components.rendering;

import com.badlogic.ashley.core.Component;

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
