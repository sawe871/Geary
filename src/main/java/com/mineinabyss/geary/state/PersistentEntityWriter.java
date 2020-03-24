package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineinabyss.geary.ecs.ComponentSupplier;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import com.mineinabyss.geary.state.adapters.ComponentSupplierSerializer;
import com.mineinabyss.geary.state.adapters.ComponentTypeAdapterFactory;
import com.mineinabyss.geary.state.adapters.EntityTypeAdapter;

public class PersistentEntityWriter {

  private final EntityToUUIDMapper entityToUUIDMapper;

  private Gson gson;


  public PersistentEntityWriter(EntityToUUIDMapper entityToUUIDMapper) {
    this.entityToUUIDMapper = entityToUUIDMapper;

    gson = new GsonBuilder()
        .registerTypeAdapter(Entity.class, new EntityTypeAdapter())
        .registerTypeAdapterFactory(new ComponentTypeAdapterFactory())
        .registerTypeAdapter(ComponentSupplier.class, new ComponentSupplierSerializer())
        .setPrettyPrinting()
        .create();
  }

  public String saveEntityMapper() {
    return gson.toJson(entityToUUIDMapper.getEntities());
  }
}
