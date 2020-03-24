package com.mineinabyss.geary.state.adapters;

import static com.google.common.truth.Truth.assertThat;

import com.badlogic.ashley.core.Component;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineinabyss.geary.ecs.components.rendering.DisplayState;
import org.junit.Test;

public class ComponentTypeAdapterFactoryTest {

  @Test
  public void testDeserialize() {
    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new ComponentTypeAdapterFactory())
        .create();

    DisplayState src = new DisplayState(2);
    String serialized = gson.toJson(src);

    DisplayState dest = (DisplayState) gson.fromJson(serialized, Component.class);

    assertThat(dest.getModelNo()).isEqualTo(src.getModelNo());
  }
}