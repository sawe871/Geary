package com.mineinabyss.geary.ecs.components.grappling;

import com.badlogic.ashley.core.Component;
import com.mineinabyss.geary.ecs.EntityRef;

public class GrapplingHookExtended implements Component {

  private EntityRef extended;

  public GrapplingHookExtended(EntityRef entityRef) {
    this.extended = entityRef;
  }

  public EntityRef getExtendedEntity() {
    return extended;
  }
}
