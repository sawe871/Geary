package com.mineinabyss.geary.state.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mineinabyss.geary.ecs.EntityRef;
import java.lang.reflect.Type;
import java.util.UUID;

public class EntityRefDeserializer implements JsonDeserializer<EntityRef> {

  @Override
  public EntityRef deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return EntityRef
        .create(UUID.fromString(jsonElement.getAsJsonObject().get("entityId").getAsString()));
  }
}
