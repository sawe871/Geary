package com.mineinabyss.geary.ecs;

import com.badlogic.ashley.core.Entity;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class EntityWrapper {

  public abstract Entity getEntity();
}
