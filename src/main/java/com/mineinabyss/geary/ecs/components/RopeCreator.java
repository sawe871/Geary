package com.mineinabyss.geary.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.Optional;

public class RopeCreator implements Component {

  private Entity rope;
  private boolean attachToCreatedRope;

  public RopeCreator(boolean attachToCreatedRope) {
    this.attachToCreatedRope = attachToCreatedRope;
  }

  public Optional<Entity> getRope() {
    return Optional.ofNullable(rope);
  }

  public void setRope(Entity rope) {
    this.rope = rope;
  }

  public boolean shouldAttachToCreatedRope() {
    return attachToCreatedRope;
  }
}
