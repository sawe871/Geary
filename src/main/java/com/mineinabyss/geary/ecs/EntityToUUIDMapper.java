package com.mineinabyss.geary.ecs;

import static com.google.common.base.Preconditions.checkState;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.google.common.collect.ImmutableMap;
import com.mineinabyss.geary.state.Exclude;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Bidirectional map of ECS entities to Minecraft objects with UUIDs.
 */
public class EntityToUUIDMapper implements EntityListener {

  private Map<UUID, Entity> idToEntityMap;

  @Exclude
  private Map<Entity, UUID> entityToIdMap;

  public EntityToUUIDMapper() {
    idToEntityMap = new HashMap<>();
    entityToIdMap = new HashMap<>();
  }

  @Override
  public void entityAdded(Entity entity) {
    // Sometimes we manually add entities
    if (entityToIdMap.containsKey(entity)) {
      return;
    }

    UUID uuid = UUID.randomUUID();

    idToEntityMap.put(uuid, entity);
    entityToIdMap.put(entity, uuid);
  }

  public void entityAdded(Entity entity, UUID entityId) {
    checkState(!entityToIdMap.containsKey(entity));
    checkState(!idToEntityMap.containsKey(entityId));

    entityToIdMap.put(entity, entityId);
    idToEntityMap.put(entityId, entity);
  }

  @Override
  public void entityRemoved(Entity entity) {
    UUID uuid = entityToIdMap.remove(entity);
    idToEntityMap.remove(uuid);
  }

  public UUID getId(Entity entity) {
    return entityToIdMap.get(entity);
  }

  public Entity getEntity(UUID entityId) {
    return idToEntityMap.get(entityId);
  }

  public Entity getEntity(EntityRef entityId) {
    return getEntity(entityId.getEntityId());
  }

  public Map<UUID, Entity> getEntities() {
    return ImmutableMap.copyOf(idToEntityMap);
  }
}
