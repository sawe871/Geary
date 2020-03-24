package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class Actor implements Component {

  private UUID actor;

  public Actor(Entity actor) {
    this.actor = actor.getUniqueId();
  }

  public Entity getActor() {
    return Bukkit.getEntity(actor);
  }

}
