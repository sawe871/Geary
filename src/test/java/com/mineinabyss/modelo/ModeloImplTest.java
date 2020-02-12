package com.mineinabyss.modelo;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mineinabyss.modelo.PluginResourcePackData.ModelOverride;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ModeloImplTest {

  @Test
  public void something() {
    Plugin plugin = mock(Plugin.class);
    when(plugin.getName()).thenReturn("plugino");
    when(plugin.getResource(anyString()))
        .thenReturn(getClass().getClassLoader().getResourceAsStream("test_model.json"));

    PluginResourcePackData pack = PluginResourcePackData.builder()
        .setPlugin(plugin)
        .addTextures("somethingElse")
        .addModel("someKey", ModelOverride
            .builder()
            .addMaterial(Material.DIAMOND_AXE)
            .addMaterial(Material.GOLDEN_SWORD)
            .setPath("something")
            .build())
        .build();

    Modelo modelo = new ModeloImpl();

    modelo.addResourcePackData(pack);
    modelo.buildResourcePack();
  }


}