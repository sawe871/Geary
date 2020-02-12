package com.mineinabyss.geary.ecs;

import com.google.auto.value.AutoValue;
import java.util.UUID;

@AutoValue
public abstract class EntityRef {

  public abstract UUID getEntityId();

  public static EntityRef create(UUID newEntityId) {
    return new AutoValue_EntityRef(newEntityId);
  }
}
