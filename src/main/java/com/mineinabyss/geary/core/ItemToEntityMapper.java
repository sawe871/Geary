package com.mineinabyss.geary.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ItemToEntityMapper {

  private final Engine engine;
  private Map<UUID, Entity> uuidToEntity;

  public ItemToEntityMapper(Engine engine) {
    this.engine = engine;
    uuidToEntity = new HashMap<>();
  }

  public Optional<Entity> getEntityForUUID(UUID uuid) {
    return Optional.ofNullable(uuidToEntity.get(uuid));
  }

  public void addEntityWithUUID(UUID uuid, Entity entity) {
    uuidToEntity.put(uuid, entity);
    engine.addEntity(entity);
  }
}
