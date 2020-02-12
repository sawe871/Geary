package com.mineinabyss.geary.state;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mineinabyss.geary.ComponentSupplier;
import com.mineinabyss.geary.ecs.EntityMapper;
import com.mineinabyss.geary.ecs.EntityRef;
import com.mineinabyss.geary.state.serializers.EntityRefDeserializer;
import java.lang.reflect.Type;
import java.util.stream.StreamSupport;

public class DeserialationUtil {

  public Entity deserializeEntity(String json) {
    Gson gson = new GsonBuilder().registerTypeAdapter(Entity.class, new EntityDeserializer())
        .registerTypeAdapter(
            EntityRef.class, new EntityRefDeserializer())
        .registerTypeAdapter(ComponentSupplier.class, new ComponentSupplierDeserializer())
        .create();

    return gson.fromJson(json, Entity.class);
  }

  public EntityMapper deserializeIdMap(String json) {
    Gson gson = new GsonBuilder().create();

    return gson.fromJson(json, EntityMapper.class);
  }

  static class EntityDeserializer implements JsonDeserializer<Entity> {

    @Override
    public Entity deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

      JsonObject jsonObject = jsonElement.getAsJsonObject();

      JsonArray components = jsonObject.getAsJsonArray("components");

      Entity entity = new Entity();
      ImmutableList.copyOf(components).stream()
          .map(jsonElement1 -> deserializeComponent(jsonElement1, jsonDeserializationContext))
          .forEach(entity::add);

      return entity;
    }

    private Component deserializeComponent(JsonElement jsonElement,
        JsonDeserializationContext jsonDeserializationContext) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      String className = jsonObject.get("class").getAsString();

      Class<? extends Component> clazz = (Class<? extends Component>) unchecked(
          () -> Class.forName(className));

      return jsonDeserializationContext.deserialize(jsonObject.get("data"), clazz);
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {

      T get() throws Exception;
    }

    private <T> T unchecked(ThrowingSupplier<T> supplier) {
      try {
        return supplier.get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static class ComponentSupplierDeserializer implements
      JsonDeserializer<ComponentSupplier> {

    @Override
    public ComponentSupplier deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext context) throws JsonParseException {
      return () -> StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), false)
          .map(jsonElement1 -> (Component) context.deserialize(jsonElement, Component.class))
          .collect(toImmutableSet());
    }
  }
}
