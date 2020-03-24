package com.mineinabyss.geary.state.adapters;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class EntityTypeAdapter implements JsonSerializer<Entity>, JsonDeserializer<Entity> {

  @Override
  public JsonElement serialize(Entity entity, Type type,
      JsonSerializationContext context) {
    return context.serialize(ImmutableList.copyOf(entity.getComponents()));
  }

  @Override
  public Entity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {

    Entity entity = new Entity();

    jsonElement.getAsJsonArray().forEach(e -> {
      entity.add(context.deserialize(e, Component.class));
    });

    return entity;
  }
}
