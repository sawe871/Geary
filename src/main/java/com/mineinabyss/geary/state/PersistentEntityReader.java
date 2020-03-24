package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.state.adapters.ComponentTypeAdapterFactory;
import com.mineinabyss.geary.state.adapters.EntityRefDeserializer;
import com.mineinabyss.geary.state.adapters.EntityTypeAdapter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class PersistentEntityReader {

  private final EntityToUUIDMapper entityToUUIDMapper;
  private final Engine engine;
  public static final Type UUID_ENTITY_MAP_TYPE = new TypeToken<Map<UUID, Entity>>() {
  }.getType();
  private Gson gson;

  public PersistentEntityReader(EntityToUUIDMapper entityToUUIDMapper, Engine engine) {
    this.entityToUUIDMapper = entityToUUIDMapper;
    this.engine = engine;
    gson = new GsonBuilder()
        .registerTypeAdapterFactory(new ComponentTypeAdapterFactory())
        .registerTypeAdapter(Entity.class, new EntityTypeAdapter())
        .registerTypeAdapter(EntityRef.class, new EntityRefDeserializer())
        .create();
  }

  public void loadEntityMapper(Reader config) {
    Map<UUID, Entity> entities = gson.fromJson(config, UUID_ENTITY_MAP_TYPE);

    entities.forEach((uuid, entity) -> entityToUUIDMapper.entityAdded(entity, uuid));
    entities.forEach((uuid, entity) -> engine.addEntity(entity));
  }
}
