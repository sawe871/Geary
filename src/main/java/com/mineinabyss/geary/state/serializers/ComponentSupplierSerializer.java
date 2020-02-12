package com.mineinabyss.geary.state.serializers;

import com.badlogic.ashley.core.Component;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mineinabyss.geary.ComponentSupplier;
import java.lang.reflect.Type;
import java.util.Set;

public class ComponentSupplierSerializer implements JsonSerializer<ComponentSupplier> {

  @Override
  public JsonElement serialize(ComponentSupplier componentSupplier, Type type,
      JsonSerializationContext context) {
    Set<Component> components = componentSupplier.get();

    JsonArray componentsJson = new JsonArray();

    components.stream().map(context::serialize).forEach(componentsJson::add);

    return componentsJson;
  }
}
