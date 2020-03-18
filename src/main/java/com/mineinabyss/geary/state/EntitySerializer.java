package com.mineinabyss.geary.state;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.mineinabyss.geary.ecs.ComponentSupplier;
import com.mineinabyss.geary.ecs.EntityToUUIDMapper;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class EntitySerializer {

  private final EntityToUUIDMapper entityToUUIDMapper;

  public EntitySerializer(EntityToUUIDMapper entityToUUIDMapper) {
    this.entityToUUIDMapper = entityToUUIDMapper;
  }

  public String serializeEntityMap(EntityToUUIDMapper entityToUUIDMapper) {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    return gson.toJson(entityToUUIDMapper);
  }

  public JsonElement serialize(Entity entity) {
    Type listType = new TypeToken<List<Component>>() {
    }.getType();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Entity.class, new EntityIdSerializer(entityToUUIDMapper))
        .registerTypeAdapter(ComponentSupplier.class, new ComponentSupplierSerializer())
        .registerTypeAdapter(listType,
            new ComponentSerializer())
        .create();

    JsonObject object = new JsonObject();
    object
        .add("components", gson.toJsonTree(ImmutableList.copyOf(entity.getComponents()), listType));
    object.add("id", new JsonPrimitive(entityToUUIDMapper.getId(entity).toString()));

    return gson.toJsonTree(object);
  }

  private static class EntityIdSerializer implements JsonSerializer<Entity> {

    private final EntityToUUIDMapper entityToUUIDMapper;

    public EntityIdSerializer(EntityToUUIDMapper entityToUUIDMapper) {
      this.entityToUUIDMapper = entityToUUIDMapper;
    }

    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(entityToUUIDMapper.getId(src).toString());
    }
  }

  private static class ComponentSerializer implements JsonSerializer<List<Component>> {

    @Override
    public JsonElement serialize(List<Component> src, Type typeOfSrc,
        JsonSerializationContext context) {

      JsonArray results = new JsonArray();

      src.stream().forEach(component -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("data", context.serialize(component));
        jsonObject.add("class", new JsonPrimitive(component.getClass().getCanonicalName()));
        results.add(jsonObject);
      });

      return results;
    }
  }

  private static class ComponentSupplierSerializer implements JsonSerializer<ComponentSupplier> {

    @Override
    public JsonElement serialize(ComponentSupplier componentSupplier, Type type,
        JsonSerializationContext context) {
      Set<Component> components = componentSupplier.get();

      JsonArray componentsJson = new JsonArray();

      components.stream().map(context::serialize).forEach(componentsJson::add);

      return componentsJson;
    }
  }
}
