package com.mineinabyss.geary.state.adapters;

import static com.mineinabyss.geary.ThrowingSupplier.unchecked;

import com.badlogic.ashley.core.Component;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ComponentTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    ComponentTypeAdapterFactory factory = this;

    if (Component.class.isAssignableFrom(typeToken.getRawType())) {

      final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);

      return new TypeAdapter<T>() {
        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
          jsonWriter.beginObject();
          jsonWriter.name(t.getClass().getCanonicalName());
          delegate.write(jsonWriter, t);
          jsonWriter.endObject();
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
          jsonReader.beginObject();
          String className = jsonReader.nextName();
          Class<?> unchecked = unchecked(() -> Class.forName(className));
          T component = (T) gson.getDelegateAdapter(factory, TypeToken.get(unchecked))
              .read(jsonReader);
          jsonReader.endObject();
          return component;
        }
      };
    }
    return null;
  }
}
