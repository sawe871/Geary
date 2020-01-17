package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.Optional;

public class Equipment implements Component {

  Entity feet;
  Entity mainHand;

  public Optional<Entity> getFeet() {
    return Optional.ofNullable(feet);
  }

  public void setFeet(Entity feet) {
    this.feet = feet;
  }

  public Optional<Entity> getMainHand() {
    return Optional.ofNullable(mainHand);
  }

  public void setMainHand(Entity mainHand) {
    this.mainHand = mainHand;
  }
}
