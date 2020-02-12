package com.mineinabyss.modelo;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

@AutoValue
public abstract class PluginResourcePackData {

  abstract Plugin getPlugin();
  abstract ImmutableSet<String> getTextures();
  abstract ImmutableMap<String, ModelOverride> getModels();

  public static Builder builder() {
    return new AutoValue_PluginResourcePackData.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    abstract Builder setPlugin(Plugin plugin);
    abstract ImmutableMap.Builder<String, ModelOverride> modelsBuilder();
    abstract ImmutableSet.Builder<String> texturesBuilder();

    public Builder addTextures(String... resourceUrls) {
      texturesBuilder().add(resourceUrls);
      return this;
    }

    public Builder addModel(String key, ModelOverride modelOverride) {
      modelsBuilder().put(key, modelOverride);
      return this;
    }

    public abstract PluginResourcePackData build();
  }


  @AutoValue
  public abstract static class ModelOverride {

    abstract String getPath();
    abstract ImmutableSet<Material> getMaterials();

    public static Builder builder() {
      return new AutoValue_PluginResourcePackData_ModelOverride.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

      abstract ImmutableSet.Builder<Material> materialsBuilder();

      public abstract Builder setPath(String newPath);
      public abstract ModelOverride build();

      public Builder addMaterial(Material material) {
        materialsBuilder().add(material);

        return this;
      }
    }
  }
}
