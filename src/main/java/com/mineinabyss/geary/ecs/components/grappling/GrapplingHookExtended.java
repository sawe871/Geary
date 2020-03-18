package com.mineinabyss.geary.ecs.components.grappling;

import com.badlogic.ashley.core.Component;
import com.mineinabyss.geary.ecs.EntityRef;

/**
 * Stores information about an extended grappling hook.
 */
public class GrapplingHookExtended implements Component {

  private EntityRef extended;

  public GrapplingHookExtended(EntityRef entityRef) {
    this.extended = entityRef;
  }

  /**
   * Reference to the projectile that was fired by this entity.
   */
  public EntityRef getExtendedEntity() {
    return extended;
  }
}
