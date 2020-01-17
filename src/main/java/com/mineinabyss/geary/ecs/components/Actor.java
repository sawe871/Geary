package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import org.bukkit.entity.Entity;

public class Actor implements Component {

  private Entity actor;

  public Actor(Entity actor) {
    this.actor = actor;
  }

  public Entity getActor() {
    return actor;
  }

  public void setActor(Entity actor) {
    this.actor = actor;
  }
}
