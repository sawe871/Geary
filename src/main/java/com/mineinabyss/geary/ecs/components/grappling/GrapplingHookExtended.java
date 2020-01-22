package com.mineinabyss.geary.ecs.components.grappling;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class GrapplingHookExtended implements Component {

  Entity extended;

  public GrapplingHookExtended(Entity extended) {
    this.extended = extended;
  }

  public Entity getExtendedEntity() {
    return extended;
  }
}
