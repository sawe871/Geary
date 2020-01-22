package com.mineinabyss.geary.ecs.components.rendering;

import com.badlogic.ashley.core.Component;

public class Name implements Component {

  private String name;

  public Name(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
