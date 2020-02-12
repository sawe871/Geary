package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineinabyss.geary.ComponentSupplier;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.state.serializers.ComponentSupplierSerializer;
import com.mineinabyss.geary.state.serializers.ComponentTypeAdapterFactory;
import com.mineinabyss.geary.state.serializers.EntityTypeAdapter;

public class EntityMapperSaver {

  private final EntityMapper entityMapper;

  private Gson gson;


  public EntityMapperSaver(EntityMapper entityMapper) {
    this.entityMapper = entityMapper;

    gson = new GsonBuilder()
        .registerTypeAdapter(Entity.class, new EntityTypeAdapter())
        .registerTypeAdapterFactory(new ComponentTypeAdapterFactory())
        .registerTypeAdapter(ComponentSupplier.class, new ComponentSupplierSerializer())
        .setPrettyPrinting()
        .create();
  }

  public String saveEntityMapper() {
    return gson.toJson(entityMapper.getEntities());
  }
}
