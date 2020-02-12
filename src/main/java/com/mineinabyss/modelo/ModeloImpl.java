package com.mineinabyss.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipOutputStream;
import org.bukkit.plugin.Plugin;

public class ModeloImpl implements Modelo {

  private static final Path ITEM_ROOT = Paths.get("items/");

  private Set<PluginResourcePackData> data;

  public ModeloImpl() {
    this.data = new HashSet<>();
  }

  @Override
  public void addResourcePackData(PluginResourcePackData pluginResourcePackData) {
    data.add(pluginResourcePackData);
  }

  @Override
  public void buildResourcePack() {
    try {
      ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("test.zip"));
      zipOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    process(data.iterator().next());
  }


  private void process(PluginResourcePackData resourcePackData) {
    Plugin plugin = resourcePackData.getPlugin();

//    resourcePackData.getTextures().stream().collect(toImmutableMap(url -> url, url -> url));

    resourcePackData.getModels().entrySet().stream().map(Entry::getKey)
        .forEach(modelPath -> updateModel(plugin, modelPath));
  }

  private void updateModel(Plugin plugin, String modelPath) {
    InputStream file = plugin.getResource(modelPath);

    Gson gson = new GsonBuilder().create();
    JsonObject object = gson.fromJson(new InputStreamReader(file), JsonObject.class);

    JsonObject textures = object.getAsJsonObject("textures");

    textures.entrySet().forEach(stringJsonElementEntry -> {
      Path path = ITEM_ROOT.relativize(Paths.get(stringJsonElementEntry.getValue().getAsString()));
      Path resolvedPath = Paths.get("items", plugin.getName()).resolve(path);

      JsonPrimitive newValue = new JsonPrimitive(resolvedPath.toString());

      textures.add(stringJsonElementEntry.getKey(), newValue);
    });
  }

}
