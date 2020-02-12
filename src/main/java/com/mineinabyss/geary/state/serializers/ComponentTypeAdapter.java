package com.mineinabyss.geary.state.serializers;

import com.badlogic.ashley.core.Component;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class ComponentTypeAdapter implements JsonSerializer<Component>,
    JsonDeserializer<Component> {

  @Override
  public JsonElement serialize(Component component, Type type, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("data", context.serialize(component));
    jsonObject.add("class", new JsonPrimitive(component.getClass().getCanonicalName()));
    return jsonObject;
  }

  @Override
  public Component deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
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
